package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class DeleteFriendResponseMessage extends ResponseMessage{
    public static final String responseName = Global.getInstance().getProperty("deleteFriendPrefix");

    public DeleteFriendResponseMessage(String status, String data) {
        super(status, data);
    }
}
