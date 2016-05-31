package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

final class MappedObjectConstructor implements ObjectConstructor {
    private static final DefaultConstructorAllocator defaultConstructorAllocator;
    private static final UnsafeAllocator unsafeAllocator;
    private final ParameterizedTypeHandlerMap<InstanceCreator<?>> instanceCreatorMap;

    static {
        unsafeAllocator = UnsafeAllocator.create();
        defaultConstructorAllocator = new DefaultConstructorAllocator(500);
    }

    public MappedObjectConstructor(ParameterizedTypeHandlerMap<InstanceCreator<?>> instanceCreators) {
        this.instanceCreatorMap = instanceCreators;
    }

    public <T> T construct(Type typeOfT) {
        InstanceCreator<T> creator = (InstanceCreator) this.instanceCreatorMap.getHandlerFor(typeOfT);
        if (creator != null) {
            return creator.createInstance(typeOfT);
        }
        return constructWithAllocators(typeOfT);
    }

    public Object constructArray(Type type, int length) {
        return Array.newInstance(C$Gson$Types.getRawType(type), length);
    }

    private <T> T constructWithAllocators(Type typeOfT) {
        try {
            Class<T> clazz = C$Gson$Types.getRawType(typeOfT);
            T obj = defaultConstructorAllocator.newInstance(clazz);
            if (obj == null) {
                obj = unsafeAllocator.newInstance(clazz);
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke no-args constructor for " + typeOfT + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", e);
        }
    }

    public String toString() {
        return this.instanceCreatorMap.toString();
    }
}
