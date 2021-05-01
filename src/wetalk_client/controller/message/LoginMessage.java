package wetalk_client.controller.message;

public class LoginMessage implements Message {
    private final String username;
    private final String password;
    private final boolean savePassword;

    public LoginMessage(String username, String password, boolean savePassword) {
        this.username = username;
        this.password = password;
        this.savePassword = savePassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSavePassword() {
        return savePassword;
    }
}
