package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class JsonObject extends JsonElement {
    private final Map<String, JsonElement> members;

    public JsonObject() {
        this.members = new LinkedHashMap();
    }

    public void add(String property, JsonElement value) {
        if (value == null) {
            value = JsonNull.createJsonNull();
        }
        this.members.put(C$Gson$Preconditions.checkNotNull(property), value);
    }

    public JsonElement remove(String property) {
        return (JsonElement) this.members.remove(property);
    }

    public void addProperty(String property, String value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, Number value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, Boolean value) {
        add(property, createJsonElement(value));
    }

    public void addProperty(String property, Character value) {
        add(property, createJsonElement(value));
    }

    private JsonElement createJsonElement(Object value) {
        return value == null ? JsonNull.createJsonNull() : new JsonPrimitive(value);
    }

    public Set<Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }

    public boolean has(String memberName) {
        return this.members.containsKey(memberName);
    }

    public JsonElement get(String memberName) {
        if (!this.members.containsKey(memberName)) {
            return null;
        }
        JsonElement member = (JsonElement) this.members.get(memberName);
        if (member == null) {
            return JsonNull.createJsonNull();
        }
        return member;
    }

    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return (JsonPrimitive) this.members.get(memberName);
    }

    public JsonArray getAsJsonArray(String memberName) {
        return (JsonArray) this.members.get(memberName);
    }

    public JsonObject getAsJsonObject(String memberName) {
        return (JsonObject) this.members.get(memberName);
    }

    public boolean equals(Object o) {
        return o == this || ((o instanceof JsonObject) && ((JsonObject) o).members.equals(this.members));
    }

    public int hashCode() {
        return this.members.hashCode();
    }

    protected void toString(Appendable sb, Escaper escaper) throws IOException {
        sb.append('{');
        boolean first = true;
        for (Entry<String, JsonElement> entry : this.members.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append('\"');
            sb.append(escaper.escapeJsonString((CharSequence) entry.getKey()));
            sb.append("\":");
            ((JsonElement) entry.getValue()).toString(sb, escaper);
        }
        sb.append('}');
    }
}
