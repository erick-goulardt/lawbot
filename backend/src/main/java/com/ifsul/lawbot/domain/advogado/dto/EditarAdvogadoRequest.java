package com.ifsul.lawbot.domain.advogado.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarAdvogadoRequest(
        @NotNull
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {
}