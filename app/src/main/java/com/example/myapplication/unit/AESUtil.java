package com.example.myapplication.unit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;


public class AESUtil {

    public static String AESKey = "haoj";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String plainText, String secretKey) {
        try {
            Key key = generateKey(secretKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting the text", e);
        }
    }

    public static String decrypt(String encryptedText, String secretKey) {
        try {
            Key key = generateKey(secretKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting the text", e);
        }
    }

    private static Key generateKey(String secretKey) {
        return new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }
    public static String padKey(String key) {
        int length = key.length();
        if (length < 16) {
            while (key.length() < 16) {
                key += "-";
            }
            return key.substring(0, 16);
        } else if (length < 24) {
            while (key.length() < 24) {
                key += "-";
            }
            return key.substring(0, 24);
        } else if (length < 32) {
            while (key.length() < 32) {
                key += "-";
            }
        }
        return key.substring(0, 32); // 取前32个字符
    }

}
