package dev.project.VinewoodStreet.dto.response;

import java.time.LocalDateTime;

public record HistoricoPrecoDTO(

    Double preco,
    LocalDateTime dataHora

) { }
