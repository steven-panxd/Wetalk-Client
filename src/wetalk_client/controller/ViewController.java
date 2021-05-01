package wetalk_client.controller;

import wetalk_client.controller.message.SwitchViewMessage;
import wetalk_client.view.View;

public class ViewController {

    static View switchView(View oldView, SwitchViewMessage message) {
        oldView.dispose();
        return message.getView();
    }
}
