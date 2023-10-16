package com.ifsul.lawbot.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class HashSenhas {

    private static int workload = 12;

    public static String hash(String senha){
        String salt = BCrypt.gensalt(workload);
        String hashedSenha = BCrypt.hashpw(senha, salt);
        return hashedSenha;
    }
}
