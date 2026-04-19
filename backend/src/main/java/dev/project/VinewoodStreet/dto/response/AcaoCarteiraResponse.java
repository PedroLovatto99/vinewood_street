package dev.project.VinewoodStreet.dto.response;

import java.util.List;

public record AcaoCarteiraResponse(
    Double saldoDisponivel,
    List<AcaoCompradaDTO> minhasAcoes
) { }
