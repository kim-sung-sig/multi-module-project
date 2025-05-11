package com.example.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonProvider {

    private static final Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Enum.class, new EnumToStringAdapter())
            .create();

    private static final Gson PRETTY_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Enum.class, new EnumToStringAdapter())
            .create();

    private GsonProvider() {
    }

    public static String toJson(Object object) {
        return DEFAULT_GSON.toJson(object);
    }

    public static String toJsonPretty(Object object) {
        return PRETTY_GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return DEFAULT_GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return DEFAULT_GSON.fromJson(json, typeOfT);
    }

}

class EnumToStringAdapter implements JsonSerializer<Enum<?>> {
    @Override
    public JsonElement serialize(Enum<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }
}
