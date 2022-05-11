package br.com.coffeeandit.transactionbff.dto;

import java.time.LocalDateTime;

public class RequestTransactionDto extends TransactionDto {
    private SituacaoEnum situacao;
    private LocalDateTime data;
}
