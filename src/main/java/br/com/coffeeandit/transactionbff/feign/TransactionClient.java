package br.com.coffeeandit.transactionbff.feign;

import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "transaction", url = "${transaction.url}")
public interface TransactionClient {

    @GetMapping(path = "/transaction/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TransactionDto> buscarTransacoes(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta);
}
