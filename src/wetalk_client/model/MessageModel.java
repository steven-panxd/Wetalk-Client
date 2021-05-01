package wetalk_client.model;

import java.util.Date;

public class MessageModel {
    private final int senderID;
    private final int receiverID;
    private final String content;
    private final Long sendTimeStamp;

    private MessageModel(int id, int senderID, int receiverID, String content, Long sendTimeStamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sendTimeStamp = sendTimeStamp;
    }

    public MessageModel(int senderID, int receiverID, String content, Long sendTimeStamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sendTimeStamp = sendTimeStamp;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public String getContent() {
        return content;
    }

    public Long getSendTimeStamp() {
        return sendTimeStamp;
    }
}
