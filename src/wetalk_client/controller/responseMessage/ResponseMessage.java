package wetalk_client.controller.responseMessage;

import com.google.gson.reflect.TypeToken;
import wetalk_client.utils.Global;
import wetalk_client.utils.Json;

import java.util.HashMap;

public abstract class ResponseMessage{
    private final String status;
    private final String rawData;

    public ResponseMessage(String status, String data) {
        this.status = status;
        this.rawData = data;
    }

    public boolean getStatus() {
        return status.equals(Global.getInstance().getProperty("succeedPrefix"));
    }

    public HashMap<String, String> getData() {
        return Json.getInstance().fromJson(this.rawData, new TypeToken<HashMap<String, String>>() {}.getType());
    }
}
