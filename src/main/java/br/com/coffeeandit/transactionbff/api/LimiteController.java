package br.com.coffeeandit.transactionbff.api;

import br.com.coffeeandit.transactionbff.dto.LimiteDiario;
import br.com.coffeeandit.transactionbff.feign.LimiteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private LimiteClient limiteClient;

    public LimiteController(LimiteClient limiteClient) {
        this.limiteClient = limiteClient;
    }

    @GetMapping(value = "/{agencia}/{conta}")
    public LimiteDiario buscarLimiteDiario(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta) {
        return limiteClient.buscarLimiteDiario(agencia, conta);
    }
}
