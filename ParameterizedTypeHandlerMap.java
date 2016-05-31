package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ParameterizedTypeHandlerMap<T> {
    private static final Logger logger;
    private final Map<Type, T> map;
    private boolean modifiable;
    private final List<Pair<Class<?>, T>> typeHierarchyList;

    ParameterizedTypeHandlerMap() {
        this.map = new HashMap();
        this.typeHierarchyList = new ArrayList();
        this.modifiable = true;
    }

    static {
        logger = Logger.getLogger(ParameterizedTypeHandlerMap.class.getName());
    }

    public synchronized void registerForTypeHierarchy(Class<?> typeOfT, T value) {
        registerForTypeHierarchy(new Pair(typeOfT, value));
    }

    public synchronized void registerForTypeHierarchy(Pair<Class<?>, T> pair) {
        if (this.modifiable) {
            int index = getIndexOfSpecificHandlerForTypeHierarchy((Class) pair.first);
            if (index >= 0) {
                logger.log(Level.WARNING, "Overriding the existing type handler for {0}", pair.first);
                this.typeHierarchyList.remove(index);
            }
            index = getIndexOfAnOverriddenHandler((Class) pair.first);
            if (index >= 0) {
                throw new IllegalArgumentException("The specified type handler for type " + pair.first + " hides the previously registered type hierarchy handler for " + ((Pair) this.typeHierarchyList.get(index)).first + ". Gson does not allow this.");
            }
            this.typeHierarchyList.add(0, pair);
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    private int getIndexOfAnOverriddenHandler(Class<?> type) {
        for (int i = this.typeHierarchyList.size() - 1; i >= 0; i--) {
            if (type.isAssignableFrom((Class) ((Pair) this.typeHierarchyList.get(i)).first)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void register(Type typeOfT, T value) {
        if (this.modifiable) {
            if (hasSpecificHandlerFor(typeOfT)) {
                logger.log(Level.WARNING, "Overriding the existing type handler for {0}", typeOfT);
            }
            this.map.put(typeOfT, value);
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    public synchronized void registerIfAbsent(ParameterizedTypeHandlerMap<T> other) {
        if (this.modifiable) {
            for (Entry<Type, T> entry : other.map.entrySet()) {
                if (!this.map.containsKey(entry.getKey())) {
                    register((Type) entry.getKey(), entry.getValue());
                }
            }
            for (int i = other.typeHierarchyList.size() - 1; i >= 0; i--) {
                Pair<Class<?>, T> entry2 = (Pair) other.typeHierarchyList.get(i);
                if (getIndexOfSpecificHandlerForTypeHierarchy((Class) entry2.first) < 0) {
                    registerForTypeHierarchy(entry2);
                }
            }
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    public synchronized void register(ParameterizedTypeHandlerMap<T> other) {
        if (this.modifiable) {
            for (Entry<Type, T> entry : other.map.entrySet()) {
                register((Type) entry.getKey(), entry.getValue());
            }
            for (int i = other.typeHierarchyList.size() - 1; i >= 0; i--) {
                registerForTypeHierarchy((Pair) other.typeHierarchyList.get(i));
            }
        } else {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        }
    }

    public synchronized void registerIfAbsent(Type typeOfT, T value) {
        if (!this.modifiable) {
            throw new IllegalStateException("Attempted to modify an unmodifiable map.");
        } else if (!this.map.containsKey(typeOfT)) {
            register(typeOfT, value);
        }
    }

    public synchronized void makeUnmodifiable() {
        this.modifiable = false;
    }

    public synchronized T getHandlerFor(Type type) {
        T handler;
        handler = this.map.get(type);
        if (handler == null) {
            Type rawClass = C$Gson$Types.getRawType(type);
            if (rawClass != type) {
                handler = getHandlerFor(rawClass);
            }
            if (handler == null) {
                handler = getHandlerForTypeHierarchy(rawClass);
            }
        }
        return handler;
    }

    private T getHandlerForTypeHierarchy(Class<?> type) {
        for (Pair<Class<?>, T> entry : this.typeHierarchyList) {
            if (((Class) entry.first).isAssignableFrom(type)) {
                return entry.second;
            }
        }
        return null;
    }

    public synchronized boolean hasSpecificHandlerFor(Type type) {
        return this.map.containsKey(type);
    }

    private synchronized int getIndexOfSpecificHandlerForTypeHierarchy(Class<?> type) {
        int i;
        i = this.typeHierarchyList.size() - 1;
        while (i >= 0) {
            if (type.equals(((Pair) this.typeHierarchyList.get(i)).first)) {
                break;
            }
            i--;
        }
        i = -1;
        return i;
    }

    public synchronized ParameterizedTypeHandlerMap<T> copyOf() {
        ParameterizedTypeHandlerMap<T> copy;
        copy = new ParameterizedTypeHandlerMap();
        copy.map.putAll(this.map);
        copy.typeHierarchyList.addAll(this.typeHierarchyList);
        return copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{mapForTypeHierarchy:{");
        boolean first = true;
        for (Pair<Class<?>, T> entry : this.typeHierarchyList) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(typeToString((Type) entry.first)).append(':');
            sb.append(entry.second);
        }
        sb.append("},map:{");
        first = true;
        for (Entry<Type, T> entry2 : this.map.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(typeToString((Type) entry2.getKey())).append(':');
            sb.append(entry2.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    private String typeToString(Type type) {
        return C$Gson$Types.getRawType(type).getSimpleName();
    }
}
