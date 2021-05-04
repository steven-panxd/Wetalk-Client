package wetalk_client.controller;

import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.google.gson.reflect.TypeToken;
import wetalk_client.controller.responseMessage.*;
import wetalk_client.controller.requestMessage.*;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;
import wetalk_client.utils.Network;
import wetalk_client.view.ChatView;
import wetalk_client.view.LoginView;
import wetalk_client.view.RegisterView;
import wetalk_client.view.View;

import com.esotericsoftware.kryonet.Connection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Main controller of the project
 */
public class MainController {
    View view;
    BlockingQueue<RequestMessage> queue;

    /**
     * Constructor of MainController
     * @param view current displaying view (JFrame window)
     * @param queue a blocking queue contains RequestMessage sends from view
     */
    public MainController(View view, BlockingQueue<RequestMessage> queue) {
        this.view = view;
        this.queue = queue;
    }

    /**
     * Setup network connection to Server
     * Setup MyReceivedDataListener to handle returning data from Server
     * Setup a blocking queue stores messages send by current user
     * The while loop takes messages from view, cast the message to different methods in controllers,
     * then encodes HashMap data to Json string request data and sends it to Server
     */
    public void mainLoop() {
        Network networkConn = new Network(this.view, new MyReceivedDataListener());

        // temporarily stores sent message from login-ed user
        Global.getInstance().put("sentMessageModelQueue", new LinkedBlockingQueue<>());

        while (true) {
            RequestMessage requestMessage = null;
            try {
                requestMessage = queue.take();
            } catch (InterruptedException exception) {
                // ignore
            }

            HashMap<String, String> mapData;
            HashMap<String, String> mapRequest = new HashMap<>();
            // Send Data Router
            if(requestMessage != null) {
                if (requestMessage instanceof SwitchViewRequestMessage) {
                    this.view = ViewController.switchView(view, (SwitchViewRequestMessage) requestMessage);
                    continue;
                } else if (requestMessage instanceof GetLatestDataRequestMessage) { // get the most recent messages
                    mapData = UserSendRequestController.getLatestData((GetLatestDataRequestMessage) requestMessage);
                } else if (requestMessage instanceof LoginRequestMessage) {  // login
                    mapData = UserSendRequestController.login((LoginRequestMessage) requestMessage);
                } else if (requestMessage instanceof RegisterRequestMessage) {
                    mapData = UserSendRequestController.register((RegisterRequestMessage) requestMessage);
                } else if (requestMessage instanceof GetFriendListRequestMessage) {
                    mapData = UserSendRequestController.getFriendList((GetFriendListRequestMessage) requestMessage);
                } else if (requestMessage instanceof SendMessageRequestMessage) {
                    mapData = UserSendRequestController.sendMessage((SendMessageRequestMessage) requestMessage);
                } else if (requestMessage instanceof AddFriendRequestMessage) {
                    mapData = UserSendRequestController.addFriend((AddFriendRequestMessage) requestMessage);
                } else if (requestMessage instanceof AcceptFriendRequestMessage) {
                    mapData = UserSendRequestController.acceptFriend((AcceptFriendRequestMessage) requestMessage);
                } else if (requestMessage instanceof RejectFriendRequestMessage) {
                    mapData = UserSendRequestController.rejectFriend((RejectFriendRequestMessage) requestMessage);
                } else if (requestMessage instanceof DeleteFriendRequestMessage) {
                    mapData = UserSendRequestController.deleteFriend((DeleteFriendRequestMessage) requestMessage);
                } else {
                    this.view.showAlert("Client: No such router rule.");
                    continue;
                }

                // put mapData to mapRequest
                if(mapData != null) {
                    mapRequest.put("data", Json.getInstance().toJson(mapData));
                } else {
                    mapRequest.put("data", "");
                }

                // put accessToken to request if exists
                String accessToken = Global.getInstance().getProperty("accessToken");
                if(accessToken != null) {
                    mapRequest.put("accessToken", accessToken);
                }

                // put requestName to request if exists
                mapRequest.put("request", requestMessage.getRequestName());

                // send data
                String data = Json.getInstance().toJson(mapRequest);
                networkConn.sendData(data);
            }
        }
    }

    /**
     * Listens on the data from the Server
     */
    private class MyReceivedDataListener extends Listener{

        /**
         * Be called when client receives Json string response from Server
         * Decodes response and cast it to different response handlers
         * @param connection current connection with Server
         * @param response Json string response from server
         */
        @Override
        public void received(Connection connection, Object response) {
            if(response instanceof FrameworkMessage.KeepAlive) {
                return;  // do noting for keep alive connection from Kyronet
            }
            HashMap<String, String> objResponse = Json.getInstance().fromJson((String) response, new TypeToken<HashMap<String, String>>() {}.getType());
            String responseStr = objResponse.get("response");
            if (responseStr.equals(LoginResponseMessage.responseName)) {  // handling login response
                LoginResponseMessage message = new LoginResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.login((LoginView) view, message);
            } else if (responseStr.equals(RegisterResponseMessage.responseName)) {
                RegisterResponseMessage message = new RegisterResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.register((RegisterView) view, message);
            } else if (responseStr.equals(GetFriendListResponseMessage.responseName)) {  // handling getFriendList response
                GetFriendListResponseMessage message = new GetFriendListResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.getFriendList(view, message);
            } else if (responseStr.equals(SendMessageResponseMessage.responseName)) {
                SendMessageResponseMessage message = new SendMessageResponseMessage(objResponse.get("status"), objResponse.getOrDefault("data", ""));
                UserHandleResponseController.sendMessage((ChatView) view, message);
            } else if (responseStr.equals(GetLatestDataResponseMessage.responseName)) {
                GetLatestDataResponseMessage message = new GetLatestDataResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.getLatestData((ChatView) view, message);
            } else if (responseStr.equals(AddFriendResponseMessage.responseName)) {
                AddFriendResponseMessage message = new AddFriendResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.addFriend((ChatView) view, message);
            } else if (responseStr.equals(AcceptFriendResponseMessage.responseName)) {
                AcceptFriendResponseMessage message = new AcceptFriendResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.acceptFriend((ChatView) view, message);
            } else if (responseStr.equals(RejectFriendResponseMessage.responseName)) {
                RejectFriendResponseMessage message = new RejectFriendResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.rejectFriend((ChatView) view, message);
            } else if(responseStr.equals(DeleteFriendResponseMessage.responseName)) {
                DeleteFriendResponseMessage message = new DeleteFriendResponseMessage(objResponse.get("status"), objResponse.get("data"));
                UserHandleResponseController.deleteFriend((ChatView) view, message);
            } else {
                GenericResponseMessage message = new GenericResponseMessage(objResponse.get("status"), objResponse.get("data"));
                view.showAlert(message.getData().get("message"));
            }
        }
    }
}
