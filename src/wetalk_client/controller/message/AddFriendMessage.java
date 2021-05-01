package wetalk_client.controller.message;

public class AddFriendMessage implements Message {
    private final String friendUsername;

    public AddFriendMessage(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendID() {
        return friendUsername;
    }
}
