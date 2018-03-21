package POJO;

/**
 * Created by Yev on 2018-03-20.
 */

public class LoginHandler {
    private String username;
    private String password;

    public LoginHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
