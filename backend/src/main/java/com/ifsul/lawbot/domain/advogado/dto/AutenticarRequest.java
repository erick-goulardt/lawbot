package com.ifsul.lawbot.domain.advogado.dto;

import jakarta.validation.constraints.NotBlank;

public record AutenticarRequest(
        @NotBlank
        String login,
        @NotBlank
        String senha) {
}
