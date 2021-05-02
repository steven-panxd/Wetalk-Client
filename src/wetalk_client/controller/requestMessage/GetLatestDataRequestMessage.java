package wetalk_client.controller.requestMessage;

import wetalk_client.utils.Global;

public class GetLatestDataRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("getLatestDataPrefix");
}
