package wetalk_client.utils;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import javafx.util.Callback;

import java.io.IOException;

public class Network {
    private final static Network instance = new Network();

    private final Client client;

    public Network(){
        this.client = new Client();
        String serverAddress = Global.getInstance().getProperty("mainServerIP");
        int serverPort = Integer.parseInt(Global.getInstance().getProperty("mainServerPort"));
        int localPort = Integer.parseInt(Global.getInstance().getProperty("localPort"));
        this.client.start();
        try {
            this.client.connect(localPort, serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void sendData(String data, Callback<Object, Void> callback) {
        synchronized (this) {
            this.client.sendTCP(data);
            this.client.addListener(new Listener() {
                public void received (Connection connection, Object object) {
                    if(object instanceof FrameworkMessage.KeepAlive) {
                        // do nothing for keep alive connection
                    } else {
                        callback.call(object);
                        Network.getInstance().client.removeListener(this);
                    }
                }
            });
        }
    }

    public void close() {
        this.client.stop();
    }

    public static Network getInstance() {
        return Network.instance;
    }
}
