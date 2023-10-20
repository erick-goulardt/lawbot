package com.ifsul.lawbot.dto.advogado;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarAdvogadoRequest(
        String nome,
        String email,
        String senha
) {
}