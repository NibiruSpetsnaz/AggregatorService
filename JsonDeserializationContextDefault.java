package com.google.gson;

import java.lang.reflect.Type;

final class JsonDeserializationContextDefault implements JsonDeserializationContext {
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final MappedObjectConstructor objectConstructor;
    private final ObjectNavigator objectNavigator;

    JsonDeserializationContextDefault(ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, MappedObjectConstructor objectConstructor) {
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.deserializers = deserializers;
        this.objectConstructor = objectConstructor;
    }

    ObjectConstructor getObjectConstructor() {
        return this.objectConstructor;
    }

    public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        if (json.isJsonArray()) {
            return fromJsonArray(typeOfT, json.getAsJsonArray(), this);
        }
        if (json.isJsonObject()) {
            return fromJsonObject(typeOfT, json.getAsJsonObject(), this);
        }
        if (json.isJsonPrimitive()) {
            return fromJsonPrimitive(typeOfT, json.getAsJsonPrimitive(), this);
        }
        throw new JsonParseException("Failed parsing JSON source: " + json + " to Json");
    }

    private <T> T fromJsonArray(Type arrayType, JsonArray jsonArray, JsonDeserializationContext context) throws JsonParseException {
        JsonArrayDeserializationVisitor<T> visitor = new JsonArrayDeserializationVisitor(jsonArray, arrayType, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, context);
        this.objectNavigator.accept(new ObjectTypePair(null, arrayType, true), visitor);
        return visitor.getTarget();
    }

    private <T> T fromJsonObject(Type typeOfT, JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        JsonObjectDeserializationVisitor<T> visitor = new JsonObjectDeserializationVisitor(jsonObject, typeOfT, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, context);
        this.objectNavigator.accept(new ObjectTypePair(null, typeOfT, true), visitor);
        return visitor.getTarget();
    }

    private <T> T fromJsonPrimitive(Type typeOfT, JsonPrimitive json, JsonDeserializationContext context) throws JsonParseException {
        JsonObjectDeserializationVisitor<T> visitor = new JsonObjectDeserializationVisitor(json, typeOfT, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, context);
        this.objectNavigator.accept(new ObjectTypePair(json.getAsObject(), typeOfT, true), visitor);
        return visitor.getTarget();
    }
}
