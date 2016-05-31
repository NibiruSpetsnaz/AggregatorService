package com.google.gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class MapAsArrayTypeAdapter extends BaseMapTypeAdapter implements JsonSerializer<Map<?, ?>>, JsonDeserializer<Map<?, ?>> {
    MapAsArrayTypeAdapter() {
    }

    public Map<?, ?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Object, Object> result = BaseMapTypeAdapter.constructMapType(typeOfT, context);
        Type[] keyAndValueType = typeToTypeArguments(typeOfT);
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonArray entryArray = array.get(i).getAsJsonArray();
                result.put(context.deserialize(entryArray.get(0), keyAndValueType[0]), context.deserialize(entryArray.get(1), keyAndValueType[1]));
            }
            checkSize(array, array.size(), result, result.size());
        } else {
            JsonObject object = json.getAsJsonObject();
            for (Entry<String, JsonElement> entry : object.entrySet()) {
                result.put(context.deserialize(new JsonPrimitive((String) entry.getKey()), keyAndValueType[0]), context.deserialize((JsonElement) entry.getValue(), keyAndValueType[1]));
            }
            checkSize(object, object.entrySet().size(), result, result.size());
        }
        return result;
    }

    public JsonElement serialize(Map<?, ?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result;
        Type[] keyAndValueType = typeToTypeArguments(typeOfSrc);
        boolean serializeAsArray = false;
        List<JsonElement> keysAndValues = new ArrayList();
        for (Entry<?, ?> entry : src.entrySet()) {
            JsonElement key = BaseMapTypeAdapter.serialize(context, entry.getKey(), keyAndValueType[0]);
            int i = (key.isJsonObject() || key.isJsonArray()) ? 1 : 0;
            serializeAsArray |= i;
            keysAndValues.add(key);
            keysAndValues.add(BaseMapTypeAdapter.serialize(context, entry.getValue(), keyAndValueType[1]));
        }
        int i2;
        if (serializeAsArray) {
            result = new JsonArray();
            for (i2 = 0; i2 < keysAndValues.size(); i2 += 2) {
                JsonArray entryArray = new JsonArray();
                entryArray.add((JsonElement) keysAndValues.get(i2));
                entryArray.add((JsonElement) keysAndValues.get(i2 + 1));
                result.add(entryArray);
            }
        } else {
            result = new JsonObject();
            for (i2 = 0; i2 < keysAndValues.size(); i2 += 2) {
                result.add(((JsonElement) keysAndValues.get(i2)).getAsString(), (JsonElement) keysAndValues.get(i2 + 1));
            }
            checkSize(src, src.size(), result, result.entrySet().size());
        }
        return result;
    }

    private Type[] typeToTypeArguments(Type typeOfT) {
        if (typeOfT instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) typeOfT).getActualTypeArguments();
            if (actualTypeArguments.length == 2) {
                return actualTypeArguments;
            }
            throw new IllegalArgumentException("MapAsArrayTypeAdapter cannot handle " + typeOfT);
        }
        return new Type[]{Object.class, Object.class};
    }

    private void checkSize(Object input, int inputSize, Object output, int outputSize) {
        if (inputSize != outputSize) {
            throw new JsonSyntaxException("Input size " + inputSize + " != output size " + outputSize + " for input " + input + " and output " + output);
        }
    }
}
