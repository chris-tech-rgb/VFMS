package com.example.vfms;

import android.os.AsyncTask;

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

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        if (type.equals("login")) {
            try {
                Socket socket = new Socket("47.119.141.11", 10000);
                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                String username = params[1];
                String password = params[2];
                JSONObject loginJSON = new JSONObject();
                JSONObject content = new JSONObject();
                content.put("username", username);
                content.put("password", password);
                loginJSON.put("function", "login");
                loginJSON.put("timestamp", java.util.Calendar.getInstance().getTime().toString());
                loginJSON.put("content", content);
                writer.write(loginJSON.toString());
                writer.flush();
                socket.shutdownOutput();
                InputStream inputStream= socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String rep = bufferedReader.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject repJSON = (JSONObject) jsonParser.parse(rep);
                String function = (String) repJSON.get("function");
                if (function.equals("login-rep")) {
                    int res = (int) repJSON.get("content");
                    socket.close();
                    return Integer.toString(res);
                } else socket.close();
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
}

