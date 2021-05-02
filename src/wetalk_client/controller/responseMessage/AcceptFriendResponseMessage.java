package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class AcceptFriendResponseMessage extends ResponseMessage{
    public static final String responseName = Global.getInstance().getProperty("acceptFriendPrefix");

    public AcceptFriendResponseMessage(String status, String data) {
        super(status, data);
    }
}
