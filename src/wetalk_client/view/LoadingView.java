package wetalk_client.view;

import javafx.util.Callback;
import wetalk_client.controller.message.GetFriendListMessage;
import wetalk_client.controller.message.Message;
import wetalk_client.controller.message.SwitchViewMessage;
import wetalk_client.utils.Global;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class LoadingView extends View{
    private final BlockingQueue<Message> queue;

    public LoadingView(BlockingQueue<Message> queue) {
        super();

        this.queue = queue;

        this.setTitle("Loading...");

        JLabel loadingLabel = new JLabel("Loading...Please wait...");
        this.add(loadingLabel);

        this.setBounds(700, 350, 300, 300);
        this.setVisible(true);

        this.initChatView();
    }

    private void initChatView() {
        Message getFriendListMessage = new GetFriendListMessage();
        try {
            queue.put(getFriendListMessage);
        }catch (InterruptedException e) {
            e.printStackTrace();
            this.showAlert(e.getMessage());
        }

        Runnable runnable = new WaitingFriendListRunnable(Global.getInstance(), () -> {
            Message message = new SwitchViewMessage(new ChatView(queue));
            try {
                this.queue.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.showAlert(e.getMessage());
            }
            return null;
        });

        new Thread(runnable).start();
    }

    class WaitingFriendListRunnable implements Runnable {
        private final Global global;
        private final Callable<Void> callback;

        public WaitingFriendListRunnable(Global global, Callable<Void> callback) {
            this.global = global;
            this.callback = callback;
        }

        public void run() {
            while(true) {
                Object data = this.global.get("friends");

                // if data, break
                if (data != null) {
                    try {
                        this.callback.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

                // if no data, sleep one second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
