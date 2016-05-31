package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

final class MapTypeAdapter extends BaseMapTypeAdapter {
    MapTypeAdapter() {
    }

    public JsonElement serialize(Map src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject map = new JsonObject();
        Type childGenericType = null;
        if (typeOfSrc instanceof ParameterizedType) {
            childGenericType = C$Gson$Types.getMapKeyAndValueTypes(typeOfSrc, C$Gson$Types.getRawType(typeOfSrc))[1];
        }
        for (Entry entry : src.entrySet()) {
            JsonElement valueElement;
            Object value = entry.getValue();
            if (value == null) {
                valueElement = JsonNull.createJsonNull();
            } else {
                Type childType;
                if (childGenericType == null) {
                    childType = value.getClass();
                } else {
                    childType = childGenericType;
                }
                valueElement = BaseMapTypeAdapter.serialize(context, value, childType);
            }
            map.add(String.valueOf(entry.getKey()), valueElement);
        }
        return map;
    }

    public Map deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Object, Object> map = BaseMapTypeAdapter.constructMapType(typeOfT, context);
        Type[] keyAndValueTypes = C$Gson$Types.getMapKeyAndValueTypes(typeOfT, C$Gson$Types.getRawType(typeOfT));
        for (Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            map.put(context.deserialize(new JsonPrimitive((String) entry.getKey()), keyAndValueTypes[0]), context.deserialize((JsonElement) entry.getValue(), keyAndValueTypes[1]));
        }
        return map;
    }

    public String toString() {
        return MapTypeAdapter.class.getSimpleName();
    }
}
