package com.ifsul.lawbot.dto.advogado;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record EditarAdvogadoRequest(
        String nome,
        @Email
        String email,
        @CPF
        String senha
) {
}