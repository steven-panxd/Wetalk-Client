package wetalk_client.controller;

import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.responseMessage.*;
import wetalk_client.model.MessageModel;
import wetalk_client.model.UserModel;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.LocalStorage;
import wetalk_client.view.ChatView;
import wetalk_client.view.LoginView;
import wetalk_client.view.RegisterView;
import wetalk_client.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Handing response from Server
 */
public class UserHandleResponseController {

    /**
     * Handling response from Server after client sent register request
     * @param view current view
     * @param message the response RegisterResponseMessage from server
     */
    public static void register(RegisterView view, RegisterResponseMessage message) {
        if(message.getStatus()) {
            String succeedMessage = message.getData().get("message");
            view.setRegisterSucceed(succeedMessage);
        } else {
            String failMessage = message.getData().get("message");
            view.setRegisterFail(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent login request
     * @param view current view
     * @param message the response LoginResponseMessage from server
     */
    public static void login(LoginView view, LoginResponseMessage message) {
        HashMap<String, String> mapData = message.getData();

        if(message.getStatus()) {
            // store accessToken to Global
            Global.getInstance().setProperty("accessToken", mapData.get("accessToken"));
            int userID = Integer.parseInt(mapData.get("id"));


            // restore the saved username, password and savePassword from login method in UserSendRequest Controller
            String username = Global.getInstance().getProperty("username");
            String password = Global.getInstance().getProperty("password");
            boolean savePassword = (boolean) Global.getInstance().getOrDefault("savePassword", false);

            // store current login-ed user to global
            Global.getInstance().put("user", new UserModel(userID, username));

            // save/delete username and password to temp folder
            String fileName = Global.getInstance().getProperty("tempFilePrefix") + "login" + Global.getInstance().getProperty("tempFileExt");
            if(savePassword) {
                // save
                Map<String, String> tempData = new HashMap<>();
                tempData.put("username", username);
                tempData.put("password", password);
                tempData.put("savePassword", String.valueOf(true));
                String jsonTempData = Json.getInstance().toJson(tempData);
                try {
                    LocalStorage.getInstance().saveDataToFile(fileName, jsonTempData);
                } catch (IOException e) {
                    view.showAlert(e.getMessage());
                }
            } else {
                // delete
                LocalStorage.getInstance().deleteFile(fileName);
            }
            view.loginSucceed();
        } else {
            String failMessage = mapData.get("message");
            view.loginFail(failMessage);
        }

        Global.getInstance().remove("username");
        Global.getInstance().remove("password");
        Global.getInstance().remove("savePassword");
    }

    /**
     * Handling response from Server after client sent getFriendList request
     * @param view current view
     * @param message the response GetFriendListResponseMessage from server
     */
    public static void getFriendList(View view, GetFriendListResponseMessage message) {
        HashMap<String, String> mapData = message.getData();
        if(message.getStatus()) {
            ArrayList<UserModel> friendList = Json.getInstance().fromJson(mapData.get("friendList"), new TypeToken<ArrayList<UserModel>>(){}.getType());
            Global.getInstance().put("friendList", friendList);
        } else {
            String failMessage = mapData.get("message");
            view.showAlert(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent sendMessage request
     * @param view current view
     * @param message the response SendMessageResponseMessage from server
     */
    public static void sendMessage(ChatView view, SendMessageResponseMessage message) {
        if(message.getStatus()) {
            // get the temporarily store current message model
            LinkedBlockingQueue<MessageModel> queue =  (LinkedBlockingQueue<MessageModel>) Global.getInstance().get("sentMessageModelQueue");
            MessageModel messageModel = queue.remove();
            view.setSendMessageSucceed(messageModel);
        } else {
            String failMessage = message.getData().get("message");
            view.setSendMessageFail(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent getLatestData request
     * @param view current view
     * @param message the response GetLatestDataResponseMessage from server
     */
    public static void getLatestData(ChatView view, GetLatestDataResponseMessage message) {
//      {
//          "response":"getLatestData",
//          "status":"succeed",
//          "data" :{
//              "newMessages":"[MessageMode11, MessageMode12,...]",
//              "addFriendRequesters":"[UserModel1, UserModel2,...]",
//              "acceptedUsers":"[UserModel1, UserModel2,...]",
//              "rejectedUsers":"[UserModel1, UserModel2,...]",
//              "deletedUsers":"[UserModel1, UserModel2,...]"
//           }
//      }
        if(message.getStatus()) {
            HashMap<String, String> mapData = message.getData();

            String jsonNewMessages = mapData.get("newMessages");
            ArrayList<MessageModel> newMessages = Json.getInstance().fromJson(jsonNewMessages, new TypeToken<ArrayList<MessageModel>>(){}.getType());
            view.setGetLatestMessageSucceed(newMessages);

            String jsonDeletedUsers = mapData.get("deletedUsers");
            ArrayList<UserModel> deletedUsers = Json.getInstance().fromJson(jsonDeletedUsers, new TypeToken<ArrayList<UserModel>>() {}.getType());
            view.setGetLatestDeletedUsersSucceed(deletedUsers);

            String jsonAddFriendRequesters = mapData.get("addFriendRequesters");
            ArrayList<UserModel> addFriendRequesters = Json.getInstance().fromJson(jsonAddFriendRequesters, new TypeToken<ArrayList<UserModel>>(){}.getType());
            view.setGetLatestAddFriendRequesterSucceed(addFriendRequesters);

            String jsonAcceptedUsers = mapData.get("acceptedUsers");
            ArrayList<UserModel> acceptedUsers = Json.getInstance().fromJson(jsonAcceptedUsers, new TypeToken<ArrayList<UserModel>>(){}.getType());
            view.setGetLatestAcceptedUsersSucceed(acceptedUsers);

            String jsonRejectedUsers = mapData.get("rejectedUsers");
            ArrayList<UserModel> rejectedUsers = Json.getInstance().fromJson(jsonRejectedUsers, new TypeToken<ArrayList<UserModel>>(){}.getType());
            view.setGetLatestRejectedUsersSucceed(rejectedUsers);

        } else {
            String failMessage = message.getData().get("message");
            view.setGetLatestDataFail(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent addFriend request
     * @param view current view
     * @param message the response AddFriendResponseMessage from server
     */
    public static void addFriend(ChatView view, AddFriendResponseMessage message) {
        if(message.getStatus()) {
            view.setAddFriendSucceed("Request sent, please wait for confirmation.");
        } else {
            String failMessage = message.getData().get("message");
            view.setAddFriendFail(failMessage);
        };
    }

    /**
     * Handling response from Server after client sent acceptFriend request
     * @param view current view
     * @param message the response AcceptFriendResponseMessage from server
     */
    public static void acceptFriend(ChatView view, AcceptFriendResponseMessage message) {
        if(!message.getStatus()) {
            String failMessage = message.getData().get("message");
            view.showAlert(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent rejectFriend request
     * @param view current view
     * @param message the response RejectFriendResponseMessage from server
     */
    public static void rejectFriend(ChatView view, RejectFriendResponseMessage message) {
        if(!message.getStatus()) {
            String failMessage = message.getData().get("message");
            view.showAlert(failMessage);
        }
    }

    /**
     * Handling response from Server after client sent deleteFriend request
     * @param view current view
     * @param message the response DeleteFriendResponseMessage from server
     */
    public static void deleteFriend(ChatView view, DeleteFriendResponseMessage message) {
        if(message.getStatus()) {
            view.setDeleteFriendSucceed();
        } else {
            String failMessage = message.getData().get("message");
            view.setDeleteFriendFail(failMessage);
        }
    }
}
