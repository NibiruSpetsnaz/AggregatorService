package com.google.gson;

import java.lang.reflect.Type;
import java.util.Map;

abstract class BaseMapTypeAdapter implements JsonSerializer<Map<?, ?>>, JsonDeserializer<Map<?, ?>> {
    BaseMapTypeAdapter() {
    }

    protected static final JsonElement serialize(JsonSerializationContext context, Object src, Type srcType) {
        return ((JsonSerializationContextDefault) context).serialize(src, srcType, false);
    }

    protected static final Map<Object, Object> constructMapType(Type mapType, JsonDeserializationContext context) {
        return (Map) ((JsonDeserializationContextDefault) context).getObjectConstructor().construct(mapType);
    }
}
