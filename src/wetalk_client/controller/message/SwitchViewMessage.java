package wetalk_client.controller.message;

import wetalk_client.view.View;

public class SwitchViewMessage implements Message {
    private final View view;

    public SwitchViewMessage(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
