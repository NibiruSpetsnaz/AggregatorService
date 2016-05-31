package com.google.gson;

import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;

final class DefaultTypeAdapters {
    private static final BigDecimalTypeAdapter BIG_DECIMAL_TYPE_ADAPTER;
    private static final BigIntegerTypeAdapter BIG_INTEGER_TYPE_ADAPTER;
    private static final BooleanTypeAdapter BOOLEAN_TYPE_ADAPTER;
    private static final ByteTypeAdapter BYTE_TYPE_ADAPTER;
    private static final CharacterTypeAdapter CHARACTER_TYPE_ADAPTER;
    private static final CollectionTypeAdapter COLLECTION_TYPE_ADAPTER;
    private static final DefaultDateTypeAdapter DATE_TYPE_ADAPTER;
    private static final ParameterizedTypeHandlerMap<JsonDeserializer<?>> DEFAULT_DESERIALIZERS;
    static final ParameterizedTypeHandlerMap<JsonDeserializer<?>> DEFAULT_HIERARCHY_DESERIALIZERS;
    static final ParameterizedTypeHandlerMap<JsonSerializer<?>> DEFAULT_HIERARCHY_SERIALIZERS;
    private static final ParameterizedTypeHandlerMap<InstanceCreator<?>> DEFAULT_INSTANCE_CREATORS;
    private static final ParameterizedTypeHandlerMap<JsonSerializer<?>> DEFAULT_SERIALIZERS;
    private static final DoubleDeserializer DOUBLE_TYPE_ADAPTER;
    private static final EnumTypeAdapter ENUM_TYPE_ADAPTER;
    private static final FloatDeserializer FLOAT_TYPE_ADAPTER;
    private static final GregorianCalendarTypeAdapter GREGORIAN_CALENDAR_TYPE_ADAPTER;
    private static final DefaultInetAddressAdapter INET_ADDRESS_ADAPTER;
    private static final IntegerTypeAdapter INTEGER_TYPE_ADAPTER;
    private static final DefaultJavaSqlDateTypeAdapter JAVA_SQL_DATE_TYPE_ADAPTER;
    private static final LocaleTypeAdapter LOCALE_TYPE_ADAPTER;
    private static final LongDeserializer LONG_DESERIALIZER;
    private static final MapTypeAdapter MAP_TYPE_ADAPTER;
    private static final NumberTypeAdapter NUMBER_TYPE_ADAPTER;
    private static final ShortTypeAdapter SHORT_TYPE_ADAPTER;
    private static final StringBufferTypeAdapter STRING_BUFFER_TYPE_ADAPTER;
    private static final StringBuilderTypeAdapter STRING_BUILDER_TYPE_ADAPTER;
    private static final StringTypeAdapter STRING_TYPE_ADAPTER;
    private static final DefaultTimestampDeserializer TIMESTAMP_DESERIALIZER;
    private static final DefaultTimeTypeAdapter TIME_TYPE_ADAPTER;
    private static final UriTypeAdapter URI_TYPE_ADAPTER;
    private static final UrlTypeAdapter URL_TYPE_ADAPTER;
    private static final UuidTypeAdapter UUUID_TYPE_ADAPTER;

    private static final class BigDecimalTypeAdapter implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {
        private BigDecimalTypeAdapter() {
        }

        public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive((Number) src);
        }

