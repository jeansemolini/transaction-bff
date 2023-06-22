package br.com.coffeeandit.transactionbff.domain;

import br.com.coffeeandit.transactionbff.dto.RequestTransactionDto;
import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import br.com.coffeeandit.transactionbff.exception.NotFoundException;
import br.com.coffeeandit.transactionbff.feign.TransactionClient;
import br.com.coffeeandit.transactionbff.redis.TransactionRedisRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRedisRepository transactionRedisRepository;
    private final RetryTemplate retryTemplate;
    private final ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate;
    private final TransactionClient transactionClient;

    @Value("${app.topic}")
    private String topic;

    public TransactionService(TransactionRedisRepository transactionRedisRepository, RetryTemplate retryTemplate,
                              ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate, TransactionClient transactionClient) {
        this.transactionRedisRepository = transactionRedisRepository;
        this.retryTemplate = retryTemplate;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.transactionClient = transactionClient;
    }

    @Transactional
    @Retryable(value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public Mono<RequestTransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        log.info("Salvando a requisição {}", requestTransactionDto);

        return Mono.fromCallable(() -> {
            requestTransactionDto.setData(LocalDateTime.now());
            requestTransactionDto.naoAnalisada();
            return transactionRedisRepository.save(requestTransactionDto);

        })
        .doOnError(throwable -> {
            log.error(throwable.getMessage(), throwable);
            throw new NotFoundException("Unable to find resource");
        })
        .doOnSuccess(requestTransactionDto1 -> {
            log.info("Transação enviada com sucesso: {}", requestTransactionDto1);
            reactiveKafkaProducerTemplate.send(topic, requestTransactionDto)
                    .doOnSuccess(voidSenderResult -> log.info(voidSenderResult.toString()))
                    .subscribe();
        })
        .doFinally(signalType -> {
            if (signalType.compareTo(SignalType.ON_COMPLETE) == 0) {
                log.info("Mensagem enviada ao kafka com sucesso {}", requestTransactionDto);
            }
        });
    }

    public Optional<TransactionDto> findbyId(final String id) {
        return retryTemplate.execute(ret -> {
            log.info("Consultando Redis...");
            return transactionRedisRepository.findById(id);
        });
    }

    public Flux<List<TransactionDto>> findByAgenciaAndContaFlux(final Long agencia, Long conta) {
        List<TransactionDto> byAgenciaAndConta = findByAgenciaAndConta(agencia, conta);
        return Flux.fromIterable(byAgenciaAndConta).cache(Duration.ofSeconds(2))
                .limitRate(200).defaultIfEmpty(new TransactionDto())
                .buffer(200);
    }

    public List<TransactionDto> findByAgenciaAndConta(final Long agencia, Long conta) {
        return transactionClient.buscarTransacoes(agencia, conta);
    }

    public List<TransactionDto> findAll() {
        return retryTemplate.execute(ret -> {
            log.info("Consultando Redis...");
            return transactionRedisRepository.findAll();
        });
    }
}
