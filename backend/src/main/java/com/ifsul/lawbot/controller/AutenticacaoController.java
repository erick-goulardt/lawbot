package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.domain.advogado.Advogado;
import com.ifsul.lawbot.domain.advogado.dto.AutenticarRequest;
import com.ifsul.lawbot.infra.security.DadosTokenJWT;
import com.ifsul.lawbot.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ifsul.lawbot.infra.security.HashSenhas;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private HashSenhas hashSenhas;

    @Autowired
    private TokenService tokenService;
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid AutenticarRequest dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Advogado) authentication.getPrincipal());
        System.out.println("login: " + dados.login() + "\n senha: " + dados.senha() + "\n token: " + tokenJWT);
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
