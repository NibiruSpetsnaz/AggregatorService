package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

final class JsonArrayDeserializationVisitor<T> extends JsonDeserializationVisitor<T> {
    JsonArrayDeserializationVisitor(JsonArray jsonArray, Type arrayType, ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, ObjectConstructor objectConstructor, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, JsonDeserializationContext context) {
        super(jsonArray, arrayType, objectNavigator, fieldNamingPolicy, objectConstructor, deserializers, context);
    }

    protected T constructTarget() {
        if (this.json.isJsonArray()) {
            JsonArray jsonArray = this.json.getAsJsonArray();
            if (C$Gson$Types.isArray(this.targetType)) {
                return this.objectConstructor.constructArray(C$Gson$Types.getArrayComponentType(this.targetType), jsonArray.size());
            }
            return this.objectConstructor.construct(C$Gson$Types.getRawType(this.targetType));
        }
        throw new JsonParseException("Expecting array found: " + this.json);
    }

    public void visitArray(Object array, Type arrayType) {
        if (this.json.isJsonArray()) {
            JsonArray jsonArray = this.json.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                Object obj;
                JsonElement jsonChild = jsonArray.get(i);
                if (jsonChild == null || jsonChild.isJsonNull()) {
                    obj = null;
                } else if (jsonChild instanceof JsonObject) {
                    obj = visitChildAsObject(C$Gson$Types.getArrayComponentType(arrayType), jsonChild);
                } else if (jsonChild instanceof JsonArray) {
                    obj = visitChildAsArray(C$Gson$Types.getArrayComponentType(arrayType), jsonChild.getAsJsonArray());
                } else if (jsonChild instanceof JsonPrimitive) {
                    obj = visitChildAsObject(C$Gson$Types.getArrayComponentType(arrayType), jsonChild.getAsJsonPrimitive());
                } else {
                    throw new IllegalStateException();
                }
                Array.set(array, i, obj);
            }
            return;
        }
        throw new JsonParseException("Expecting array found: " + this.json);
    }

    public void startVisitingObject(Object node) {
        throw new JsonParseException("Expecting array but found object: " + node);
    }

    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        throw new JsonParseException("Expecting array but found array field " + f.getName() + ": " + obj);
    }

    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        throw new JsonParseException("Expecting array but found object field " + f.getName() + ": " + obj);
    }

    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type actualTypeOfField, Object parent) {
        throw new JsonParseException("Expecting array but found field " + f.getName() + ": " + parent);
    }

    public void visitPrimitive(Object primitive) {
        throw new JsonParseException("Type information is unavailable, and the target is not a primitive: " + this.json);
    }
}
