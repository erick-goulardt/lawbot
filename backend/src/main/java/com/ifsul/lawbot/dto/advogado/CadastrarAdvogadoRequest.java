package com.ifsul.lawbot.dto.advogado;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CadastrarAdvogadoRequest(
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String oab,
        @CPF
        @NotBlank
        String cpf,
        @NotNull
        LocalDate dataNascimento) {
}
