package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class RejectFriendResponseMessage extends ResponseMessage {
    public static final String responseName  = Global.getInstance().getProperty("rejectFriendPrefix");

    public RejectFriendResponseMessage(String status, String data) {
        super(status, data);
    }
}
