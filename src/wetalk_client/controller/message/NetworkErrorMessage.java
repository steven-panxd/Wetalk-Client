package wetalk_client.controller.message;

public class NetworkErrorMessage implements Message {
    private final String message;

    public NetworkErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
