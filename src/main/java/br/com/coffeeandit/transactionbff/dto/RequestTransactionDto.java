package br.com.coffeeandit.transactionbff.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"uuid", "situacao"})
public class RequestTransactionDto extends TransactionDto {
    private SituacaoEnum situacao;
    private LocalDateTime data;
}
