package br.com.coffeeandit.transactionbff.mock;

import br.com.coffeeandit.transactionbff.dto.BeneficiarioDto;
import br.com.coffeeandit.transactionbff.dto.Conta;
import br.com.coffeeandit.transactionbff.dto.RequestTransactionDto;
import br.com.coffeeandit.transactionbff.dto.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DataMock {

    public static RequestTransactionDto getTransactionDto() {
        var transaction = new RequestTransactionDto();
        transaction.setUuid(UUID.randomUUID());
        transaction.setValor(BigDecimal.valueOf(1298));
        transaction.setTipoTransacao(TipoTransacao.TED);
        transaction.setData(LocalDateTime.now());

        Conta conta = getConta();
        transaction.setConta(conta);

        BeneficiarioDto beneficiarioDto = getBeneficiarioDto();
        transaction.setBeneficiario(beneficiarioDto);
        return transaction;
    }

    public static BeneficiarioDto getBeneficiarioDto() {
        BeneficiarioDto beneficiarioDto = new BeneficiarioDto();
        beneficiarioDto.setConta("17850-1");
        beneficiarioDto.setAgencia("0144");
        beneficiarioDto.setCodigoBanco(741L);
        beneficiarioDto.setNomeFavorecido("Nome do Favorecido");
        return beneficiarioDto;
    }

    public static Conta getConta() {
        Conta conta = new Conta();
        conta.setCodigoConta(4809L);
        conta.setCodigoAgencia(204L);
        return conta;
    }

}
