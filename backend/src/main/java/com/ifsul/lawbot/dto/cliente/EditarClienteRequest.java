package com.ifsul.lawbot.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record EditarClienteRequest(
        String nome,
        @Email
        String email,
        @CPF
        String cpf

) {
}
