package wetalk_client.controller;

import wetalk_client.controller.requestMessage.SwitchViewRequestMessage;
import wetalk_client.view.View;


/**
 * Handling messages from view
 * Receives messages regarding view operations (Such as change current view)
 */
public class ViewController {

    /**
     * Handling switch view message from view
     * @param oldView the view that need to be disposed
     * @param message the new view that client wants to display
     * @return new view
     */
    static View switchView(View oldView, SwitchViewRequestMessage message) {
        oldView.dispose();
        return message.getView();
    }
}
