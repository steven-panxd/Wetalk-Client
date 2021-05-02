package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class RejectFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("rejectFriendPrefix");
    private final int rejectFriendID;

    public RejectFriendRequestMessage(int rejectFriendID) {
        this.rejectFriendID = rejectFriendID;
    }

    public int getRejectFriendID() {
        return rejectFriendID;
    }
}
