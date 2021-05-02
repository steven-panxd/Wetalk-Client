package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class RegisterRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("registerPrefix");
    private final String username;
    private final String password;

    public RegisterRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
