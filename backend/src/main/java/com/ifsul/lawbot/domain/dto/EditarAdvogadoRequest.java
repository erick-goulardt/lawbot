package com.ifsul.lawbot.domain.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record EditarAdvogadoRequest(
        @NotNull
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {
}