package wetalk_client.controller;

import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.message.*;
import wetalk_client.model.MessageModel;
import wetalk_client.model.UserModel;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.LocalStorage;
import wetalk_client.utils.Network;
import wetalk_client.view.ChatView;
import wetalk_client.view.LoginView;
import wetalk_client.view.RegisterView;
import wetalk_client.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    static void login(LoginView view, LoginMessage message) {
        String prefix = Global.getInstance().getProperty("loginPrefix");
        String username = message.getUsername();
        String password = message.getPassword();
        boolean savePassword = message.isSavePassword();

        // send data to Server and receive an access token, store the token, a User Object to Global variables
        String data = prefix + ":" + username + ":" + password;
        Network.getInstance().sendData(data, response -> {
            String[] field = ((String) response).split(":", -1);
            if(field[0].equals(Global.getInstance().getProperty("succeedPrefix"))) {
                // store accessToken to Global
                Global.getInstance().setProperty("accessToken", field[1]);
                // store login-ed user information to a UserModel object to Global
                Global.getInstance().put("user", new UserModel(Integer.parseInt(field[2]), field[3]));
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
                view.loginFail(field[1]);
            }
            return null;
        });
    }

    static void register(RegisterView view, RegisterMessage message) {
        // TODO regiter
    }

    static void getFriendList(View view, GetFriendListMessage message) {
        String prefix = Global.getInstance().getProperty("getFriendListPrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken;
        Network.getInstance().sendData(data, response -> {
            String[] fields = ((String) response).split(":", -1);
            if(fields[0].equals(Global.getInstance().getProperty("succeedPrefix"))) {
                if(fields.length > 1) {
                    // sample response data = "<Success>:1,username1:2,username2"
                    ArrayList<UserModel> firendList = new ArrayList<>();
                    for(int i = 1; i < fields.length; i++) {
                        String[] friendData = fields[i].split(",", -1);
                        int friendUserID = Integer.parseInt(friendData[0]);
                        String friendUsername = friendData[1];
                        firendList.add(new UserModel(friendUserID, friendUsername));
                    }
                    Global.getInstance().put("friends", firendList);
                } else {
                    Global.getInstance().put("friends", new ArrayList<UserModel>());
                }
            } else {
                view.showAlert(fields[1]);
            }
            return null;
        });
    }

    static void sendMessage(ChatView view, SendMessageMessage message) {
        MessageModel messageModel = message.getMessageModel();
        String prefix = Global.getInstance().getProperty("sendMessagePrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken + ":" + messageModel.getReceiverID() + ":" + messageModel.getContent() + ":" + messageModel.getSendTimeStamp();
        Network.getInstance().sendData(data, response -> {
            String[] fields = ((String) response).split(":", -1);
            if(fields[0].equals(Global.getInstance().getProperty("succeedPrefix"))) {
                view.setSendMessageSucceed(messageModel);
            } else {
                view.setSendMessageFail(fields[1]);
            }
            return null;
        });
    }

    public static void getLatestMessage(ChatView view, GetLatestMessageMessage message) {
        String prefix = Global.getInstance().getProperty("getLatestMessagePrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken;
        Network.getInstance().sendData(data, response -> {
            // return json data here,  sample data {
            //      "status": "<succeed>",
            //      "data" : "[{}]"
            // }
            System.out.println(response);
            HashMap<String,String> objResponse = Json.getInstance().fromJson((String) response, new TypeToken<HashMap<String,String>>(){}.getType());
            if(objResponse.get("status").equals(Global.getInstance().getProperty("succeedPrefix"))) {
                String jsonNewMessages = objResponse.get("newMessages");
                ArrayList<MessageModel> newMessages = Json.getInstance().fromJson(jsonNewMessages, new TypeToken<ArrayList<MessageModel>>(){}.getType());
                view.setGetLatestMessageSucceed(newMessages);

                String jsonAddFriendRequesters = objResponse.get("addFriendRequesters");
                ArrayList<UserModel> addFriendRequesters = Json.getInstance().fromJson(jsonAddFriendRequesters, new TypeToken<ArrayList<UserModel>>(){}.getType());
                view.setGetLatestAddFriendRequesterSucceed(addFriendRequesters);

                String jsonAcceptedUsers = objResponse.get("acceptedUsers");
                ArrayList<UserModel> acceptedUsers = Json.getInstance().fromJson(jsonAcceptedUsers, new TypeToken<ArrayList<UserModel>>(){}.getType());
                view.setGetLatestAcceptedUsersSucceed(acceptedUsers);

                String jsonRejectedUsers = objResponse.get("rejectedUsers");
                ArrayList<UserModel> rejectedUsers = Json.getInstance().fromJson(jsonRejectedUsers, new TypeToken<ArrayList<UserModel>>(){}.getType());
                view.setGetLatestRejectedUsersSucceed(rejectedUsers);
            } else {
                view.setGetLatestDataFail(objResponse.get("data"));
            }
            return null;
        });
    }

    public static void addFriend(ChatView view, AddFriendMessage message) {
        String prefix = Global.getInstance().getProperty("addFriendPrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken + ":" + message.getFriendID();
        Network.getInstance().sendData(data, response -> {
            System.out.println(response);
            HashMap<String, String> objResponse = Json.getInstance().fromJson((String) response, new TypeToken<HashMap<String, String>>(){}.getType());
            if(objResponse.get("status").equals(Global.getInstance().getProperty("succeedPrefix"))) {
                view.setAddFriendSucceed("Request sent, please wait for confirmation.");
            } else {
                view.setAddFriendFail(objResponse.getOrDefault("data", "Unknown Error"));
            };
            return null;
        });
    }

    public static void acceptFriend(ChatView view, AcceptFriendMessage message) {
        String prefix = Global.getInstance().getProperty("acceptAddFriendPrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken + ":" + message.getAcceptFriendID();
        Network.getInstance().sendData(data, response -> {
            String[] fields = ((String) response).split(":", -1);
            if(fields[0].equals(Global.getInstance().getProperty("failPrefix"))) {
               view.showAlert(fields[1]);
            }
            return null;
        });
    }

    public static void rejectFriend(ChatView view, RejectFriendMessage message) {
        String prefix = Global.getInstance().getProperty("rejectAddFriendPrefix");
        String accessToken = Global.getInstance().getProperty("accessToken");

        String data = prefix + ":" + accessToken + ":" + message.getRejectFriendID();
        Network.getInstance().sendData(data, response -> {
            String[] fields = ((String) response).split(":", -1);
            if(fields[0].equals(Global.getInstance().getProperty("failPrefix"))) {
                view.showAlert(fields[1]);
            }
            return null;
        });
    }
}
