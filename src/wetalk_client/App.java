package wetalk_client;

import wetalk_client.controller.MainController;
import wetalk_client.controller.requestMessage.RequestMessage;
import wetalk_client.view.LoginView;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Entrance of the project
 */
public class App {
    public static void main(String[] args) {
        LinkedBlockingQueue<RequestMessage> queue = new LinkedBlockingQueue<>();
        LoginView view = new LoginView(queue);
        MainController controller = new MainController(view, queue);
        controller.mainLoop();
    }
}
