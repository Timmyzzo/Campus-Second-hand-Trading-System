package com.example.campustrade.util;

public class EncryptionUtil {

    private static final int SHIFT = 3; // 移动3位

    // 把字符串的每个字符的ASCII码加3
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        StringBuilder encryptedText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            encryptedText.append((char) (c + SHIFT));
        }
        return encryptedText.toString();
    }

    // 无用解密
    public static String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }
        StringBuilder plainText = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            plainText.append((char) (c - SHIFT));
        }
        return plainText.toString();
    }
}