package wetalk_client.controller;

import wetalk_client.controller.requestMessage.*;
import wetalk_client.model.MessageModel;
import wetalk_client.utils.Global;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Handles messages from user
 * Wraps it to a HashMap data and return it back to MainController for encoding
 */
public class UserSendRequestController {

    /**
     * wraps login request data method
     * @param message a LoginRequestMessage from view
     * @return a HashMap that contains username and password
     */
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

    /**
     * wraps register request data method
     * @param message a RegisterRequestMessage from view
     * @return a HashMap that contains username and password
     */
    static HashMap<String, String> register(RegisterRequestMessage message) {
        String username = message.getUsername();
        String password = message.getPassword();

        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("username", username);
        mapData.put("password", password);
        return mapData;
    }

    /**
     * wraps getFriendList request data method
     * @param message a GetFriendListRequestMessage from view
     * @return null since no extra data need for this request
     */
    static HashMap<String, String> getFriendList(GetFriendListRequestMessage message) {
        // no data need to send
        return null;
    }

    /**
     * wraps sendMessage request data method
     * @param message a SendMessageRequestMessage from view
     * @return a HashMap that contains receiverID, content and sendTimeStamp
     */
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

    /**
     * wraps getLatestData request data method
     * @param message a GetLatestDataRequestMessage from view
     * @return null since no extra data need for this request
     */
    public static HashMap<String, String> getLatestData(GetLatestDataRequestMessage message) {
        // no data need to send
        return null;
    }

    /**
     * wraps addFriend request data method
     * @param message a AddFriendRequestMessage from view
     * @return a HashMap that contains friendUsername
     */
    public static HashMap<String, String> addFriend(AddFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("friendUsername", message.getFriendUsername());
        return mapData;
    }

    /**
     * wraps acceptFriend request data method
     * @param message a AcceptFriendRequestMessage from view
     * @return a HashMap that contains acceptedFriendID
     */
    public static HashMap<String, String> acceptFriend(AcceptFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("acceptedFriendID", String.valueOf(message.getAcceptedFriendID()));
        return mapData;
    }

    /**
     * wraps rejectFriend request data method
     * @param message a RejectFriendRequestMessage from view
     * @return a HashMap that contains rejectedFriendID
     */
    public static HashMap<String, String> rejectFriend(RejectFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("rejectedFriendID", String.valueOf(message.getRejectedFriendID()));
        return mapData;
    }

    /**
     * wraps deleteFriend request data method
     * @param message a DeleteFriendRequestMessage from view
     * @return a HashMap that contains deletedFriendID
     */
    public static HashMap<String, String> deleteFriend(DeleteFriendRequestMessage message) {
        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("deletedFriendID", String.valueOf(message.getDeleteFriendID()));
        return mapData;
    }
}
