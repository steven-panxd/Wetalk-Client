package wetalk_client.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;


/**
 * Wrapper class for Gson
 * An encoder and decoder of Json string data
 * singleton design pattern
 */
public class Json{
    private static final Json instance = new Json();
    private final Gson gson;

    public Json() {
        this.gson = new Gson();
    }

    public <T> T fromJson(String json, Type typeOf) {
        return this.gson.fromJson(json, typeOf);
    }

    public String toJson(Object src) {
        return this.gson.toJson(src);
    }

    /**
     * returns the only one instance of Json
     * @return an instance of Json
     */
    public static Json getInstance() {
        return Json.instance;
    }
}
