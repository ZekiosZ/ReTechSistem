package br.com.fiap.retech.dto.request;
    import jakarta.validation.constraints.*;
    public record CategoriaEletroRequest(@NotBlank @Size(max = 50) String nomeCategoria,
        @NotNull @Min(0) @Max(9999) Integer pontosPorUnidade) {}
