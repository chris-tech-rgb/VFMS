package com.example.vfms.data;

import android.content.Context;

import com.example.vfms.BackgroundWorker;
import com.example.vfms.data.model.LoggedInUser;
import com.example.vfms.data.rsa.RsaTools;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Result<LoggedInUser> login(String username, String password, Context context) {

        try {
            // TODO: handle loggedInUser authentication
            String type = "login";
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            String output = backgroundWorker.execute(type, username, password).get();
            if (output.equals("0") || output.equals("1")) {
                if (output.equals("0")) {
                    try {
                        RsaTools.RsaInit((context));
                        //Log.d("abc", RsaTools.getKeyAsString(RsaTools.GetMyKeyPair(context).getPublic()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BackgroundWorker bw = new BackgroundWorker();
                    //Log.d("beforeMessage", "ok");
                    bw.execute("register", username, RsaTools.getKeyAsString(RsaTools.GetMyKeyPair(context).getPublic())).get();
                }
                LoggedInUser user = new LoggedInUser(username, emailStrip(username));
                return new Result.Success<>(user);
            } else return new Result.Fail(Integer.parseInt(output));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private String emailStrip(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}