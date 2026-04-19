package dev.project.VinewoodStreet.dto.response;

import dev.project.VinewoodStreet.enums.TipoImpacto;

import java.time.LocalDateTime;

public record EventoNoticiaDTO(

    String siglaEmpresa,
    String titulo,
    String conteudo,
    TipoImpacto impacto,
    LocalDateTime dataPublicacao
) {
}
