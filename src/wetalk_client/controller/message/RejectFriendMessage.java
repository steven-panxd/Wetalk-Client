package wetalk_client.controller.message;

public class RejectFriendMessage implements Message {
    private final int rejectFriendID;

    public RejectFriendMessage(int rejectFriendID) {
        this.rejectFriendID = rejectFriendID;
    }

    public int getRejectFriendID() {
        return rejectFriendID;
    }
}
