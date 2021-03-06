package com.example.vfms.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.example.vfms.CEncryption;
import com.example.vfms.data.LoginRepository;
import com.example.vfms.data.Result;
import com.example.vfms.data.model.LoggedInUser;
import com.example.vfms.R;

public class LoginViewModel extends ViewModel {

    @SuppressWarnings("FieldMayBeFinal")
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    @SuppressWarnings("FieldMayBeFinal")
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    @SuppressWarnings("FieldMayBeFinal")
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    @SuppressWarnings("unused")
    MutableLiveData<LoginFormState> getLoginFormStateStatic() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Context context) {
        // can be launched in a separate asynchronous job
        CEncryption cEncryption = new CEncryption();
        String ciphertextPassword = cEncryption.cEnc(username, password);
        Result<LoggedInUser> result = loginRepository.login(username, ciphertextPassword,context);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else if (result instanceof Result.Fail) {
            loginResult.setValue(new LoginResult(((Result.Fail) result).getFail()));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    public boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
    }

    // A placeholder password validation check
    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}