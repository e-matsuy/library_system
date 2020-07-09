package LibraryManager.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;

public final class GsonSingleton {
    private static final Gson GSON_INSTANCE = new GsonBuilder().setLenient().serializeNulls().create();

    public static synchronized <T> T fromJson(String jsonString, Class<T> classoft){
        return GSON_INSTANCE.fromJson(jsonString, classoft);
    }

    public static synchronized <T> T fromJson(Reader jsonReader, Class<T> classoft){
        return GSON_INSTANCE.fromJson(jsonReader, classoft);
    }

    public static synchronized String toJson(Object object){
        return GSON_INSTANCE.toJson(object);
    }
}
