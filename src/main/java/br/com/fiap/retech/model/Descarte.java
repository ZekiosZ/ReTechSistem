package br.com.fiap.retech.model;
    import java.time.LocalDateTime;
    public record Descarte(Long idDescarte, Long idUsuario, Long idMaquina, Long idCategoria, Long idInstituicao, LocalDateTime dataRegistro, int quantidade, int pontosGerados) {}
