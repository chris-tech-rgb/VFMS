package com.example.vfms.data.rsa;

import android.content.Context;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class Coin{
    Coin(String OldValue,String NowValue)
    {
        this.NowValue=NowValue;
        this.OldValue=OldValue;
        old_pk=GetPK(OldValue);
        now_pk=GetPK(NowValue);
        old_sign=GetSignature(OldValue);
        now_sign=GetSignature(NowValue);
        /*Useful=CheckItself();
        if(!Useful)
        {
            System.out.println("WRONG:this is a wrong coin!!");
        }*/
    }
    Coin(String Prehash,String hash,String OriginalValue,Context ct)
    {
        this.NowValue=OriginalValue;
        this.OldValue=null;
        old_pk=null;
        now_pk=GetPK(OriginalValue);
        old_sign=null;
        now_sign=GetSignature(OriginalValue);
        Useful=CheckOrigin(Prehash,hash,now_sign,ct);
        if(!Useful)
        {
            System.out.println("WRONG:this is a wrong coin!!");
        }
    }
    Coin(Coin oldcoin,String NowValue)
    {
        this.NowValue=NowValue;
        this.OldValue=oldcoin.NowValue;
        old_pk=GetPK(OldValue);
        now_pk=GetPK(NowValue);
        old_sign=GetSignature(OldValue);
        now_sign=GetSignature(NowValue);
        Useful=CheckItself();
        if(!Useful)
        {
            System.out.println("WRONG:this is a wrong coin!!");
        }
    }
    public String NowValue;
    public String OldValue;
    public boolean Useful =false;

    private String now_pk;
    private String old_pk;
    private String now_sign;
    private String old_sign;

    private boolean CheckItself(){
        return (check_signature(this.now_sign,this.old_pk));
    }
    public String ValueForGive(String Value,String PKey) {
        String NValue=null;

        if(NValue!=null)
            return NValue;
        else return null;
    }
    private boolean CheckOrigin(String Prehash, String hash, String sign, Context ct){
        KeyPair kp = null;
        try {
            kp=RsaTools.GetMyKeyPair(ct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(RsaTools.VerifySignature(hash, (RSAPublicKey) kp.getPublic(),sign))
            return true;
        else
            return false;
    }
    private String ValueForGet(String Value,String PKey) {
        return null;
    }
    private String GetPK(String CoinValue){
        return CoinValue.split("\\*")[1];
    }
    private String GetSignature(String CoinValue){
        return CoinValue.split("\\*")[0];
    }
    private boolean check_signature(String signature,String Pk){
        PublicKey publicKey=null;
        try {
            publicKey =RsaTools.ToPubKey(Pk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RsaTools.VerifySignature(RsaTools.stringToMD5(OldValue+Pk), (RSAPublicKey)publicKey ,signature);
    }
    public boolean UpdateToDataBase()
    {
        return false;
    }

}