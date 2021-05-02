package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class AcceptFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("acceptFriendPrefix");
    private final int acceptFriendID;

    public AcceptFriendRequestMessage(int acceptFriendID) {
        this.acceptFriendID = acceptFriendID;
    }

    public int getAcceptFriendID() {
        return acceptFriendID;
    }
}
