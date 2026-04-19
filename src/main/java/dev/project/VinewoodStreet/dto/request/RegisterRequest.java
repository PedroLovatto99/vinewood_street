package dev.project.VinewoodStreet.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(

        @NotNull(message = "e-mail é obrigatório")
        String email,

        @NotNull(message = "nome é obrigatório")
        String nome,

        @NotNull(message = "senha é obrigatória")
        String senha

) {}
