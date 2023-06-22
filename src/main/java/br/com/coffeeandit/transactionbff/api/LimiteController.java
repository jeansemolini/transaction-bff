package br.com.coffeeandit.transactionbff.api;

import br.com.coffeeandit.transactionbff.domain.LimiteService;
import br.com.coffeeandit.transactionbff.dto.LimiteDiario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private LimiteService limiteService;

    public LimiteController(LimiteService limiteService) {
        this.limiteService = limiteService;
    }

    @GetMapping(value = "/{agencia}/{conta}")
    public Mono<LimiteDiario> buscarLimiteDiario(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta) {
        return limiteService.buscarLimiteDiario(agencia, conta);
    }
}
