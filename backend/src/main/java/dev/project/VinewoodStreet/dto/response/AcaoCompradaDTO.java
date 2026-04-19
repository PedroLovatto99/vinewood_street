package dev.project.VinewoodStreet.dto.response;

public record AcaoCompradaDTO(
    Long id,
    String sigla,
    String nomeEmpresa,
    Integer quantidade,
    Double precoMedioCompra,
    Double precoAtualMercado,
    Double lucroPrejuizoPercentual
) {}
