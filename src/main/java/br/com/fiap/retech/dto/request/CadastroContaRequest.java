
package br.com.fiap.retech.dto.request;

import jakarta.validation.constraints.*;

public record CadastroContaRequest(

        @NotBlank
        @Size(max = 100)
        String nomeCompleto,

        @NotBlank
        @Pattern(regexp = "[0-9]{11}")
        String cpf,

        @NotBlank
        @Size(min = 8, max = 72)
        String senha

) {}
