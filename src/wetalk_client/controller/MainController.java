package wetalk_client.controller;

import wetalk_client.controller.message.*;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.Network;
import wetalk_client.view.ChatView;
import wetalk_client.view.LoginView;
import wetalk_client.view.RegisterView;
import wetalk_client.view.View;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class MainController {
    View view;
    BlockingQueue<Message> queue;

    public MainController(View view, BlockingQueue<Message> queue) {
        this.view = view;
        this.queue = queue;
    }

    public void mainLoop() {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                String accessToken = Global.getInstance().getProperty("accessToken", null);
                if(accessToken != null) {
                    HashMap<String, String> mapData = new HashMap<>();
                    mapData.put("operation", Global.getInstance().getProperty("logoutPrefix"));
                    mapData.put("accessToken", accessToken);
                    String jsonData = Json.getInstance().toJson(mapData);
                    Network.getInstance().sendData(jsonData, (r) -> { return null; });
                }
            }
        });

        while (true) {
            Message message = null;
            try {
                message = queue.take();
            } catch (InterruptedException exception) {
                // do nothing
            }

            // Router
            if(message != null) {
                if (message instanceof SwitchViewMessage) {
                    this.view = ViewController.switchView(view, (SwitchViewMessage) message);
                } else if (message instanceof GetLatestMessageMessage) { // get the most recent messages
                    UserController.getLatestMessage((ChatView) view, (GetLatestMessageMessage) message);
                } else if (message instanceof LoginMessage) {  // login
                    UserController.login((LoginView) view, (LoginMessage) message);
                } else if (message instanceof RegisterMessage) {
                    UserController.register((RegisterView) view, (RegisterMessage) message);
                } else if (message instanceof GetFriendListMessage) {
                    UserController.getFriendList(view, (GetFriendListMessage) message);
                } else if (message instanceof SendMessageMessage) {
                    UserController.sendMessage((ChatView) view, (SendMessageMessage) message);
                } else if (message instanceof AddFriendMessage) {
                    UserController.addFriend((ChatView) view, (AddFriendMessage) message);
                } else if (message instanceof AcceptFriendMessage) {
                    UserController.acceptFriend((ChatView) view, (AcceptFriendMessage) message);
                } else if (message instanceof RejectFriendMessage) {
                    UserController.rejectFriend((ChatView) view, (RejectFriendMessage) message);
                } else {
                    this.view.showAlert("Client Router Error!");
                }
            }
        }
    }
}
