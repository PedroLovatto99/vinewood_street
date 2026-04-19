package dev.project.VinewoodStreet.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VenderAcaoRequest(

        @NotBlank(message = "A sigla da empresa é obrigatória.")
        String sigla,

        @NotNull(message = "A quantidade não pode ser nula.")
        @Min(value = 1, message = "Você precisa vender pelo menos 1 ação.")
        Integer quantidade

) {}
