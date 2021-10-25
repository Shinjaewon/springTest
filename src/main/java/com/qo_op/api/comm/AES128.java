package com.qo_op.api.comm;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class AES128 {


    public byte[] encryptToBytes(String key, String strToEncrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

    }

    public String encrypt(String key, String strToEncrypt) throws Exception {
        String encryptedStr = Base64.getEncoder().encodeToString(encryptToBytes(key, strToEncrypt));
        return encryptedStr;
    }

    public byte[] decryptToBytes(String key, byte[] bytesToDecrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(bytesToDecrypt);
    }
}
