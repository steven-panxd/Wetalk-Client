package wetalk_client.controller.message;

public class AcceptFriendMessage implements Message {
    private final int acceptFriendID;

    public AcceptFriendMessage(int acceptFriendID) {
        this.acceptFriendID = acceptFriendID;
    }

    public int getAcceptFriendID() {
        return acceptFriendID;
    }
}
