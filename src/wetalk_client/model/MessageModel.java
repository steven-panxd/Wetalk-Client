package wetalk_client.model;

/**
 * Data model for every single message sent or received by user
 */
public class MessageModel {
    private final int senderID;
    private final int receiverID;
    private final String content;
    private final Long sendTimeStamp;

    /**
     * Constructor of MessageModel
     * @param senderID sender's user id of this message
     * @param receiverID receiver's user id of this message
     * @param content string content of this message
     * @param sendTimeStamp send time stamp of this message
     */
    public MessageModel(int senderID, int receiverID, String content, Long sendTimeStamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sendTimeStamp = sendTimeStamp;
    }

    /**
     * returns sender's user id of this message
     * @return sender's user id
     */
    public int getSenderID() {
        return senderID;
    }

    /**
     * returns receiver's user id of this message
     * @return receiver's user id
     */
    public int getReceiverID() {
        return receiverID;
    }

    /**
     * returns string content of this message
     * @return string content of this message
     */
    public String getContent() {
        return content;
    }

    /**
     * returns send time stamp of this message
     * @return send time stamp of this message
     */
    public Long getSendTimeStamp() {
        return sendTimeStamp;
    }
}
