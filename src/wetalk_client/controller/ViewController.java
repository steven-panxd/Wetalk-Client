package wetalk_client.controller;

import wetalk_client.controller.requestMessage.SwitchViewRequestMessage;
import wetalk_client.view.View;

public class ViewController {

    static View switchView(View oldView, SwitchViewRequestMessage message) {
        oldView.dispose();
        return message.getView();
    }
}
