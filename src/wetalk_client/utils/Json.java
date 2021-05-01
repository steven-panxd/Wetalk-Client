package wetalk_client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;


/**
 * Wrapper class for Gson, singleton design pattern
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

    public static Json getInstance() {
        return Json.instance;
    }
}
