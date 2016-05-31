package com.google.gson;

import com.google.gson.ObjectNavigator.Visitor;
import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

final class JsonSerializationVisitor implements Visitor {
    private final MemoryRefStack ancestors;
    private final JsonSerializationContext context;
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final ObjectNavigator objectNavigator;
    private JsonElement root;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    JsonSerializationVisitor(ObjectNavigator objectNavigator, FieldNamingStrategy2 fieldNamingPolicy, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, JsonSerializationContext context, MemoryRefStack ancestors) {
        this.objectNavigator = objectNavigator;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
        this.context = context;
        this.ancestors = ancestors;
    }

    public Object getTarget() {
        return null;
    }

    public void start(ObjectTypePair node) {
        if (node != null) {
            if (this.ancestors.contains(node)) {
                throw new CircularReferenceException(node);
            }
            this.ancestors.push(node);
        }
    }

    public void end(ObjectTypePair node) {
        if (node != null) {
            this.ancestors.pop();
        }
    }

    public void startVisitingObject(Object node) {
        assignToRoot(new JsonObject());
    }

    public void visitArray(Object array, Type arrayType) {
        assignToRoot(new JsonArray());
        int length = Array.getLength(array);
        Type componentType = C$Gson$Types.getArrayComponentType(arrayType);
        for (int i = 0; i < length; i++) {
            addAsArrayElement(new ObjectTypePair(Array.get(array, i), componentType, false));
        }
    }

    public void visitArrayField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (!isFieldNull(f, obj)) {
                addAsChildOfObject(f, new ObjectTypePair(getFieldValue(f, obj), typeOfF, false));
            } else if (this.serializeNulls) {
                addChildAsElement(f, JsonNull.createJsonNull());
            }
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(f);
        }
    }

    public void visitObjectField(FieldAttributes f, Type typeOfF, Object obj) {
        try {
            if (!isFieldNull(f, obj)) {
                addAsChildOfObject(f, new ObjectTypePair(getFieldValue(f, obj), typeOfF, false));
            } else if (this.serializeNulls) {
                addChildAsElement(f, JsonNull.createJsonNull());
            }
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(f);
        }
    }

    public void visitPrimitive(Object obj) {
        assignToRoot(obj == null ? JsonNull.createJsonNull() : new JsonPrimitive(obj));
    }

    private void addAsChildOfObject(FieldAttributes f, ObjectTypePair fieldValuePair) {
        addChildAsElement(f, getJsonElementForChild(fieldValuePair));
    }

    private void addChildAsElement(FieldAttributes f, JsonElement childElement) {
        this.root.getAsJsonObject().add(this.fieldNamingPolicy.translateName(f), childElement);
    }

    private void addAsArrayElement(ObjectTypePair elementTypePair) {
        if (elementTypePair.getObject() == null) {
            this.root.getAsJsonArray().add(JsonNull.createJsonNull());
            return;
        }
        this.root.getAsJsonArray().add(getJsonElementForChild(elementTypePair));
    }

    private JsonElement getJsonElementForChild(ObjectTypePair fieldValueTypePair) {
        JsonSerializationVisitor childVisitor = new JsonSerializationVisitor(this.objectNavigator, this.fieldNamingPolicy, this.serializeNulls, this.serializers, this.context, this.ancestors);
        this.objectNavigator.accept(fieldValueTypePair, childVisitor);
        return childVisitor.getJsonElement();
    }

    public boolean visitUsingCustomHandler(ObjectTypePair objTypePair) {
        try {
            if (objTypePair.getObject() != null) {
                JsonElement element = findAndInvokeCustomSerializer(objTypePair);
                if (element == null) {
                    return false;
                }
                assignToRoot(element);
                return true;
            } else if (!this.serializeNulls) {
                return true;
            } else {
                assignToRoot(JsonNull.createJsonNull());
                return true;
            }
        } catch (CircularReferenceException e) {
            throw e.createDetailedException(null);
        }
    }

    private JsonElement findAndInvokeCustomSerializer(ObjectTypePair objTypePair) {
        Pair<JsonSerializer<?>, ObjectTypePair> pair = objTypePair.getMatchingHandler(this.serializers);
        if (pair == null) {
            return null;
        }
        JsonSerializer serializer = pair.first;
        objTypePair = pair.second;
        start(objTypePair);
        try {
            JsonElement element = serializer.serialize(objTypePair.getObject(), objTypePair.getType(), this.context);
            if (element == null) {
                element = JsonNull.createJsonNull();
            }
            end(objTypePair);
            return element;
        } catch (Throwable th) {
            end(objTypePair);
        }
    }

    public boolean visitFieldUsingCustomHandler(FieldAttributes f, Type declaredTypeOfField, Object parent) {
        try {
            C$Gson$Preconditions.checkState(this.root.isJsonObject());
            Object obj = f.get(parent);
            if (obj != null) {
                JsonElement child = findAndInvokeCustomSerializer(new ObjectTypePair(obj, declaredTypeOfField, false));
                if (child == null) {
                    return false;
                }
                addChildAsElement(f, child);
                return true;
            } else if (!this.serializeNulls) {
                return true;
            } else {
                addChildAsElement(f, JsonNull.createJsonNull());
                return true;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        } catch (CircularReferenceException e2) {
            throw e2.createDetailedException(f);
        }
    }

    private void assignToRoot(JsonElement newRoot) {
        this.root = (JsonElement) C$Gson$Preconditions.checkNotNull(newRoot);
    }

    private boolean isFieldNull(FieldAttributes f, Object obj) {
        return getFieldValue(f, obj) == null;
    }

    private Object getFieldValue(FieldAttributes f, Object obj) {
        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement getJsonElement() {
        return this.root;
    }
}
