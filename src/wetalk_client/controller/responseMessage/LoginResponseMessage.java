package wetalk_client.controller.responseMessage;

import wetalk_client.utils.Global;

public class LoginResponseMessage extends ResponseMessage {
    public final static String responseName = Global.getInstance().getProperty("loginPrefix");

    public LoginResponseMessage(String status, String data) {
        super(status, data);
    }
}
