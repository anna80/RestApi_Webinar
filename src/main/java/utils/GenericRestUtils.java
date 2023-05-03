package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;

public final class GenericRestUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private GenericRestUtils(){}

    public static <T> T getResponseAsJsonObject(Response response, Class<T> className){
        return GSON.fromJson(response.getBody().asString(),className);
    }
}
