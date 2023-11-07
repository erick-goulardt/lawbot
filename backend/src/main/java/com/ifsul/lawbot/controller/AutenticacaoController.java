package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.auth.AutenticarRequest;
import com.ifsul.lawbot.dto.utils.DadosTokenJWT;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid AutenticarRequest dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        String tokenJWT = "";
        try{
            tokenJWT = tokenService.gerarToken((Advogado) authentication.getPrincipal());
        } catch (Exception ex){
            try{
                tokenJWT = tokenService.gerarToken((Cliente) authentication.getPrincipal());
            } catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
