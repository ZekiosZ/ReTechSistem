package br.com.fiap.retech.dto.request;
    import jakarta.validation.constraints.*;
    public record InstituicaoRequest(@NotBlank @Size(max = 100) String nomeFantasia,
        @NotBlank @Pattern(regexp = "[0-9]{14}", message = "CNPJ deve ter 14 digitos") String cnpj,
        @NotBlank @Size(max = 30) String tipoParceiro) {}
