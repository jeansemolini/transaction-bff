package br.com.coffeeandit.transactionbff.feign;

import br.com.coffeeandit.transactionbff.dto.LimiteDiario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "limites", url = "${limites.url}")
public interface LimiteClient {

    @GetMapping(path = "/limite-diario/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE)
    LimiteDiario buscarLimiteDiario(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta);
}
