package com.example.vfms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public String md5(String s, int time) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            if (time <= 1){
                return hexString.toString();
            } else return md5(hexString.toString(), time - 1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
