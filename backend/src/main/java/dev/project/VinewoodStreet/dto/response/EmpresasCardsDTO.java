package dev.project.VinewoodStreet.dto.response;

public record EmpresasCardsDTO(
    Long id,
    String sigla,
    String nome,
    Double precoAtual,
    Double variacaoUltimas24h

) { }
