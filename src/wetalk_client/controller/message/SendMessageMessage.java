package wetalk_client.controller.message;

import wetalk_client.model.MessageModel;

public class SendMessageMessage implements Message {
    private final MessageModel messageModel;

    public SendMessageMessage(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }
}
