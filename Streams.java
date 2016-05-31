package com.google.gson;

import android.support.v4.view.MotionEventCompat;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.omgm.easytaxi.services.mappmodel.LocalServiceBean;
import java.io.IOException;
import java.io.Writer;
import java.util.Map.Entry;

final class Streams {

    /* renamed from: com.google.gson.Streams.1 */
    static /* synthetic */ class C00091 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken;

        static {
            $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.END_ARRAY.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    private static class AppendableWriter extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;

        static class CurrentWrite implements CharSequence {
            char[] chars;

            CurrentWrite() {
            }

            public int length() {
                return this.chars.length;
            }

            public char charAt(int i) {
                return this.chars[i];
            }

            public CharSequence subSequence(int start, int end) {
                return new String(this.chars, start, end - start);
            }
        }

        private AppendableWriter(Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }

        public void write(char[] chars, int offset, int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append(this.currentWrite, offset, offset + length);
        }

        public void write(int i) throws IOException {
            this.appendable.append((char) i);
        }

        public void flush() {
        }

        public void close() {
        }
    }

    Streams() {
    }

    static JsonElement parse(JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return parseRecursive(reader);
        } catch (Throwable e) {
            if (isEmpty) {
                return JsonNull.createJsonNull();
            }
            throw new JsonIOException(e);
        } catch (Throwable e2) {
            throw new JsonSyntaxException(e2);
        } catch (Throwable e22) {
            throw new JsonIOException(e22);
        } catch (Throwable e222) {
            throw new JsonSyntaxException(e222);
        }
    }

    private static JsonElement parseRecursive(JsonReader reader) throws IOException {
        switch (C00091.$SwitchMap$com$google$gson$stream$JsonToken[reader.peek().ordinal()]) {
            case LocalServiceBean.SERVICE_TAXI /*1*/:
                return new JsonPrimitive(reader.nextString());
            case LocalServiceBean.SERVICE_LIMO /*2*/:
                return new JsonPrimitive(JsonPrimitive.stringToNumber(reader.nextString()));
            case LocalServiceBean.SERVICE_DRINK_N_DRIVE /*3*/:
                return new JsonPrimitive(Boolean.valueOf(reader.nextBoolean()));
            case FragmentManagerImpl.ANIM_STYLE_CLOSE_EXIT /*4*/:
                reader.nextNull();
                return JsonNull.createJsonNull();
            case MotionEventCompat.ACTION_POINTER_DOWN /*5*/:
                JsonElement array = new JsonArray();
                reader.beginArray();
                while (reader.hasNext()) {
                    array.add(parseRecursive(reader));
                }
                reader.endArray();
                return array;
            case MotionEventCompat.ACTION_POINTER_UP /*6*/:
                JsonElement object = new JsonObject();
                reader.beginObject();
                while (reader.hasNext()) {
                    object.add(reader.nextName(), parseRecursive(reader));
                }
                reader.endObject();
                return object;
            default:
                throw new IllegalArgumentException();
        }
    }

    static void write(JsonElement element, boolean serializeNulls, JsonWriter writer) throws IOException {
        if (element == null || element.isJsonNull()) {
            if (serializeNulls) {
                writer.nullValue();
            }
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                writer.value(primitive.getAsNumber());
            } else if (primitive.isBoolean()) {
                writer.value(primitive.getAsBoolean());
            } else {
                writer.value(primitive.getAsString());
            }
        } else if (element.isJsonArray()) {
            writer.beginArray();
            i$ = element.getAsJsonArray().iterator();
            while (i$.hasNext()) {
                JsonElement e = (JsonElement) i$.next();
                if (e.isJsonNull()) {
                    writer.nullValue();
                } else {
                    write(e, serializeNulls, writer);
                }
            }
            writer.endArray();
        } else if (element.isJsonObject()) {
            writer.beginObject();
            for (Entry<String, JsonElement> e2 : element.getAsJsonObject().entrySet()) {
                JsonElement value = (JsonElement) e2.getValue();
                if (serializeNulls || !value.isJsonNull()) {
                    writer.name((String) e2.getKey());
                    write(value, serializeNulls, writer);
                }
            }
            writer.endObject();
        } else {
            throw new IllegalArgumentException("Couldn't write " + element.getClass());
        }
    }

    static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer) appendable : new AppendableWriter(null);
    }
}
