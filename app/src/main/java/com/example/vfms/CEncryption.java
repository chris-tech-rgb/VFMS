package com.example.vfms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CEncryption {
    public String md5(String string, int time) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            if (time <= 1) {
                return hexString.toString();
            } else return md5(hexString.toString(), time - 1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String interleaving(String string1, String string2) {
        int length1 = string1.length(), length2 = string2.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            if (i % 2 == 0) {
                stringBuilder.append(string1.charAt(i % length1));
            } else {
                stringBuilder.append((string2.charAt(i % length2)));
            }
        }
        return stringBuilder.toString();
    }

    public String cEnc(String string1, String string2) {
        return interleaving(md5(interleaving(md5(interleaving(string1, string2), 4), md5(interleaving(string2, md5(string1, 1)), 2)), 1),
                interleaving(md5(interleaving(md5(string2, 1), md5(string1, 2)), 2), md5(interleaving(string1, interleaving(string1, string2)), 3)));
    }
}
