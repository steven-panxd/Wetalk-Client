package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class DeleteFriendRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("deleteFriendPrefix");
    private final int deleteFriendID;


    public DeleteFriendRequestMessage(int deleteFriendID) {
        this.deleteFriendID = deleteFriendID;
    }

    public int getDeleteFriendID() {
        return deleteFriendID;
    }
}
