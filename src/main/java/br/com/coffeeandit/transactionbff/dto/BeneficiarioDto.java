package br.com.coffeeandit.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BeneficiarioDto {

    @NotNull(message = "Informar o CPF.")
    @Schema(description = "CPF fo Beneficiário")
    private Long CPF;
    @NotNull(message = "Informar o código do banco de destino.")
    @Schema(description = "Conta de origem da transação")
    private Long codigoBanco;
    @NotNull(message = "Informar a agência de destino.")
    @Schema(description = "Agência de destino")
    private String agencia;
    @NotNull(message = "Informar a conta de destino.")
    @Schema(description = "Conta de destino")
    private String conta;
    @NotNull(message = "Informar o nome do Favorecido.")
    @Schema(description = "Nome do Favorecido")
    private String nomeFavorecido;
}
