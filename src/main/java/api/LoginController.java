package api;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface LoginController {
    public boolean checkHardwareId() throws IOException, NoSuchAlgorithmException;
    public boolean checkCredentials(String login,String password) throws IOException;
}
