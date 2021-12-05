package com.example.vfms.data.rsa;

import android.content.Context;

import java.security.KeyPair;
import java.sql.*;
import java.util.ArrayList;

public class CoinSystem {

    static public Coin CreateCoin(String origin, KeyPair pair, String Pk, Context ct)throws Exception
    {
        String hash;
        String prehash =CreateOrigin()+"*"+"Origin";
        hash=RsaTools.stringToMD5(prehash);
        String s = RsaTools.RsaSign(hash,pair);
        Coin coin=new Coin(prehash,hash,s+"*"+Pk,ct);
        String url = "jdbc:postgresql://47.119.141.11:5432/info";
        String user = "user";
        String password = "user123456";
        Class.forName("org.postgresql.Driver");
        Connection conn =DriverManager.getConnection(url, user, password);
        String sql ="SELECT createcoin( ?,? )";
        PreparedStatement state = conn.prepareStatement(sql);
        state.setString(1,coin.NowValue);
        state.setString(2,"tester");
        ResultSet rs = state.executeQuery();
        System.out.println(rs.next());
        return coin;//
    }
    static String CreateOrigin()
    {
        return CreateOrigin(null);
    }
    static String CreateOrigin(String origin){
        if(origin!=null)
            return RsaTools.stringToMD5(origin);
        else
        {
            int i;
            i=(int)(10000*(Math.random()));
            return RsaTools.stringToMD5(String.valueOf(i));
        }
    }
    static boolean GiveCoin(Coin mycoin,KeyPair mypair,String hisPK){
        String hash;
        hash=RsaTools.stringToMD5(mycoin.NowValue+ hisPK);
        String s =RsaTools.RsaSign(hash,mypair);
        Coin coin =new Coin(mycoin.NowValue,s+"*"+hisPK);
        try {
            UpdateCoin(coin,hisPK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    static boolean UpdateCoin(Coin coin,String ownerName)throws Exception{
        String url = "jdbc:postgresql://47.119.141.11:5432/info";
        String user = "user";
        String password = "user123456";
        Class.forName("org.postgresql.Driver");
        Connection conn =DriverManager.getConnection(url, user, password);
        String sql ="SELECT updatecoin( ?,?,? )";
        PreparedStatement state = conn.prepareStatement(sql);
        state.setString(1, coin.OldValue);
        state.setString(2,coin.NowValue);
        state.setString(3,"tester");
        ResultSet rs = state.executeQuery();
        System.out.println(rs.next());
        return true;
    }
    static ArrayList SearchMyCoin(String ownerName)throws Exception{
        ArrayList CoinList = new ArrayList();
        String url = "jdbc:postgresql://47.119.141.11:5432/info";
        String user = "user";
        String password = "user123456";
        Class.forName("org.postgresql.Driver");
        Connection conn =DriverManager.getConnection(url, user, password);
        Statement state = conn.createStatement();
        String sql ="Select * from coin where ownername = '"+ ownerName +"'";
        ResultSet rs = state.executeQuery(sql);
        while(rs.next())
        {
            String nowValue = rs.getString("value");
            String oldValue = rs.getString("oldvalue_1");
            Coin c = new Coin(oldValue,nowValue);
            if(c.Useful||true)
            {
                CoinList.add(c);
            }
        }
        return CoinList;
    }


}
