package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class AcceptFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("acceptFriendPrefix");
    private final int acceptedFriendID;

    public AcceptFriendRequestMessage(int acceptedFriendID) {
        this.acceptedFriendID = acceptedFriendID;
    }

    public int getAcceptedFriendID() {
        return acceptedFriendID;
    }
}
