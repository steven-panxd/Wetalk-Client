package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class RegisterResponseMessage extends ResponseMessage{
    public static final String responseName = Global.getInstance().getProperty("registerPrefix");

    public RegisterResponseMessage(String status, String data) {
        super(status, data);
    }
}
