package com.ifsul.lawbot.dto.cliente;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditarClienteRequest(
        String nome,
        String email,
        String senha

) {
}
