package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class GetFriendListResponseMessage extends ResponseMessage{
    public final static String responseName = Global.getInstance().getProperty("getFriendListPrefix");

    public GetFriendListResponseMessage(String status, String data) {
        super(status, data);
    }
}
