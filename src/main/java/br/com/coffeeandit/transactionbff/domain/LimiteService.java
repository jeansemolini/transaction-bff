package br.com.coffeeandit.transactionbff.domain;

import br.com.coffeeandit.transactionbff.dto.LimiteDiario;
import br.com.coffeeandit.transactionbff.feign.LimiteClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Supplier;

@Service
public class LimiteService {

    private LimiteClient limiteClient;
    private CircuitBreaker countCircuitBreaker;

    public LimiteService(LimiteClient limiteClient, CircuitBreaker countCircuitBreaker) {
        this.limiteClient = limiteClient;
        this.countCircuitBreaker = countCircuitBreaker;
    }

    public Mono<LimiteDiario> buscarLimiteDiario(final Long agencia, final Long conta) {
        return buscarLimiteDiarioSupplier(agencia, conta);
    }

    private Mono<LimiteDiario> buscarLimiteDiarioSupplier(final Long agencia, final Long conta) {
        var limiteDiarioSup = countCircuitBreaker.decorateSupplier(() ->
                limiteClient.buscarLimiteDiario(agencia, conta));

        return Mono.fromSupplier(Decorators
                .ofSupplier(limiteDiarioSup)
                .withCircuitBreaker(countCircuitBreaker)
                .withFallback(Arrays.asList(CallNotPermittedException.class),
                        e -> this.getStaticLimit())
                .decorate()
        );
    }

    private LimiteDiario getStaticLimit() {
        LimiteDiario limiteDiario = new LimiteDiario();
        limiteDiario.setValor(BigDecimal.ZERO);
        return limiteDiario;
    }
}
