package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.repositories.ChaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Optional;

import static com.ifsul.lawbot.services.CriptografiaService.ALGORITHM;
import static com.ifsul.lawbot.services.CriptografiaService.KEY_SIZE;

@Service
public class GerarChaveService {

    @Autowired
    private ChaveRepository chaveRepository;

    public Chave findKey() {
        Optional<Chave> findKey = chaveRepository.findAll().stream().findFirst();
        Chave key = null;

        if (findKey.isEmpty()) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
                keyPairGenerator.initialize(KEY_SIZE);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();
                key = Chave.builder().chavePrivada(privateKey).chavePublica(publicKey).build();
                chaveRepository.save(key);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            key = findKey.get();
        }
        return key;
    }
}
