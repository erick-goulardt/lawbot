package com.ifsul.lawbot.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CadastrarClienteRequest(
        @NotNull
        Long idAdvogado,
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String cpf,
        @NotNull
        LocalDate dataNascimento
) {
}