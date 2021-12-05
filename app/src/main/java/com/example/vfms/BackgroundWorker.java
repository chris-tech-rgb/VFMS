package com.example.vfms;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings({"unchecked", "deprecation"})
public class BackgroundWorker extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        switch (type) {
            case "login":
                try {
                    JSONObject json = new JSONObject();
                    JSONObject content = new JSONObject();
                    content.put("username", params[1]);
                    content.put("password", params[2]);
                    json.put("function", "login");
                    json.put("timestamp", timestamp());
                    json.put("content", content);
                    return message(json);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "list":
                try {
                    JSONObject json = new JSONObject();
                    json.put("function", "list");
                    json.put("timestamp", timestamp());
                    json.put("content", null);
                    return message(json);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "chart":
                try {
                    JSONObject json = new JSONObject();
                    JSONObject content = new JSONObject();
                    content.put("id", params[1]);
                    content.put("date", params[2]);
                    json.put("function", "chart");
                    json.put("timestamp", timestamp());
                    json.put("content", content);
                    return message(json);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "register":
                try {
                    JSONObject json = new JSONObject();
                    JSONObject content = new JSONObject();
                    content.put("username", params[1]);
                    content.put("pubkey",params[2]);
                    json.put("function", "register");
                    json.put("timestamp", timestamp());
                    json.put("content", content);

                    return message(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "getcoin":
                try {
                    JSONObject json = new JSONObject();
                    JSONObject content = new JSONObject();
                    content.put("username", params[1]);
                    content.put("pubkey",params[2]);
                    content.put("loclat",params[3]);
                    content.put("loclng",params[4]);
                    //Log.d("locDebug", "loclat: "+params[3]+"loclng"+params[4]);
                    json.put("function", "getcoin");
                    json.put("timestamp", timestamp());
                    json.put("content", content);
                    return message(json);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    static String timestamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    static String message(JSONObject json) throws IOException, ParseException {
        Socket socket = new Socket("47.119.141.11", 10000);
        OutputStream outputStream = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(json.toString());
        writer.flush();
        socket.shutdownOutput();
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String rep = bufferedReader.readLine();
        Log.d("reg", "message: "+rep);
        JSONParser jsonParser = new JSONParser();
        JSONObject repJSON = (JSONObject) jsonParser.parse(rep);
        String function = (String) repJSON.get("function");
        assert function != null;
        if (function.equals("rep")) {
            String res = (String) repJSON.get("content");
            //Log.d("tag", res);
            socket.close();
            return res;
        } else {
            socket.close();
            return null;
        }
    }
}

