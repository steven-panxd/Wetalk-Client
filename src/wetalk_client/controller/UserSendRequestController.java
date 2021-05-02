package wetalk_client.controller;

import wetalk_client.controller.requestMessage.*;
import wetalk_client.model.MessageModel;
import wetalk_client.utils.Global;
import wetalk_client.view.ChatView;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class UserSendRequestController {

    static HashMap<String, String> login(LoginRequestMessage message) {
        String username = message.getUsername();
        String password = message.getPassword();
        boolean savePassword = message.isSavePassword();

        // temporarily store for response handler use
        Global.getInstance().setProperty("username", username);
        Global.getInstance().setProperty("password", password);
        Global.getInstance().put("savePassword", savePassword);

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("username", username);
        mapData.put("password", password);
        return mapData;
    }

    static HashMap<String, String> register(RegisterRequestMessage message) {
        String username = message.getUsername();
        String password = message.getPassword();

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("username", username);
        mapData.put("password", password);
        return mapData;
    }

    static HashMap<String, String> getFriendList(GetFriendListRequestMessage message) {
        // no data need to send
        return null;
    }

    static HashMap<String, String> sendMessage(SendMessageRequestMessage message) {
        MessageModel messageModel = message.getMessageModel();

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("receiverID", String.valueOf(messageModel.getReceiverID()));
        mapData.put("content", messageModel.getContent());
        mapData.put("sendTime", String.valueOf(messageModel.getSendTimeStamp()));

        // temporarily store current message model to Global -> queue
        LinkedBlockingQueue<MessageModel> queue =  (LinkedBlockingQueue<MessageModel>) Global.getInstance().get("sentMessageModelQueue");
        queue.add(messageModel);

        return mapData;
    }

    public static HashMap<String, String> getLatestData(ChatView view, GetLatestDataRequestMessage message) {
        // no data need to send
        return null;
    }

    public static HashMap<String, String> addFriend(AddFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("friendUsername", message.getFriendUsername());
        return mapData;
    }

    public static HashMap<String, String> acceptFriend(AcceptFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("acceptedFriendID", String.valueOf(message.getAcceptFriendID()));
        return mapData;
    }

    public static HashMap<String, String> rejectFriend(RejectFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("rejectedFriendID", String.valueOf(message.getRejectFriendID()));
        return mapData;
    }
}
