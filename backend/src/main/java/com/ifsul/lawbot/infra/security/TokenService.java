package com.ifsul.lawbot.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ifsul.lawbot.domain.advogado.Advogado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    public String gerarToken(Advogado advogado){
        try {
            System.out.println(secret);
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Lawbot")
                    .withSubject(advogado.getOab())
                    .withExpiresAt(dataExpiracap())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    private Instant dataExpiracap() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
