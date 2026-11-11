
package br.com.fiap.retech.dto.request;

import jakarta.validation.constraints.*;

public record TrocarSenhaRequest(

        @NotBlank
        String senhaAtual,

        @NotBlank
        @Size(min = 8, max = 72)
        String novaSenha

) {}
