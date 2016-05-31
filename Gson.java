package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public final class Gson {
    static final AnonymousAndLocalClassExclusionStrategy DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY;
    private static final ExclusionStrategy DEFAULT_EXCLUSION_STRATEGY;
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final ModifierBasedExclusionStrategy DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY;
    static final FieldNamingStrategy2 DEFAULT_NAMING_POLICY;
    static final SyntheticFieldExclusionStrategy DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ExclusionStrategy deserializationExclusionStrategy;
    private final ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers;
    private final FieldNamingStrategy2 fieldNamingPolicy;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final MappedObjectConstructor objectConstructor;
    private final boolean prettyPrinting;
    private final ExclusionStrategy serializationExclusionStrategy;
    private final boolean serializeNulls;
    private final ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers;

    static {
        DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY = new AnonymousAndLocalClassExclusionStrategy();
        DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY = new SyntheticFieldExclusionStrategy(true);
        DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY = new ModifierBasedExclusionStrategy(128, 8);
        DEFAULT_NAMING_POLICY = new SerializedNameAnnotationInterceptingNamingPolicy(new JavaFieldNamingPolicy());
        DEFAULT_EXCLUSION_STRATEGY = createExclusionStrategy();
    }

    public Gson() {
        this(DEFAULT_EXCLUSION_STRATEGY, DEFAULT_EXCLUSION_STRATEGY, DEFAULT_NAMING_POLICY, new MappedObjectConstructor(DefaultTypeAdapters.getDefaultInstanceCreators()), DEFAULT_JSON_NON_EXECUTABLE, DefaultTypeAdapters.getAllDefaultSerializers(), DefaultTypeAdapters.getAllDefaultDeserializers(), DEFAULT_JSON_NON_EXECUTABLE, true, DEFAULT_JSON_NON_EXECUTABLE);
    }

    Gson(ExclusionStrategy deserializationExclusionStrategy, ExclusionStrategy serializationExclusionStrategy, FieldNamingStrategy2 fieldNamingPolicy, MappedObjectConstructor objectConstructor, boolean serializeNulls, ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers, ParameterizedTypeHandlerMap<JsonDeserializer<?>> deserializers, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting) {
        this.deserializationExclusionStrategy = deserializationExclusionStrategy;
        this.serializationExclusionStrategy = serializationExclusionStrategy;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.objectConstructor = objectConstructor;
        this.serializeNulls = serializeNulls;
        this.serializers = serializers;
        this.deserializers = deserializers;
        this.generateNonExecutableJson = generateNonExecutableGson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
    }

    private static ExclusionStrategy createExclusionStrategy() {
        List<ExclusionStrategy> strategies = new LinkedList();
        strategies.add(DEFAULT_ANON_LOCAL_CLASS_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_SYNTHETIC_FIELD_EXCLUSION_STRATEGY);
        strategies.add(DEFAULT_MODIFIER_BASED_EXCLUSION_STRATEGY);
        return new DisjunctionExclusionStrategy(strategies);
    }

    public JsonElement toJsonTree(Object src) {
        if (src == null) {
            return JsonNull.createJsonNull();
        }
        return toJsonTree(src, src.getClass());
    }

    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return new JsonSerializationContextDefault(new ObjectNavigator(this.serializationExclusionStrategy), this.fieldNamingPolicy, this.serializeNulls, this.serializers).serialize(src, typeOfSrc);
    }

    public String toJson(Object src) {
        if (src == null) {
            return toJson(JsonNull.createJsonNull());
        }
        return toJson(src, src.getClass());
    }

    public String toJson(Object src, Type typeOfSrc) {
        Appendable writer = new StringWriter();
        toJson(toJsonTree(src, typeOfSrc), writer);
        return writer.toString();
    }

    public void toJson(Object src, Appendable writer) throws JsonIOException {
        if (src != null) {
            toJson(src, src.getClass(), writer);
        } else {
            toJson(JsonNull.createJsonNull(), writer);
        }
    }

    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        toJson(toJsonTree(src, typeOfSrc), writer);
    }

    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        toJson(toJsonTree(src, typeOfSrc), writer);
    }

    public String toJson(JsonElement jsonElement) {
        Appendable writer = new StringWriter();
        toJson(jsonElement, writer);
        return writer.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        try {
            if (this.generateNonExecutableJson) {
                writer.append(JSON_NON_EXECUTABLE_PREFIX);
            }
            JsonWriter jsonWriter = new JsonWriter(Streams.writerForAppendable(writer));
            if (this.prettyPrinting) {
                jsonWriter.setIndent("  ");
            }
            toJson(jsonElement, jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        try {
            Streams.write(jsonElement, this.serializeNulls, writer);
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
        } catch (Throwable e) {
            throw new JsonIOException(e);
        } catch (Throwable th) {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
        }
    }

    public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return Primitives.wrap(classOfT).cast(fromJson(json, (Type) classOfT));
    }

    public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return fromJson(new StringReader(json), typeOfT);
    }

    public <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        JsonReader jsonReader = new JsonReader(json);
        Object object = fromJson(jsonReader, (Type) classOfT);
        assertFullConsumption(object, jsonReader);
        return Primitives.wrap(classOfT).cast(object);
    }

    public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        JsonReader jsonReader = new JsonReader(json);
        T object = fromJson(jsonReader, typeOfT);
        assertFullConsumption(object, jsonReader);
        return object;
    }

    private static void assertFullConsumption(Object obj, JsonReader reader) {
        if (obj != null) {
            try {
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonIOException(e2);
            }
        }
    }

    public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        boolean oldLenient = reader.isLenient();
        reader.setLenient(true);
        try {
            T fromJson = fromJson(Streams.parse(reader), typeOfT);
            return fromJson;
        } finally {
            reader.setLenient(oldLenient);
        }
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return Primitives.wrap(classOfT).cast(fromJson(json, (Type) classOfT));
    }

    public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return new JsonDeserializationContextDefault(new ObjectNavigator(this.deserializationExclusionStrategy), this.fieldNamingPolicy, this.deserializers, this.objectConstructor).deserialize(json, typeOfT);
    }

    public String toString() {
        return "{" + "serializeNulls:" + this.serializeNulls + ",serializers:" + this.serializers + ",deserializers:" + this.deserializers + ",instanceCreators:" + this.objectConstructor + "}";
    }
}
