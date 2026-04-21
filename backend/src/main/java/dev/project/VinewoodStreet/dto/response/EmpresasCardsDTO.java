package dev.project.VinewoodStreet.dto.response;

import java.io.Serializable;

public record EmpresasCardsDTO(
    Long id,
    String sigla,
    String nome,
    Double precoAtual,
    Double variacaoUltimas24h

) implements Serializable {}
