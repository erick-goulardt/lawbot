package com.ifsul.lawbot.services;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class CriptografiaService {

    public static String encriptar(String plaintext) {
        try {
            Base64.Encoder enc = Base64.getEncoder();
            byte[] encryptedBytes = enc.encode(plaintext.getBytes());
            return new String(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decriptar(String ciphertext) {
        try {
            Base64.Decoder dec = Base64.getDecoder();
            byte[] decryptedBytes = dec.decode(ciphertext);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
