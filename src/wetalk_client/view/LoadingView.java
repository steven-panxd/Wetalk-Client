package wetalk_client.view;

import wetalk_client.controller.requestMessage.GetFriendListRequestMessage;
import wetalk_client.controller.requestMessage.RequestMessage;
import wetalk_client.controller.requestMessage.SwitchViewRequestMessage;
import wetalk_client.utils.Global;

import javax.swing.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * Loading view after login
 * Fetch the latest version of friend list and store it to memory for Chat view to use
 */
public class LoadingView extends View{
    private final BlockingQueue<RequestMessage> queue;

    /**
     * Constructor of LoadingView
     * @param queue The blocking queue to send request to controller
     */
    public LoadingView(BlockingQueue<RequestMessage> queue) {
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
        RequestMessage getFriendListRequestMessage = new GetFriendListRequestMessage();
        try {
            queue.put(getFriendListRequestMessage);
        }catch (InterruptedException e) {
            e.printStackTrace();
            this.showAlert(e.getMessage());
        }

        Runnable runnable = new WaitingFriendListRunnable(Global.getInstance(), () -> {
            RequestMessage requestMessage = new SwitchViewRequestMessage(new ChatView(queue));
            try {
                this.queue.put(requestMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.showAlert(e.getMessage());
            }
            return null;
        });

        new Thread(runnable).start();
    }

    private static class WaitingFriendListRunnable implements Runnable {
        private final Global global;
        private final Callable<Void> callback;

        public WaitingFriendListRunnable(Global global, Callable<Void> callback) {
            this.global = global;
            this.callback = callback;
        }

        public void run() {
            while(true) {
                Object data = this.global.get("friendList");

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
