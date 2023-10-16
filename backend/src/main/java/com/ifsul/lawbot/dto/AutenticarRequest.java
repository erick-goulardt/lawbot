package com.ifsul.lawbot.dto;

import jakarta.validation.constraints.NotBlank;

public record AutenticarRequest(
        @NotBlank
        String login,
        @NotBlank
        String senha) {
}
