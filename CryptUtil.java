/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.IDSafe.Util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Gaurav_Pawaskar
 */
public class CryptUtil {

    public static int generateSalt() {
        Random salt = new Random();
        int newSalt = salt.nextInt(Integer.MAX_VALUE);
        return newSalt;
    }

    public static StringBuffer getSHA256HashWithSalt(String value, int salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes());
        md.update(intToByteArray(salt));
        byte byteData[] = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString;
    }

    public static byte[] generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                    (byte) (value >>> 24),
                    (byte) (value >>> 16),
                    (byte) (value >>> 8),
                    (byte) value};
    }

    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }
    
    public static byte [] getAESCrypt(byte [] rawKey, String data) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return encrypted;
    }
    
    public static byte [] hexStringToByteArray(String s){
        int len = s.length();
        byte [] data = new byte[len/2];
        for (int i = 0; i < len; i+=2) {
            data[i/2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static String getAESDecrypt(byte [] rawKey, byte [] encrypted) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original);
        return originalString;
    }
}
