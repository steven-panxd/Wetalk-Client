package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class GetFriendListRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("getFriendListPrefix");
}
