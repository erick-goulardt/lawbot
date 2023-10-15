package com.ifsul.lawbot.domain.advogado.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CadastrarAdvogadoRequest(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String oab,
        @NotBlank
        String cpf,
        @NotNull
        LocalDate dataNascimento) {
}
