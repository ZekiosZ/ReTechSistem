package br.com.fiap.retech.dto.request;
    import jakarta.validation.constraints.*;
    public record MaquinaRequest(@NotBlank @Size(max = 100) String bairroLocalizacao,
        @NotBlank @Pattern(regexp = "[0-9]{8}", message = "CEP deve ter 8 digitos") String cep,
        @NotBlank @Size(max = 20) String statusOperacao) {}