        public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsBigDecimal();
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return BigDecimalTypeAdapter.class.getSimpleName();
        }
    }

    private static final class BigIntegerTypeAdapter implements JsonSerializer<BigInteger>, JsonDeserializer<BigInteger> {
        private BigIntegerTypeAdapter() {
        }

        public JsonElement serialize(BigInteger src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive((Number) src);
        }

        public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsBigInteger();
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return BigIntegerTypeAdapter.class.getSimpleName();
        }
    }

    private static final class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {
        private BooleanTypeAdapter() {
        }

        public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }

        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Boolean.valueOf(json.getAsBoolean());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        public String toString() {
            return BooleanTypeAdapter.class.getSimpleName();
        }
    }

    private static final class ByteTypeAdapter implements JsonSerializer<Byte>, JsonDeserializer<Byte> {
        private ByteTypeAdapter() {
        }

        public JsonElement serialize(Byte src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive((Number) src);
        }

        public Byte deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Byte.valueOf(json.getAsByte());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return ByteTypeAdapter.class.getSimpleName();
        }
    }

    private static final class CharacterTypeAdapter implements JsonSerializer<Character>, JsonDeserializer<Character> {
        private CharacterTypeAdapter() {
        }

        public JsonElement serialize(Character src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }

        public Character deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Character.valueOf(json.getAsCharacter());
        }

        public String toString() {
            return CharacterTypeAdapter.class.getSimpleName();
        }
    }

    private static final class CollectionTypeAdapter implements JsonSerializer<Collection>, JsonDeserializer<Collection> {
        private CollectionTypeAdapter() {
        }

        public JsonElement serialize(Collection src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return JsonNull.createJsonNull();
            }
            JsonElement array = new JsonArray();
            Type childGenericType = null;
            if (typeOfSrc instanceof ParameterizedType) {
                childGenericType = C$Gson$Types.getCollectionElementType(typeOfSrc, C$Gson$Types.getRawType(typeOfSrc));
            }
            for (Object child : src) {
                if (child == null) {
                    array.add(JsonNull.createJsonNull());
                } else {
                    Type childType;
                    if (childGenericType == null || childGenericType == Object.class) {
                        childType = child.getClass();
                    } else {
                        childType = childGenericType;
                    }
                    array.add(context.serialize(child, childType));
                }
            }
            return array;
        }

        public Collection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }
            Collection collection = constructCollectionType(typeOfT, context);
            Type childType = C$Gson$Types.getCollectionElementType(typeOfT, C$Gson$Types.getRawType(typeOfT));
            Iterator i$ = json.getAsJsonArray().iterator();
            while (i$.hasNext()) {
                JsonElement childElement = (JsonElement) i$.next();
                if (childElement == null || childElement.isJsonNull()) {
                    collection.add(null);
                } else {
                    collection.add(context.deserialize(childElement, childType));
                }
            }
            return collection;
        }

        private Collection constructCollectionType(Type collectionType, JsonDeserializationContext context) {
            return (Collection) ((JsonDeserializationContextDefault) context).getObjectConstructor().construct(collectionType);
        }
    }

    private static final class DefaultConstructorCreator<T> implements InstanceCreator<T> {
        private final DefaultConstructorAllocator allocator;
        private final Class<? extends T> defaultInstance;

        public DefaultConstructorCreator(Class<? extends T> defaultInstance, DefaultConstructorAllocator allocator) {
            this.defaultInstance = defaultInstance;
            this.allocator = allocator;
        }

        public T createInstance(Type type) {
            try {
                T specificInstance = this.allocator.newInstance(C$Gson$Types.getRawType(type));
                if (specificInstance == null) {
                    specificInstance = this.allocator.newInstance(this.defaultInstance);
                }
                return specificInstance;
            } catch (Throwable e) {
                throw new JsonIOException(e);
            }
        }

        public String toString() {
            return DefaultConstructorCreator.class.getSimpleName();
        }
    }

    static final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private final DateFormat enUsFormat;
        private final DateFormat iso8601Format;
        private final DateFormat localFormat;

        DefaultDateTypeAdapter() {
            this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
        }

        DefaultDateTypeAdapter(String datePattern) {
            this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
        }

        DefaultDateTypeAdapter(int style) {
            this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
        }

        public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
            this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
        }

        DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
            this.enUsFormat = enUsFormat;
            this.localFormat = localFormat;
            this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement jsonPrimitive;
            synchronized (this.localFormat) {
                jsonPrimitive = new JsonPrimitive(this.enUsFormat.format(src));
            }
            return jsonPrimitive;
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonPrimitive) {
                Date date = deserializeToDate(json);
                if (typeOfT == Date.class) {
                    return date;
                }
                if (typeOfT == Timestamp.class) {
                    return new Timestamp(date.getTime());
                }
                if (typeOfT == java.sql.Date.class) {
                    return new java.sql.Date(date.getTime());
                }
                throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
            }
            throw new JsonParseException("The date should be a string value");
        }

        private Date deserializeToDate(JsonElement json) {
            Date parse;
            synchronized (this.localFormat) {
                try {
                    parse = this.localFormat.parse(json.getAsString());
                } catch (ParseException e) {
                    try {
                        parse = this.enUsFormat.parse(json.getAsString());
                    } catch (ParseException e2) {
                        try {
                            parse = this.iso8601Format.parse(json.getAsString());
                        } catch (ParseException e3) {
                            throw new JsonSyntaxException(json.getAsString(), e3);
                        }
                    }
                }
            }
            return parse;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(DefaultDateTypeAdapter.class.getSimpleName());
            sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
            return sb.toString();
        }
    }

    static final class DefaultInetAddressAdapter implements JsonDeserializer<InetAddress>, JsonSerializer<InetAddress> {
        DefaultInetAddressAdapter() {
        }

        public InetAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return InetAddress.getByName(json.getAsString());
            } catch (Throwable e) {
                throw new JsonParseException(e);
            }
        }

        public JsonElement serialize(InetAddress src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getHostAddress());
        }
    }

    static final class DefaultJavaSqlDateTypeAdapter implements JsonSerializer<java.sql.Date>, JsonDeserializer<java.sql.Date> {
        private final DateFormat format;

        DefaultJavaSqlDateTypeAdapter() {
            this.format = new SimpleDateFormat("MMM d, yyyy");
        }

        public JsonElement serialize(java.sql.Date src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement jsonPrimitive;
            synchronized (this.format) {
                jsonPrimitive = new JsonPrimitive(this.format.format(src));
            }
            return jsonPrimitive;
        }

        public java.sql.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonPrimitive) {
                try {
                    java.sql.Date date;
                    synchronized (this.format) {
                        date = new java.sql.Date(this.format.parse(json.getAsString()).getTime());
                    }
                    return date;
                } catch (Throwable e) {
                    throw new JsonSyntaxException(e);
                }
            }
            throw new JsonParseException("The date should be a string value");
        }
    }

    static final class DefaultTimeTypeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {
        private final DateFormat format;

        DefaultTimeTypeAdapter() {
            this.format = new SimpleDateFormat("hh:mm:ss a");
        }

        public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
            JsonElement jsonPrimitive;
            synchronized (this.format) {
                jsonPrimitive = new JsonPrimitive(this.format.format(src));
            }
            return jsonPrimitive;
        }

        public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonPrimitive) {
                try {
                    Time time;
                    synchronized (this.format) {
                        time = new Time(this.format.parse(json.getAsString()).getTime());
                    }
                    return time;
                } catch (Throwable e) {
                    throw new JsonSyntaxException(e);
                }
            }
            throw new JsonParseException("The date should be a string value");
        }
    }

    static final class DefaultTimestampDeserializer implements JsonDeserializer<Timestamp> {
        DefaultTimestampDeserializer() {
        }

        public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Timestamp(((Date) context.deserialize(json, Date.class)).getTime());
        }
    }

    private static final class DoubleDeserializer implements JsonDeserializer<Double> {
        private DoubleDeserializer() {
        }

        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Double.valueOf(json.getAsDouble());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return DoubleDeserializer.class.getSimpleName();
        }
    }

    static final class DoubleSerializer implements JsonSerializer<Double> {
        private final boolean serializeSpecialFloatingPointValues;

        DoubleSerializer(boolean serializeSpecialDoubleValues) {
            this.serializeSpecialFloatingPointValues = serializeSpecialDoubleValues;
        }

        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            if (this.serializeSpecialFloatingPointValues || (!Double.isNaN(src.doubleValue()) && !Double.isInfinite(src.doubleValue()))) {
                return new JsonPrimitive((Number) src);
            }
            throw new IllegalArgumentException(src + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialDoubleValues() method.");
        }
    }

    private static final class EnumTypeAdapter<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {
        private EnumTypeAdapter() {
        }

        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.name());
        }

        public T deserialize(JsonElement json, Type classOfT, JsonDeserializationContext context) throws JsonParseException {
            return Enum.valueOf((Class) classOfT, json.getAsString());
        }

        public String toString() {
            return EnumTypeAdapter.class.getSimpleName();
        }
    }

    private static final class FloatDeserializer implements JsonDeserializer<Float> {
        private FloatDeserializer() {
        }

        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Float.valueOf(json.getAsFloat());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return FloatDeserializer.class.getSimpleName();
        }
    }

    static final class FloatSerializer implements JsonSerializer<Float> {
        private final boolean serializeSpecialFloatingPointValues;

        FloatSerializer(boolean serializeSpecialDoubleValues) {
            this.serializeSpecialFloatingPointValues = serializeSpecialDoubleValues;
        }

        public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
            if (this.serializeSpecialFloatingPointValues || (!Float.isNaN(src.floatValue()) && !Float.isInfinite(src.floatValue()))) {
                return new JsonPrimitive((Number) src);
            }
            throw new IllegalArgumentException(src + " is not a valid float value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }

    private static final class GregorianCalendarTypeAdapter implements JsonSerializer<GregorianCalendar>, JsonDeserializer<GregorianCalendar> {
        private static final String DAY_OF_MONTH = "dayOfMonth";
        private static final String HOUR_OF_DAY = "hourOfDay";
        private static final String MINUTE = "minute";
        private static final String MONTH = "month";
        private static final String SECOND = "second";
        private static final String YEAR = "year";

        private GregorianCalendarTypeAdapter() {
        }

        public JsonElement serialize(GregorianCalendar src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty(YEAR, Integer.valueOf(src.get(1)));
            obj.addProperty(MONTH, Integer.valueOf(src.get(2)));
            obj.addProperty(DAY_OF_MONTH, Integer.valueOf(src.get(5)));
            obj.addProperty(HOUR_OF_DAY, Integer.valueOf(src.get(11)));
            obj.addProperty(MINUTE, Integer.valueOf(src.get(12)));
            obj.addProperty(SECOND, Integer.valueOf(src.get(13)));
            return obj;
        }

        public GregorianCalendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            return new GregorianCalendar(obj.get(YEAR).getAsInt(), obj.get(MONTH).getAsInt(), obj.get(DAY_OF_MONTH).getAsInt(), obj.get(HOUR_OF_DAY).getAsInt(), obj.get(MINUTE).getAsInt(), obj.get(SECOND).getAsInt());
        }

        public String toString() {
            return GregorianCalendarTypeAdapter.class.getSimpleName();
        }
    }

    private static final class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        private IntegerTypeAdapter() {
        }

        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive((Number) src);
        }

        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Integer.valueOf(json.getAsInt());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return IntegerTypeAdapter.class.getSimpleName();
        }
    }

    private static final class LocaleTypeAdapter implements JsonSerializer<Locale>, JsonDeserializer<Locale> {
        private LocaleTypeAdapter() {
        }

        public JsonElement serialize(Locale src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public Locale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            StringTokenizer tokenizer = new StringTokenizer(json.getAsString(), "_");
            String language = null;
            String country = null;
            String variant = null;
            if (tokenizer.hasMoreElements()) {
                language = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                country = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreElements()) {
                variant = tokenizer.nextToken();
            }
            if (country == null && variant == null) {
                return new Locale(language);
            }
            if (variant == null) {
                return new Locale(language, country);
            }
            return new Locale(language, country, variant);
        }

        public String toString() {
            return LocaleTypeAdapter.class.getSimpleName();
        }
    }

    private static final class LongDeserializer implements JsonDeserializer<Long> {
        private LongDeserializer() {
        }

        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Long.valueOf(json.getAsLong());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return LongDeserializer.class.getSimpleName();
        }
    }

    private static final class LongSerializer implements JsonSerializer<Long> {
        private final LongSerializationPolicy longSerializationPolicy;

        private LongSerializer(LongSerializationPolicy longSerializationPolicy) {
            this.longSerializationPolicy = longSerializationPolicy;
        }

        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return this.longSerializationPolicy.serialize(src);
        }

        public String toString() {
            return LongSerializer.class.getSimpleName();
        }
    }

    private static final class NumberTypeAdapter implements JsonSerializer<Number>, JsonDeserializer<Number> {
        private NumberTypeAdapter() {
        }

        public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }

        public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsNumber();
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return NumberTypeAdapter.class.getSimpleName();
        }
    }

    private static final class ShortTypeAdapter implements JsonSerializer<Short>, JsonDeserializer<Short> {
        private ShortTypeAdapter() {
        }

        public JsonElement serialize(Short src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive((Number) src);
        }

        public Short deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Short.valueOf(json.getAsShort());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            } catch (Throwable e2) {
                throw new JsonSyntaxException(e2);
            } catch (Throwable e22) {
                throw new JsonSyntaxException(e22);
            }
        }

        public String toString() {
            return ShortTypeAdapter.class.getSimpleName();
        }
    }

    private static final class StringBufferTypeAdapter implements JsonSerializer<StringBuffer>, JsonDeserializer<StringBuffer> {
        private StringBufferTypeAdapter() {
        }

        public JsonElement serialize(StringBuffer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public StringBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new StringBuffer(json.getAsString());
        }

        public String toString() {
            return StringBufferTypeAdapter.class.getSimpleName();
        }
    }

    private static final class StringBuilderTypeAdapter implements JsonSerializer<StringBuilder>, JsonDeserializer<StringBuilder> {
        private StringBuilderTypeAdapter() {
        }

        public JsonElement serialize(StringBuilder src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public StringBuilder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new StringBuilder(json.getAsString());
        }

        public String toString() {
            return StringBuilderTypeAdapter.class.getSimpleName();
        }
    }

    private static final class StringTypeAdapter implements JsonSerializer<String>, JsonDeserializer<String> {
        private StringTypeAdapter() {
        }

        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }

        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsString();
        }

        public String toString() {
            return StringTypeAdapter.class.getSimpleName();
        }
    }

    private static final class UriTypeAdapter implements JsonSerializer<URI>, JsonDeserializer<URI> {
        private UriTypeAdapter() {
        }

        public JsonElement serialize(URI src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toASCIIString());
        }

        public URI deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new URI(json.getAsString());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public String toString() {
            return UriTypeAdapter.class.getSimpleName();
        }
    }

    private static final class UrlTypeAdapter implements JsonSerializer<URL>, JsonDeserializer<URL> {
        private UrlTypeAdapter() {
        }

        public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toExternalForm());
        }

        public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new URL(json.getAsString());
            } catch (Throwable e) {
                throw new JsonSyntaxException(e);
            }
        }

        public String toString() {
            return UrlTypeAdapter.class.getSimpleName();
        }
    }

    private static final class UuidTypeAdapter implements JsonSerializer<UUID>, JsonDeserializer<UUID> {
        private UuidTypeAdapter() {
        }

        public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return UUID.fromString(json.getAsString());
        }

        public String toString() {
            return UuidTypeAdapter.class.getSimpleName();
        }
    }

    DefaultTypeAdapters() {
    }

    static {
        DATE_TYPE_ADAPTER = new DefaultDateTypeAdapter();
        JAVA_SQL_DATE_TYPE_ADAPTER = new DefaultJavaSqlDateTypeAdapter();
        TIME_TYPE_ADAPTER = new DefaultTimeTypeAdapter();
        TIMESTAMP_DESERIALIZER = new DefaultTimestampDeserializer();
        ENUM_TYPE_ADAPTER = new EnumTypeAdapter();
        URL_TYPE_ADAPTER = new UrlTypeAdapter();
        URI_TYPE_ADAPTER = new UriTypeAdapter();
        UUUID_TYPE_ADAPTER = new UuidTypeAdapter();
        LOCALE_TYPE_ADAPTER = new LocaleTypeAdapter();
        INET_ADDRESS_ADAPTER = new DefaultInetAddressAdapter();
        COLLECTION_TYPE_ADAPTER = new CollectionTypeAdapter();
        MAP_TYPE_ADAPTER = new MapTypeAdapter();
        BIG_DECIMAL_TYPE_ADAPTER = new BigDecimalTypeAdapter();
        BIG_INTEGER_TYPE_ADAPTER = new BigIntegerTypeAdapter();
        BOOLEAN_TYPE_ADAPTER = new BooleanTypeAdapter();
        BYTE_TYPE_ADAPTER = new ByteTypeAdapter();
        CHARACTER_TYPE_ADAPTER = new CharacterTypeAdapter();
        DOUBLE_TYPE_ADAPTER = new DoubleDeserializer();
        FLOAT_TYPE_ADAPTER = new FloatDeserializer();
        INTEGER_TYPE_ADAPTER = new IntegerTypeAdapter();
        LONG_DESERIALIZER = new LongDeserializer();
        NUMBER_TYPE_ADAPTER = new NumberTypeAdapter();
        SHORT_TYPE_ADAPTER = new ShortTypeAdapter();
        STRING_TYPE_ADAPTER = new StringTypeAdapter();
        STRING_BUILDER_TYPE_ADAPTER = new StringBuilderTypeAdapter();
        STRING_BUFFER_TYPE_ADAPTER = new StringBufferTypeAdapter();
        GREGORIAN_CALENDAR_TYPE_ADAPTER = new GregorianCalendarTypeAdapter();
        DEFAULT_SERIALIZERS = createDefaultSerializers();
        DEFAULT_HIERARCHY_SERIALIZERS = createDefaultHierarchySerializers();
        DEFAULT_DESERIALIZERS = createDefaultDeserializers();
        DEFAULT_HIERARCHY_DESERIALIZERS = createDefaultHierarchyDeserializers();
        DEFAULT_INSTANCE_CREATORS = createDefaultInstanceCreators();
    }

    private static ParameterizedTypeHandlerMap<JsonSerializer<?>> createDefaultSerializers() {
        ParameterizedTypeHandlerMap<JsonSerializer<?>> map = new ParameterizedTypeHandlerMap();
        map.register(URL.class, URL_TYPE_ADAPTER);
        map.register(URI.class, URI_TYPE_ADAPTER);
        map.register(UUID.class, UUUID_TYPE_ADAPTER);
        map.register(Locale.class, LOCALE_TYPE_ADAPTER);
        map.register(Date.class, DATE_TYPE_ADAPTER);
        map.register(java.sql.Date.class, JAVA_SQL_DATE_TYPE_ADAPTER);
        map.register(Timestamp.class, DATE_TYPE_ADAPTER);
        map.register(Time.class, TIME_TYPE_ADAPTER);
        map.register(Calendar.class, GREGORIAN_CALENDAR_TYPE_ADAPTER);
        map.register(GregorianCalendar.class, GREGORIAN_CALENDAR_TYPE_ADAPTER);
        map.register(BigDecimal.class, BIG_DECIMAL_TYPE_ADAPTER);
        map.register(BigInteger.class, BIG_INTEGER_TYPE_ADAPTER);
        map.register(Boolean.class, BOOLEAN_TYPE_ADAPTER);
        map.register(Boolean.TYPE, BOOLEAN_TYPE_ADAPTER);
        map.register(Byte.class, BYTE_TYPE_ADAPTER);
        map.register(Byte.TYPE, BYTE_TYPE_ADAPTER);
        map.register(Character.class, CHARACTER_TYPE_ADAPTER);
        map.register(Character.TYPE, CHARACTER_TYPE_ADAPTER);
        map.register(Integer.class, INTEGER_TYPE_ADAPTER);
        map.register(Integer.TYPE, INTEGER_TYPE_ADAPTER);
        map.register(Number.class, NUMBER_TYPE_ADAPTER);
        map.register(Short.class, SHORT_TYPE_ADAPTER);
        map.register(Short.TYPE, SHORT_TYPE_ADAPTER);
        map.register(String.class, STRING_TYPE_ADAPTER);
        map.register(StringBuilder.class, STRING_BUILDER_TYPE_ADAPTER);
        map.register(StringBuffer.class, STRING_BUFFER_TYPE_ADAPTER);
        map.makeUnmodifiable();
        return map;
    }

    private static ParameterizedTypeHandlerMap<JsonSerializer<?>> createDefaultHierarchySerializers() {
        ParameterizedTypeHandlerMap<JsonSerializer<?>> map = new ParameterizedTypeHandlerMap();
        map.registerForTypeHierarchy(Enum.class, ENUM_TYPE_ADAPTER);
        map.registerForTypeHierarchy(InetAddress.class, INET_ADDRESS_ADAPTER);
        map.registerForTypeHierarchy(Collection.class, COLLECTION_TYPE_ADAPTER);
        map.registerForTypeHierarchy(Map.class, MAP_TYPE_ADAPTER);
        map.makeUnmodifiable();
        return map;
    }

    private static ParameterizedTypeHandlerMap<JsonDeserializer<?>> createDefaultDeserializers() {
        ParameterizedTypeHandlerMap<JsonDeserializer<?>> map = new ParameterizedTypeHandlerMap();
        map.register(URL.class, wrapDeserializer(URL_TYPE_ADAPTER));
        map.register(URI.class, wrapDeserializer(URI_TYPE_ADAPTER));
        map.register(UUID.class, wrapDeserializer(UUUID_TYPE_ADAPTER));
        map.register(Locale.class, wrapDeserializer(LOCALE_TYPE_ADAPTER));
        map.register(Date.class, wrapDeserializer(DATE_TYPE_ADAPTER));
        map.register(java.sql.Date.class, wrapDeserializer(JAVA_SQL_DATE_TYPE_ADAPTER));
        map.register(Timestamp.class, wrapDeserializer(TIMESTAMP_DESERIALIZER));
        map.register(Time.class, wrapDeserializer(TIME_TYPE_ADAPTER));
        map.register(Calendar.class, GREGORIAN_CALENDAR_TYPE_ADAPTER);
        map.register(GregorianCalendar.class, GREGORIAN_CALENDAR_TYPE_ADAPTER);
        map.register(BigDecimal.class, BIG_DECIMAL_TYPE_ADAPTER);
        map.register(BigInteger.class, BIG_INTEGER_TYPE_ADAPTER);
        map.register(Boolean.class, BOOLEAN_TYPE_ADAPTER);
        map.register(Boolean.TYPE, BOOLEAN_TYPE_ADAPTER);
        map.register(Byte.class, BYTE_TYPE_ADAPTER);
        map.register(Byte.TYPE, BYTE_TYPE_ADAPTER);
        map.register(Character.class, wrapDeserializer(CHARACTER_TYPE_ADAPTER));
        map.register(Character.TYPE, wrapDeserializer(CHARACTER_TYPE_ADAPTER));
        map.register(Double.class, DOUBLE_TYPE_ADAPTER);
        map.register(Double.TYPE, DOUBLE_TYPE_ADAPTER);
        map.register(Float.class, FLOAT_TYPE_ADAPTER);
        map.register(Float.TYPE, FLOAT_TYPE_ADAPTER);
        map.register(Integer.class, INTEGER_TYPE_ADAPTER);
        map.register(Integer.TYPE, INTEGER_TYPE_ADAPTER);
        map.register(Long.class, LONG_DESERIALIZER);
        map.register(Long.TYPE, LONG_DESERIALIZER);
        map.register(Number.class, NUMBER_TYPE_ADAPTER);
        map.register(Short.class, SHORT_TYPE_ADAPTER);
        map.register(Short.TYPE, SHORT_TYPE_ADAPTER);
        map.register(String.class, wrapDeserializer(STRING_TYPE_ADAPTER));
        map.register(StringBuilder.class, wrapDeserializer(STRING_BUILDER_TYPE_ADAPTER));
        map.register(StringBuffer.class, wrapDeserializer(STRING_BUFFER_TYPE_ADAPTER));
        map.makeUnmodifiable();
        return map;
    }

    private static ParameterizedTypeHandlerMap<JsonDeserializer<?>> createDefaultHierarchyDeserializers() {
        ParameterizedTypeHandlerMap<JsonDeserializer<?>> map = new ParameterizedTypeHandlerMap();
        map.registerForTypeHierarchy(Enum.class, wrapDeserializer(ENUM_TYPE_ADAPTER));
        map.registerForTypeHierarchy(InetAddress.class, wrapDeserializer(INET_ADDRESS_ADAPTER));
        map.registerForTypeHierarchy(Collection.class, wrapDeserializer(COLLECTION_TYPE_ADAPTER));
        map.registerForTypeHierarchy(Map.class, wrapDeserializer(MAP_TYPE_ADAPTER));
        map.makeUnmodifiable();
        return map;
    }

    private static ParameterizedTypeHandlerMap<InstanceCreator<?>> createDefaultInstanceCreators() {
        ParameterizedTypeHandlerMap<InstanceCreator<?>> map = new ParameterizedTypeHandlerMap();
        DefaultConstructorAllocator allocator = new DefaultConstructorAllocator(50);
        map.registerForTypeHierarchy(Map.class, new DefaultConstructorCreator(LinkedHashMap.class, allocator));
        DefaultConstructorCreator<List> listCreator = new DefaultConstructorCreator(ArrayList.class, allocator);
        DefaultConstructorCreator<Queue> queueCreator = new DefaultConstructorCreator(LinkedList.class, allocator);
        DefaultConstructorCreator<Set> setCreator = new DefaultConstructorCreator(HashSet.class, allocator);
        DefaultConstructorCreator<SortedSet> sortedSetCreator = new DefaultConstructorCreator(TreeSet.class, allocator);
        map.registerForTypeHierarchy(Collection.class, listCreator);
        map.registerForTypeHierarchy(Queue.class, queueCreator);
        map.registerForTypeHierarchy(Set.class, setCreator);
        map.registerForTypeHierarchy(SortedSet.class, sortedSetCreator);
        map.makeUnmodifiable();
        return map;
    }

    private static JsonDeserializer<?> wrapDeserializer(JsonDeserializer<?> deserializer) {
        return new JsonDeserializerExceptionWrapper(deserializer);
    }

    static ParameterizedTypeHandlerMap<JsonSerializer<?>> getDefaultSerializers() {
        return getDefaultSerializers(false, LongSerializationPolicy.DEFAULT);
    }

    static ParameterizedTypeHandlerMap<JsonSerializer<?>> getAllDefaultSerializers() {
        ParameterizedTypeHandlerMap<JsonSerializer<?>> defaultSerializers = getDefaultSerializers(false, LongSerializationPolicy.DEFAULT);
        defaultSerializers.register(DEFAULT_HIERARCHY_SERIALIZERS);
        return defaultSerializers;
    }

    static ParameterizedTypeHandlerMap<JsonDeserializer<?>> getAllDefaultDeserializers() {
        ParameterizedTypeHandlerMap<JsonDeserializer<?>> defaultDeserializers = getDefaultDeserializers().copyOf();
        defaultDeserializers.register(DEFAULT_HIERARCHY_DESERIALIZERS);
        return defaultDeserializers;
    }

    static ParameterizedTypeHandlerMap<JsonSerializer<?>> getDefaultSerializers(boolean serializeSpecialFloatingPointValues, LongSerializationPolicy longSerializationPolicy) {
        ParameterizedTypeHandlerMap<JsonSerializer<?>> serializers = new ParameterizedTypeHandlerMap();
        DoubleSerializer doubleSerializer = new DoubleSerializer(serializeSpecialFloatingPointValues);
        serializers.registerIfAbsent(Double.class, doubleSerializer);
        serializers.registerIfAbsent(Double.TYPE, doubleSerializer);
        FloatSerializer floatSerializer = new FloatSerializer(serializeSpecialFloatingPointValues);
        serializers.registerIfAbsent(Float.class, floatSerializer);
        serializers.registerIfAbsent(Float.TYPE, floatSerializer);
        LongSerializer longSerializer = new LongSerializer(null);
        serializers.registerIfAbsent(Long.class, longSerializer);
        serializers.registerIfAbsent(Long.TYPE, longSerializer);
        serializers.registerIfAbsent(DEFAULT_SERIALIZERS);
        return serializers;
    }

    static ParameterizedTypeHandlerMap<JsonDeserializer<?>> getDefaultDeserializers() {
        return DEFAULT_DESERIALIZERS;
    }

    static ParameterizedTypeHandlerMap<InstanceCreator<?>> getDefaultInstanceCreators() {
        return DEFAULT_INSTANCE_CREATORS;
    }
}
