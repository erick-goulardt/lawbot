package com.ifsul.lawbot.domain;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/senha")
public class CifradorDeSenhaBcript {

    @GetMapping()
    public void cifrarSenha() {

        String senha = "123";

        // Gera um sal aleatório
        String salGerado = BCrypt.gensalt();
        System.out.println("O sal gerado foi $" + salGerado + "$");

        // Gera a senha hasheada utilizando o sal gerado
        String senhaHasheada = BCrypt.hashpw(senha, salGerado);

        System.out.println(senhaHasheada);
    }

    public boolean validarSenhaCrifrada(String senhaCifrada, String senha) {
        return BCrypt.checkpw(senhaCifrada, senha);
    }

}
