package com.google.gson;

import java.lang.reflect.Type;

final class JsonObjectDeserializationVisitor<T> extends JsonDeserializationVisitor<T> {
    JsonObjectDeserializationVisitor(JsonElement json, Type type, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        super(json, type, objectNavigator, fieldNamingPolicy, objectConstructor, deserializers, context);
    }

    protected T constructTarget() {
        return this.objectConstructor.construct(this.targetType);
    }

    public void startVisitingObject(Object node) {
    }

    public void visitArray(Object array, Type componentType) {
        throw new JsonParseException("Expecting object but found array: " + array);
    }

    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (this.json.isJsonObject()) {
                JsonElement jsonChild = this.json.getAsJsonObject().get(getFieldName(f));
                if (jsonChild != null) {
                    f.set(obj, visitChildAsObject(typeOfF, jsonChild));
                    return;
                } else {
                    f.set(obj, null);
                    return;
                }
            }
            throw new JsonParseException("Expecting object found: " + this.json);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (this.json.isJsonObject()) {
                JsonArray jsonChild = (JsonArray) this.json.getAsJsonObject().get(getFieldName(f));
                if (jsonChild != null) {
                    f.set(obj, visitChildAsArray(typeOfF, jsonChild));
                    return;
                } else {
                    f.set(obj, null);
                    return;
                }
            }
            throw new JsonParseException("Expecting object found: " + this.json);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFieldName(FieldAttributes f) {
        return this.fieldNamingPolicy.translateName(f);
    }

    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type declaredTypeOfField, Object parent) {
        try {
            String fName = getFieldName(f);
            if (this.json.isJsonObject()) {
                JsonElement child = this.json.getAsJsonObject().get(fName);
                boolean isPrimitive = Primitives.isPrimitive(declaredTypeOfField);
                if (child == null) {
                    return true;
                }
                if (!child.isJsonNull()) {
                    Pair<JsonDeserializer<?>, ObjectTypePair> pair = new ObjectTypePair(null, declaredTypeOfField, false).getMatchingHandler(this.deserializers);
                    if (pair == null) {
                        return false;
                    }
                    Object value = invokeCustomDeserializer(child, pair);
                    if (value == null && isPrimitive) {
                        return true;
                    }
                    f.set(parent, value);
                    return true;
                } else if (isPrimitive) {
                    return true;
                } else {
                    f.set(parent, null);
                    return true;
                }
            }
            throw new JsonParseException("Expecting object found: " + this.json);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    public void visitPrimitive(Object primitive) {
        if (this.json.isJsonPrimitive()) {
            this.target = this.json.getAsJsonPrimitive().getAsObject();
            return;
        }
        throw new JsonParseException("Type information is unavailable, and the target object is not a primitive: " + this.json);
    }
}
