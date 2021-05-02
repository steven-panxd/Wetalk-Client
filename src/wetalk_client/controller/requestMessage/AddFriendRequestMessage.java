package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class AddFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("addFriendPrefix");
    private final String friendUsername;

    public AddFriendRequestMessage(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }
}
