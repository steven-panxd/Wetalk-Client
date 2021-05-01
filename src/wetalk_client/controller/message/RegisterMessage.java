package wetalk_client.controller.message;

public class RegisterMessage implements Message {
    private final String username;
    private final String password;
    private final String rePassword;

    public RegisterMessage(String username, String password, String rePassword) {
        this.username = username;
        this.password = password;
        this.rePassword = rePassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
