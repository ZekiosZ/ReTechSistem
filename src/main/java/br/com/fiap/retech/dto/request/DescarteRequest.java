package br.com.fiap.retech.dto.request;
    import jakarta.validation.constraints.*;
    public record DescarteRequest(@NotNull @Positive Long idUsuario,
        @NotNull @Positive Long idMaquina,
        @NotNull @Positive Long idCategoria,
        @NotNull @Positive Long idInstituicao,
        @NotNull @Min(1) @Max(999) Integer quantidade) {}
