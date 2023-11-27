package com.ifsul.lawbot.dto.utils;

public record TokenRequest(
        String token,
        Long id
) {

    public TokenRequest(TokenRequest tokenJWT) {
        this(tokenJWT.token(), tokenJWT.id());
    }
}
