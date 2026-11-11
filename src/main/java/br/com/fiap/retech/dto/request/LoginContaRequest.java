
package br.com.fiap.retech.dto.request;

import jakarta.validation.constraints.*;

public record LoginContaRequest(
        @NotBlank String cpf,
        @NotBlank String senha
) {}
