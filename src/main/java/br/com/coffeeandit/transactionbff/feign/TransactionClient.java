package br.com.coffeeandit.transactionbff.feign;

import br.com.coffeeandit.transactionbff.dto.LimiteDiario;
import br.com.coffeeandit.transactionbff.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "transaction", url = "${transaction.url}")
public interface TransactionClient {

    @RequestMapping(path = "/transaction/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    List<TransactionDto> buscarTransacoes(@PathVariable("agencia") Long agencia, @PathVariable("conta") Long conta);
}
