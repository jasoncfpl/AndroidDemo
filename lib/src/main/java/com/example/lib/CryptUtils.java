package com.example.lib;

/**
 * @author li jia
 * @date 2026/4/1 11:54
 * @description:
 */

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {

    /**
     * AES-CBC 加密（PKCS7 填充）
     * @param key 密钥（16/24/32 字节）
     * @param plainText 明文字节数组
     * @return Base64 字符串（IV + 密文）
     * @throws Exception
     */
    public static String encryptAESCBC(byte[] key, byte[] plainText) throws Exception {
        // 创建 AES 密码器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        // 生成随机 IV（与 Go 一致：16 字节）
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText);

        // 拼接 IV + 密文
        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        // Base64 编码返回
//        return android.util.Base64.encodeToString(result, android.util.Base64.DEFAULT);
        return java.util.Base64.getEncoder().encodeToString(result);
    }

    /**
     * AES-CBC 解密
     * @param key 密钥
     * @param cipherTextStr Base64 密文字符串
     * @return 明文字节数组
     * @throws Exception
     */
//    public static byte[] decryptAESCBC(byte[] key, String cipherTextStr) throws Exception {
////        byte[] cipherText = Base64.getDecoder().decode(cipherTextStr);
////        byte[] cipherText = android.util.Base64.decode(cipherTextStr, android.util.Base64.DEFAULT);
//        byte[] cipherText = java.util.Base64.getDecoder().decode(cipherTextStr);
//
//        // 拆分 IV（前 16 字节）
//        byte[] iv = new byte[16];
//        System.arraycopy(cipherText, 0, iv, 0, 16);
//
//        // 密文部分
//        byte[] encrypted = new byte[cipherText.length - 16];
//        System.arraycopy(cipherText, 16, encrypted, 0, encrypted.length);
//
//        // 初始化解密
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//
//        return cipher.doFinal(encrypted);
//    }

    public static byte[] decryptAESCBC(byte[] key, String cipherTextStr) throws Exception {
//        byte[] cipherText = Base64.getDecoder().decode(cipherTextStr);
//        byte[] cipherText = android.util.Base64.decode(cipherTextStr, android.util.Base64.DEFAULT);
        byte[] cipherText = java.util.Base64.getDecoder().decode(cipherTextStr);

        // 拆分 IV（前 16 字节）
        byte[] iv = new byte[16];
        System.arraycopy(cipherText, 0, iv, 0, 16);

        // 密文部分
        byte[] encrypted = new byte[cipherText.length - 16];
        System.arraycopy(cipherText, 16, encrypted, 0, encrypted.length);

        // 初始化解密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher.doFinal(encrypted);
    }

    public static byte[] decryptAppSign(String strKey, String strAppSign) throws Exception {
        byte[] keys = strKey.getBytes(StandardCharsets.UTF_8);
        byte[] signByte = decryptAESCBC(keys, strAppSign);
        String sign = new String(signByte, StandardCharsets.UTF_8);
        return CryptUtils.hexToByteArray(sign);
    }

    public static String decryptAppSign1(String encryptedBase64, String keyStr) throws Exception {
        byte[] key = keyStr.getBytes("UTF-8");
        byte[] total = Base64.getDecoder().decode(encryptedBase64);

        // 1. 前 16 位 = IV
        byte[] iv = new byte[16];
        System.arraycopy(total, 0, iv, 0, 16);

        // 2. 剩下的 = 真正密文
        byte[] cipherData = new byte[total.length - 16];
        System.arraycopy(total, 16, cipherData, 0, cipherData.length);

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 3. 关键：Android 原生写法
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedData = cipher.doFinal(cipherData);

        // 4. 安全去除 PKCS7 填充
        byte[] result = stripPadding(decryptedData);
        return new String(result, "UTF-8");
    }

    // 安全 PKCS7 填充移除（修复负数报错）
    private static byte[] stripPadding(byte[] data) {
        int pad = data[data.length - 1] & 0xFF;  // 这里修复负数问题！
        if (pad < 1 || pad > 16) pad = 0;
        int length = data.length - pad;
        byte[] result = new byte[length];
        System.arraycopy(data, 0, result, 0, length);
        return result;
    }

    /**
     * 手动移除 PKCS7Padding（Android 原生通用）
     */
    private static byte[] removePKCS7Padding(byte[] data) {
        int padLength = data[data.length - 1];
        byte[] result = new byte[data.length - padLength];
        System.arraycopy(data, 0, result, 0, result.length);
        return result;
    }
    public static String decryptAppSignString(String strKey, String strAppSign) throws Exception {
        byte[] keys = strKey.getBytes(StandardCharsets.UTF_8);
        byte[] signByte = decryptAESCBC(keys, strAppSign);
        return new String(signByte, StandardCharsets.UTF_8);
    }

    public static byte[] stringTo16byte(String str) {
        // 1. 字符串转 UTF-8 字节数组（也可替换为 GBK/ISO-8859-1）
        byte[] original = str.getBytes(StandardCharsets.UTF_8);
        // 2. 创建固定16位的目标数组
        byte[] result = new byte[16];

        // 3. 复制原字节数组到目标数组（自动截断/补0）
        System.arraycopy(
                original,   0,    // 原数组、起始位置
                result,      0,    // 目标数组、起始位置
                Math.min(original.length, 16) // 复制长度（取最小）
        );

        return result;
    }

    public static byte[] hexToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 16位 byte[] 转 十六进制字符串（32个字符）
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // 转十六进制，补0
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase(); // 大写小写都行
    }
    // ===================== 测试 =====================
    public static void main(String[] args) throws Exception {
        // 与你 Go 代码里的密钥完全一致
        String keyStr = "Dk3s9Fp7Rz2xQb5Nv8gA1cH6jL0pS5uG";
        byte[] key = keyStr.getBytes();

        // 测试明文
        String plainText = "qb71CTcCIZhnfDErJmjlPpViJxQE9GUH";

        // 加密
//        String encrypted = encryptAESCBC(key, plainText.getBytes());
//        String encrypted = "tTk0/hXSEaMqRwNeWd8RH2ar//0b1DCvVc5XmF1Aqjb7Vy0s19+u5RRqiAeCF3vvSUOdZQIZ5xGVWa5W60/eEQ==";
        String encrypted = "S8taqY9hR+htiRlAPoxZcHdnoyP6Ax0MeGBUUzms7n8=";
        System.out.println("加密结果: " + encrypted);

        // 解密
        byte[] decryptedBytes = decryptAESCBC(key, encrypted);
        String decrypted = new String(decryptedBytes);
        System.out.println("解密结果: " + decrypted);

//        String s = "Jl5yKUGpJeoQffomeZRMDhKffSem1iBvjckNpzH4nT1vgFGAlf69oKmgzd0YcVMMZ2KM/cQ+nUvgBlTqcrdcZA==";
//        String s = "f+FFSj/wGQZ4Q56xGNlWw+5/MBgY/wL1f3UqeAGDil0EKknigvJAJLq/niT2hMtEyEy9xT0dGWgRS/30mePbfA==";
//        String s1 = "f+FFSj/wGQZ4Q56xGNlWw+5/MBgY/wL1f3UqeAGDil0EKknigvJAJLq/niT2hMtEyEy9xT0dGWgRS/30mePbfA==";
        String s = encrypted;
        String s1 = encrypted;
        byte[] decryptedBytes1 = decryptAESCBC(key, s);

        String decrypted1 = bytesToHex(decryptedBytes1);
        String decrypted2 = new String(decryptedBytes1, StandardCharsets.UTF_8);
        String decrypted4 = decryptAppSign1(s1, keyStr);

        System.out.println("解密结果1: " + decrypted1);
        System.out.println("解密结果2: " + decrypted2);
        System.out.println("解密结果4: " + decrypted4);

        String decrypted5 = AesCbcUtil.decryptAESCBC(key, s);
        System.out.println("解密结果5: " + decrypted5);

    }
}
