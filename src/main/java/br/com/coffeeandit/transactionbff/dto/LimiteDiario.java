package br.com.coffeeandit.transactionbff.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class LimiteDiario {

    private Long id;
    private Long agencia;
    private Long conta;
    private LocalDateTime data;
    private BigDecimal valor;
}
