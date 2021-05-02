package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class AddFriendResponseMessage extends ResponseMessage{
    public final static String responseName = Global.getInstance().getProperty("addFriendPrefix");

    public AddFriendResponseMessage(String status, String data) {
        super(status, data);
    }
}
