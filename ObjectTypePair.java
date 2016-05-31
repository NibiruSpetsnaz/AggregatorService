package com.google.gson;

import java.lang.reflect.Type;

final class ObjectTypePair {
    private Object obj;
    private final boolean preserveType;
    final Type type;

    ObjectTypePair(Object obj, Type type, boolean preserveType) {
        this.obj = obj;
        this.type = type;
        this.preserveType = preserveType;
    }

    Object getObject() {
        return this.obj;
    }

    void setObject(Object obj) {
        this.obj = obj;
    }

    Type getType() {
        return this.type;
    }

    public String toString() {
        return String.format("preserveType: %b, type: %s, obj: %s", new Object[]{Boolean.valueOf(this.preserveType), this.type, this.obj});
    }

    <HANDLER> Pair<HANDLER, ObjectTypePair> getMatchingHandler(ParameterizedTypeHandlerMap<HANDLER> handlers) {
        HANDLER handler;
        if (!(this.preserveType || this.obj == null)) {
            ObjectTypePair moreSpecificType = toMoreSpecificType();
            handler = handlers.getHandlerFor(moreSpecificType.type);
            if (handler != null) {
                return new Pair(handler, moreSpecificType);
            }
        }
        handler = handlers.getHandlerFor(this.type);
        return handler == null ? null : new Pair(handler, this);
    }

    ObjectTypePair toMoreSpecificType() {
        if (this.preserveType || this.obj == null) {
            return this;
        }
        Type actualType = getActualTypeIfMoreSpecific(this.type, this.obj.getClass());
        return actualType != this.type ? new ObjectTypePair(this.obj, actualType, this.preserveType) : this;
    }

    Type getMoreSpecificType() {
        if (this.preserveType || this.obj == null) {
            return this.type;
        }
        return getActualTypeIfMoreSpecific(this.type, this.obj.getClass());
    }

    static Type getActualTypeIfMoreSpecific(Type type, Class<?> actualClass) {
        if (!(type instanceof Class)) {
            return type;
        }
        if (((Class) type).isAssignableFrom(actualClass)) {
            type = actualClass;
        }
        if (type == Object.class) {
            return actualClass;
        }
        return type;
    }

    public int hashCode() {
        return this.obj == null ? 31 : this.obj.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ObjectTypePair other = (ObjectTypePair) obj;
        if (this.obj == null) {
            if (other.obj != null) {
                return false;
            }
        } else if (this.obj != other.obj) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.preserveType != other.preserveType) {
            z = false;
        }
        return z;
    }

    public boolean isPreserveType() {
        return this.preserveType;
    }
}
