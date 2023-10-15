package com.ifsul.lawbot.domain.cliente.dto;

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
