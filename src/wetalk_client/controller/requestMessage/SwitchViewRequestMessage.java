package wetalk_client.controller.requestMessage;

import wetalk_client.view.View;

public class SwitchViewRequestMessage extends RequestMessage {
    private final View view;

    public SwitchViewRequestMessage(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
