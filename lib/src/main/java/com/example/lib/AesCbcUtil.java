package com.example.lib;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AesCbcUtil {

    // 修复：把 PKCS7Padding 改成 PKCS5Padding（AES下完全兼容）
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    public static String encryptAESCBC(byte[] key, String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // 生成随机IV (16字节)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

        // IV + 密文 → Base64
        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(result);
    }

    public static String decryptAESCBC(byte[] key, String cipherTextBase64) throws Exception {
        byte[] cipherText = Base64.getDecoder().decode(cipherTextBase64);

        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // 拆分IV
        byte[] iv = new byte[16];
        System.arraycopy(cipherText, 0, iv, 0, 16);
        byte[] realCipherText = new byte[cipherText.length - 16];
        System.arraycopy(cipherText, 16, realCipherText, 0, realCipherText.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decrypted = cipher.doFinal(realCipherText);
        return new String(decrypted, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        // 密钥和Go代码完全一样（32字节 = AES-256）
        byte[] key = "Dk3s9Fp7Rz2xQb5Nv8gA1cH6jL0pS5uG".getBytes("UTF-8");
        String plainText = "测试 CBC 模式";

        // 加密
        // String enc = encryptAESCBC(key, plainText);
        // System.out.println("CBC 加密：" + enc);

        // 解密（你Go代码里的密文）
        String dec = decryptAESCBC(key, "XYfXfHjf/nmf51eHPmORP+G0ZoLlnbtw/ooMFEJRYzK07MHiUzNmQFputx98VOuj");
        System.out.println("CBC 解密：" + dec);
    }
}