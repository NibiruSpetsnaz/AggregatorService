package com.google.gson;

import java.lang.reflect.Constructor;

final class DefaultConstructorAllocator {
    private static final Constructor<Null> NULL_CONSTRUCTOR;
    private final Cache<Class<?>, Constructor<?>> constructorCache;

    private static final class Null {
        private Null() {
        }
    }

    static {
        NULL_CONSTRUCTOR = createNullConstructor();
    }

    public DefaultConstructorAllocator() {
        this(200);
    }

    public DefaultConstructorAllocator(int cacheSize) {
        this.constructorCache = new LruCache(cacheSize);
    }

    final boolean isInCache(Class<?> cacheKey) {
        return this.constructorCache.getElement(cacheKey) != null;
    }

    private static final Constructor<Null> createNullConstructor() {
        try {
            return getNoArgsConstructor(Null.class);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T newInstance(Class<T> c) throws Exception {
        Constructor<T> constructor = findConstructor(c);
        return constructor != null ? constructor.newInstance(new Object[0]) : null;
    }

    private <T> Constructor<T> findConstructor(Class<T> c) {
        Constructor<T> cachedElement = (Constructor) this.constructorCache.getElement(c);
        if (cachedElement == null) {
            Constructor<T> noArgsConstructor = getNoArgsConstructor(c);
            if (noArgsConstructor != null) {
                this.constructorCache.addElement(c, noArgsConstructor);
            } else {
                this.constructorCache.addElement(c, NULL_CONSTRUCTOR);
            }
            return noArgsConstructor;
        } else if (cachedElement == NULL_CONSTRUCTOR) {
            return null;
        } else {
            return cachedElement;
        }
    }

    private static <T> Constructor<T> getNoArgsConstructor(Class<T> c) {
        try {
            Constructor<T> declaredConstructor = c.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            return declaredConstructor;
        } catch (Exception e) {
            return null;
        }
    }
}
