package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class RejectFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("rejectFriendPrefix");
    private final int rejectedFriendID;

    public RejectFriendRequestMessage(int rejectedFriendID) {
        this.rejectedFriendID = rejectedFriendID;
    }

    public int getRejectedFriendID() {
        return rejectedFriendID;
    }
}
