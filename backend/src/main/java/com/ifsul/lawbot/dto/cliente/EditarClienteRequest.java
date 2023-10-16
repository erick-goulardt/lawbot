package com.ifsul.lawbot.dto.cliente;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarClienteRequest(
        @NotNull
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {
}
