package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class GetLatestDataResponseMessage extends ResponseMessage{
    public final static String responseName = Global.getInstance().getProperty("getLatestDataPrefix");

    public GetLatestDataResponseMessage(String status, String data) {
        super(status, data);
    }
}
