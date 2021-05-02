package wetalk_client.controller.requestMessage;

public abstract class RequestMessage {
    public String getRequestName() {
        String requestName = null;
        try {
            requestName = (String) this.getClass().getDeclaredField("requestName").get(this);
        } catch (Exception e) {
            // ignore
        }
        return requestName;
    }
}
