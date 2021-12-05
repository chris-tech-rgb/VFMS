package com.example.vfms.data.rsa;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.vfms.R;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
public class RsaTools {
    private static String src = "cakin security rsa";
    public static void main(String[] args) {
        jdkRSA();
    }
    public static void RsaInit(Context ct) throws Exception{
        KeyPair kp = GetKeyPair();
        PublicKey pk=kp.getPublic();
        PrivateKey dk=kp.getPrivate();
        String pks=getKeyAsString(pk);
        String dks=getKeyAsString(dk);
        SharedPreferences sp =ct.getSharedPreferences("keypair",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("privateKey",dks);
        editor.putString("publicKey",pks);
        editor.commit();

        /*
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("client1PubKey1.key"));
        osw.write(pks,0,pks.length());
        osw.flush();
        osw.close();

        OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream("client1PriKey1.key"));
        osw2.write(dks,0,dks.length());
        osw2.flush();
        osw2.close();
        */
    }
    public static void jdkRSA() {
        try {
            //1.初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
            //2.执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] result = signature.sign();
            System.out.println("jdk rsa sign : " + bytesToHex(result));
            //3.验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean bool = signature.verify(result);
            System.out.println("jdk rsa verify : " + bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static KeyPair GetMyKeyPair(Context ct) throws Exception{
        /*
        InputStreamReader isr = new InputStreamReader(new FileInputStream("client1PubKey1.key"));
        String client1PubKey="";
        int ch=0;
        while((ch=isr.read())!=-1){
            client1PubKey=client1PubKey+(char)ch;
        }
        isr.close();

        InputStreamReader isr2 = new InputStreamReader(new FileInputStream("client1PriKey1.key"));
        String client1PriKey="";
        int ch2=0;
        while((ch2=isr2.read())!=-1){
            client1PriKey=client1PriKey+(char)ch2;
        }
        isr2.close();
        */



        SharedPreferences sp =ct.getSharedPreferences("keypair",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String client1PubKey = sp.getString("publicKey",null);
        String client1PriKey = sp.getString("privateKey",null);

        KeyFactory fact = KeyFactory.getInstance("RSA");
        KeyPair kp=new KeyPair(ToPubKey(client1PubKey),ToPriKey(client1PriKey));
        System.out.println("client1PriKey="+client1PriKey);
        System.out.println("client1PubKey="+client1PubKey);
        return  kp;
    }
    public static PublicKey ToPubKey(String KeyValue)throws GeneralSecurityException, IOException {
        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec=new X509EncodedKeySpec(Base64.decode(KeyValue.getBytes(), Base64.DEFAULT));
        PublicKey publicKey=keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }
    public static PrivateKey ToPriKey(String KeyValue)throws GeneralSecurityException, IOException {

        KeyFactory keyFactory=KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec=new PKCS8EncodedKeySpec(Base64.decode(KeyValue.getBytes(), Base64.DEFAULT));
        PrivateKey privateKey=keyFactory.generatePrivate(privateKeySpec);
        return privateKey;
    }
    public static KeyPair GetKeyPair(){
        KeyPair keyPair=null;
        try {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        keyPair = keyPairGenerator.generateKeyPair();
        //RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
        //RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }
    public static String RsaSign(String s,KeyPair kp){
        PrivateKey sk = (RSAPrivateKey)kp.getPrivate();
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(sk.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(s.getBytes());
            byte[] result = signature.sign();
            System.out.println("jdk rsa sign : " + bytesToHex(result));
            return bytesToHex(result);
        }
        catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }
    public static boolean VerifySignature(String srcData,RSAPublicKey pk,String sign){
        try {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pk.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(srcData.getBytes());
        boolean bool = signature.verify(hexToByteArray(sign));
        System.out.println("jdk rsa verify : " + bool);
        return bool;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 字节转十六进制
     * @param b 需要进行转换的byte字节
     * @return  转换后的Hex字符串
     */
    public static String byteToHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    public static String getKeyAsString(Key key)
    {
        byte[] keyBytes = key.getEncoded();
        return Base64.encodeToString(keyBytes, Base64.DEFAULT);
    }

}