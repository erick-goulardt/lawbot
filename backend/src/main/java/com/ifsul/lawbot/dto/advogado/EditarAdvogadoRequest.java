package com.ifsul.lawbot.dto.advogado;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarAdvogadoRequest(
        Long id,
        String nome,
        String email,
        String senha
) {
}