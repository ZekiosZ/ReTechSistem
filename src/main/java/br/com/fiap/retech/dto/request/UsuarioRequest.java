package br.com.fiap.retech.dto.request;
    import jakarta.validation.constraints.*;
    public record UsuarioRequest(@NotBlank @Size(max = 100) String nomeCompleto,
        @NotBlank @Pattern(regexp = "[0-9]{11}", message = "CPF deve ter 11 digitos") String cpf) {}
