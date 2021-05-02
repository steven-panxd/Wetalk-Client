package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class SendMessageResponseMessage extends ResponseMessage{
    public final static String responseName = Global.getInstance().getProperty("sendMessagePrefix");

    public SendMessageResponseMessage(String status, String data) {
        super(status, data);
    }
}
