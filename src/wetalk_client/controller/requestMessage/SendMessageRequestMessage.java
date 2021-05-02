package wetalk_client.controller.requestMessage;

import wetalk_client.model.MessageModel;
import wetalk_client.utils.Global;

public class SendMessageRequestMessage extends RequestMessage {
    public static final String requestName = Global.getInstance().getProperty("sendMessagePrefix");
    private final MessageModel messageModel;

    public SendMessageRequestMessage(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }
}
