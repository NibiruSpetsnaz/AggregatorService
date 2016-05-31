package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.util.Iterator;
import java.util.Stack;

final class MemoryRefStack {
    private final Stack<ObjectTypePair> stack;

    MemoryRefStack() {
        this.stack = new Stack();
    }

    public ObjectTypePair push(ObjectTypePair obj) {
        C$Gson$Preconditions.checkNotNull(obj);
        return (ObjectTypePair) this.stack.push(obj);
    }

    public ObjectTypePair pop() {
        return (ObjectTypePair) this.stack.pop();
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public ObjectTypePair peek() {
        return (ObjectTypePair) this.stack.peek();
    }

    public boolean contains(ObjectTypePair obj) {
        if (obj == null) {
            return false;
        }
        Iterator i$ = this.stack.iterator();
        while (i$.hasNext()) {
            ObjectTypePair stackObject = (ObjectTypePair) i$.next();
            if (stackObject.getObject() == obj.getObject() && stackObject.type.equals(obj.type)) {
                return true;
            }
        }
        return false;
    }
}
