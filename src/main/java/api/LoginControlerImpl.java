package api;

import com.google.common.hash.Hashing;
import crypto.HardwareId;
import json.LoginData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class LoginControlerImpl implements LoginController {
    private LoginData loginData;


    private LoginData getLoginData() throws IOException {
        if (loginData == null) {
            loginData = LoginData.load();
        }

        return loginData;
    }

    @Override
    public boolean checkHardwareId() throws IOException, NoSuchAlgorithmException {
        return HardwareId.check(getLoginData().getHardwareHash());
    }

    @Override
    public boolean checkCredentials(String login, String password) throws IOException {
        LoginData d = getLoginData();
        return login.equals(d.getLogin()) && Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString().equals(d.getPassword());
    }
}
