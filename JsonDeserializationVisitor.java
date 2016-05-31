package com.google.gson;

import com.google.gson.ObjectNavigator.Visitor;
import com.google.gson.internal.C$Gson$Preconditions;
import java.lang.reflect.Type;

abstract class JsonDeserializationVisitor<T> implements Visitor {
    protected boolean constructed;
    protected final JsonDeserializationContext context;
    protected final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    protected final FieldNamingStrategy2 fieldNamingPolicy;
    protected final JsonElement json;
    protected final ObjectConstructor objectConstructor;
    protected final ObjectNavigator objectNavigator;
    protected T target;
    protected final Type targetType;

    protected abstract T constructTarget();

    JsonDeserializationVisitor(JsonElement json, Type targetType, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        this.targetType = targetType;
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.objectConstructor = objectConstructor;
        this.deserializers = deserializers;
        this.json = (JsonElement) C$Gson$Preconditions.checkNotNull(json);
        this.context = context;
        this.constructed = false;
    }

    public T getTarget() {
        if (!this.constructed) {
            this.target = constructTarget();
            this.constructed = true;
        }
        return this.target;
    }

    public void start(ObjectTypePair node) {
    }

    public void end(ObjectTypePair node) {
    }

    public final boolean visitUsingCustomHandler(ObjectTypePair objTypePair) {
        Pair<JsonDeserializer<?>, ObjectTypePair> pair = objTypePair.getMatchingHandler(this.deserializers);
        if (pair == null) {
            return false;
        }
        this.target = invokeCustomDeserializer(this.json, pair);
        this.constructed = true;
        return true;
    }

    protected Object invokeCustomDeserializer(JsonElement element, Pair<JsonDeserializer<?>, ObjectTypePair> pair) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return ((JsonDeserializer) pair.first).deserialize(element, ((ObjectTypePair) pair.second).type, this.context);
    }

    final Object visitChildAsObject(Type childType, JsonElement jsonChild) {
        return visitChild(childType, new JsonObjectDeserializationVisitor(jsonChild, childType, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, this.context));
    }

    final Object visitChildAsArray(Type childType, JsonArray jsonChild) {
        return visitChild(childType, new JsonArrayDeserializationVisitor(jsonChild.getAsJsonArray(), childType, this.objectNavigator, this.fieldNamingPolicy, this.objectConstructor, this.deserializers, this.context));
    }

    private Object visitChild(Type type, JsonDeserializationVisitor<?> childVisitor) {
        this.objectNavigator.accept(new ObjectTypePair(null, type, false), childVisitor);
        return childVisitor.getTarget();
    }
}
