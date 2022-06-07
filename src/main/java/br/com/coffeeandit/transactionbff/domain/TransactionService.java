package br.com.coffeeandit.transactionbff.domain;

import br.com.coffeeandit.transactionbff.dto.RequestTransactionDto;
import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import br.com.coffeeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private TransactionRedisRepository transactionRedisRepository;
    private RetryTemplate retryTemplate;

    public TransactionService(TransactionRedisRepository transactionRedisRepository, RetryTemplate retryTemplate) {
        this.transactionRedisRepository = transactionRedisRepository;
        this.retryTemplate = retryTemplate;
    }

    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());
        return Optional.of(transactionRedisRepository.save(requestTransactionDto));
    }

//    Retry utilizando anotação
//    @Retryable(value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
//    public Optional<TransactionDto> findbyId(final String id) {
//        log.info("Consultando Redis...");
//        return transactionRedisRepository.findById(id);
//    }

    public Optional<TransactionDto> findbyId(final String id) {
        return retryTemplate.execute(ret -> {
            log.info("Consultando Redis...");
            return transactionRedisRepository.findById(id);
        });
    }
}
