package com.google.gson;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

abstract class UnsafeAllocator {

    /* renamed from: com.google.gson.UnsafeAllocator.1 */
    static class C00101 extends UnsafeAllocator {
        final /* synthetic */ Method val$allocateInstance;
        final /* synthetic */ Object val$unsafe;

        C00101(Method method, Object obj) {
            this.val$allocateInstance = method;
            this.val$unsafe = obj;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$allocateInstance.invoke(this.val$unsafe, new Object[]{c});
        }
    }

    /* renamed from: com.google.gson.UnsafeAllocator.2 */
    static class C00112 extends UnsafeAllocator {
        final /* synthetic */ Method val$newInstance;

        C00112(Method method) {
            this.val$newInstance = method;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$newInstance.invoke(null, new Object[]{c, Object.class});
        }
    }

    /* renamed from: com.google.gson.UnsafeAllocator.3 */
    static class C00123 extends UnsafeAllocator {
        final /* synthetic */ int val$constructorId;
        final /* synthetic */ Method val$newInstance;

        C00123(Method method, int i) {
            this.val$newInstance = method;
            this.val$constructorId = i;
        }

        public <T> T newInstance(Class<T> c) throws Exception {
            return this.val$newInstance.invoke(null, new Object[]{c, Integer.valueOf(this.val$constructorId)});
        }
    }

    /* renamed from: com.google.gson.UnsafeAllocator.4 */
    static class C00134 extends UnsafeAllocator {
        C00134() {
        }

        public <T> T newInstance(Class<T> c) {
            throw new UnsupportedOperationException("Cannot allocate " + c);
        }
    }

    public abstract <T> T newInstance(Class<T> cls) throws Exception;

    UnsafeAllocator() {
    }

    public static UnsafeAllocator create() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return new C00101(unsafeClass.getMethod("allocateInstance", new Class[]{Class.class}), f.get(null));
        } catch (Exception e) {
            Method newInstance;
            try {
                newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Class.class});
                newInstance.setAccessible(true);
                return new C00112(newInstance);
            } catch (Exception e2) {
                try {
                    Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[]{Class.class});
                    getConstructorId.setAccessible(true);
                    int constructorId = ((Integer) getConstructorId.invoke(null, new Object[]{Object.class})).intValue();
                    newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Integer.TYPE});
                    newInstance.setAccessible(true);
                    return new C00123(newInstance, constructorId);
                } catch (Exception e3) {
                    return new C00134();
                }
            }
        }
    }
}
