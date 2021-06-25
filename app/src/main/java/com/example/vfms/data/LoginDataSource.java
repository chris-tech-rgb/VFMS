package com.example.vfms.data;

import com.example.vfms.BackgroundWorker;
import com.example.vfms.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            String type = "login";
            BackgroundWorker backgroundWorker = new BackgroundWorker();
            String output;
            output = backgroundWorker.execute(type, username, password).get();
            if (output.equals("0") || output.equals("1")) {
                LoggedInUser user = new LoggedInUser(username, emailStrip(username));
                return new Result.Success<>(user);
            } else return new Result.Fail(Integer.parseInt(output));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    private String emailStrip(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}