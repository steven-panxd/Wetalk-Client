package wetalk_client;

import wetalk_client.controller.MainController;
import wetalk_client.controller.message.Message;
import wetalk_client.view.LoginView;

import java.util.concurrent.LinkedBlockingQueue;

public class App {
    public static void main(String[] args) {
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        LoginView view = new LoginView(queue);
        MainController controller = new MainController(view, queue);
        controller.mainLoop();
    }
}