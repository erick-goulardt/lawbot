package com.ifsul.lawbot.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class HashSenhasService {

    private static int workload = 12;

    public static String hash(String senha){
        String salt = BCrypt.gensalt(workload);
        String hashedSenha = BCrypt.hashpw(senha, salt);
        return hashedSenha;
    }

    public static boolean verificaCliente(String senhaFornecida, String senhaHashArmazenada){
        return BCrypt.checkpw(senhaFornecida, senhaHashArmazenada);
    }
}
