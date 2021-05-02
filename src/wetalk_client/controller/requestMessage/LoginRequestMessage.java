package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class LoginRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("loginPrefix");
    private final String username;
    private final String password;
    private final boolean savePassword;

    public LoginRequestMessage(String username, String password, boolean savePassword) {
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
