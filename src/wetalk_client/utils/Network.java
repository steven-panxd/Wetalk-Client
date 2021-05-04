package wetalk_client.utils;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import wetalk_client.view.View;

import java.io.IOException;

public class Network {
    private final Client client;

    public Network(View view, Listener onReceivedDataListener){
        this.client = new Client();
        String serverAddress = Global.getInstance().getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(Global.getInstance().getProperty("SERVER_PORT"));
        int localPort = Integer.parseInt(Global.getInstance().getProperty("LOCAL_PORT"));
        this.client.start();

        try {
            this.client.connect(localPort, serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            view.showAlert("Can not connect to server, please restart the application.");
            System.exit(-1);
        }

        this.client.addListener(onReceivedDataListener);
    }

    public void sendData(String data) {
        this.client.sendTCP(data);
    }
}
