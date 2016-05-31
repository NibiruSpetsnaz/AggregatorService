package com.google.gson;

import java.io.IOException;
import java.util.Map.Entry;

final class JsonTreeNavigator {
    private final boolean visitNulls;
    private final JsonElementVisitor visitor;

    JsonTreeNavigator(JsonElementVisitor visitor, boolean visitNulls) {
        this.visitor = visitor;
        this.visitNulls = visitNulls;
    }

    public void navigate(JsonElement element) throws IOException {
        if (element.isJsonNull()) {
            this.visitor.visitNull();
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            this.visitor.startArray(array);
            isFirst = true;
            i$ = array.iterator();
            while (i$.hasNext()) {
                visitChild(array, (JsonElement) i$.next(), isFirst);
                if (isFirst) {
                    isFirst = false;
                }
            }
            this.visitor.endArray(array);
        } else if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            this.visitor.startObject(object);
            isFirst = true;
            for (Entry<String, JsonElement> member : object.entrySet()) {
                if (visitChild(object, (String) member.getKey(), (JsonElement) member.getValue(), isFirst) && isFirst) {
                    isFirst = false;
                }
            }
            this.visitor.endObject(object);
        } else {
            this.visitor.visitPrimitive(element.getAsJsonPrimitive());
        }
    }

    private boolean visitChild(JsonObject parent, String childName, JsonElement child, boolean isFirst) throws IOException {
        if (child.isJsonNull()) {
            if (!this.visitNulls) {
                return false;
            }
            this.visitor.visitNullObjectMember(parent, childName, isFirst);
            navigate(child.getAsJsonNull());
        } else if (child.isJsonArray()) {
            JsonArray childAsArray = child.getAsJsonArray();
            this.visitor.visitObjectMember(parent, childName, childAsArray, isFirst);
            navigate(childAsArray);
        } else if (child.isJsonObject()) {
            JsonObject childAsObject = child.getAsJsonObject();
            this.visitor.visitObjectMember(parent, childName, childAsObject, isFirst);
            navigate(childAsObject);
        } else {
            this.visitor.visitObjectMember(parent, childName, child.getAsJsonPrimitive(), isFirst);
        }
        return true;
    }

    private void visitChild(JsonArray parent, JsonElement child, boolean isFirst) throws IOException {
        if (child.isJsonNull()) {
            this.visitor.visitNullArrayMember(parent, isFirst);
            navigate(child);
        } else if (child.isJsonArray()) {
            JsonArray childAsArray = child.getAsJsonArray();
            this.visitor.visitArrayMember(parent, childAsArray, isFirst);
            navigate(childAsArray);
        } else if (child.isJsonObject()) {
            JsonObject childAsObject = child.getAsJsonObject();
            this.visitor.visitArrayMember(parent, childAsObject, isFirst);
            navigate(childAsObject);
        } else {
            this.visitor.visitArrayMember(parent, child.getAsJsonPrimitive(), isFirst);
        }
    }
}
