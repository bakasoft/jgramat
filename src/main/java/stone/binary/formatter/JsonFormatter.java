package stone.binary.formatter;

import gramat.exceptions.UnsupportedValueException;
import gramat.util.PP;
import stone.binary.input.*;
import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;
import stone.binary.schema.impl.DefaultIndexedField;
import stone.binary.schema.impl.DefaultIndexedType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonFormatter {

    public static void write(InputStream input, OutputStream output) throws IOException {
        var creators = new JsonCreatorRepository();
        var references = new JsonReferenceRepository();
        var deserializer = new BinaryDeserializer(creators, references);
        var value = deserializer.readValue(input);

        printValue(value, output);
    }

    private static void print(OutputStream output, String ascii) throws IOException {
        for (var chr : ascii.toCharArray()) {
            if (chr <= 0b01111111) {
                output.write(chr);
            }
            else {
                throw new RuntimeException();
            }
        }
    }

    private static void printValue(Value value, OutputStream output) throws IOException {
        if (value instanceof JsonReference) {
            printRef((JsonReference)value, output);
        }
        else if (value.getType() == ValueType.OBJECT) {
            printObject(value.asObject(JsonObject.class), output);
        }
        else if (value.getType() == ValueType.LIST) {
            printArray(value.asList(), output);
        }
        else if (value.isNull()) {
            printNull(output);
        }
        else if (value.getType() == ValueType.BOOLEAN) {
            printBoolean(value.asBoolean(), output);
        }
        else if (value.getType() == ValueType.INTEGER) {
            printInt(value.asInt(), output);
        }
        else if (value.getType() == ValueType.STRING) {
            printString(value.asString(), output);
        }
        else {
            throw new UnsupportedValueException(value);
        }
    }

    private static void printString(String value, OutputStream output) throws IOException {
        // TODO implement real format
        print(output, PP.str(value));
    }

    private static void printNull(OutputStream output) throws IOException {
        print(output, "null");
    }

    private static void printInt(int value, OutputStream output) throws IOException {
        print(output, String.valueOf(value));
    }

    private static void printBoolean(boolean value, OutputStream output) throws IOException {
        print(output, value ? "true" : "false");
    }

    private static void printArray(List<Value> items, OutputStream output) throws IOException {
        print(output, "[");
        for (var i = 0; i < items.size(); i++) {
            if (i > 0) {
                print(output, ",");
            }

            printValue(items.get(i), output);
        }
        print(output, "]");
    }

    private static void printObject(JsonObject obj, OutputStream output) throws IOException {
        print(output, "{");
        int i = 0;
        for (var entry : obj.values.entrySet()) {
            if (i > 0) {
                print(output, ",");
            }

            printString(entry.getKey().toString(), output);
            print(output, ":");
            printValue(entry.getValue(), output);
            i++;
        }
        print(output, "}");
    }

    private static void printRef(JsonReference ref, OutputStream output) throws IOException {
        print(output, String.format("{\"@ref\":%s}", ref.reference));
    }

    private static class JsonObject {

        public final Map<IndexedField, Value> values;

        public JsonObject() {
            values = new LinkedHashMap<>();
        }

        public void setValue(IndexedField field, Value value) {
            values.put(field, value);
        }
    }

    private static class JsonCreator implements ObjectCreator {

        private final IndexedType typeIndex;
        private final Map<Integer, IndexedField> fields;

        private JsonCreator(int index) {
            this.typeIndex = new DefaultIndexedType(index);
            this.fields = new LinkedHashMap<>();
        }

        @Override
        public IndexedType getTypeIndex() {
            return typeIndex;
        }

        @Override
        public Object newInstance() {
            return new JsonObject();
        }

        @Override
        public IndexedField findField(int index) {
            return fields.computeIfAbsent(index, DefaultIndexedField::new);
        }

        @Override
        public void setValue(Object object, IndexedField field, Value value) {
            var jsonObj = (JsonObject)object;

            jsonObj.setValue(field, value);
        }
    }

    private static class JsonCreatorRepository implements CreatorRepository {

        private final Map<Integer, JsonCreator> creators;

        public JsonCreatorRepository() {
            this.creators = new LinkedHashMap<>();
        }

        @Override
        public ObjectCreator findCreatorFor(int typeIndex) {
            return creators.computeIfAbsent(typeIndex, JsonCreator::new);
        }

    }

    private static class JsonReference implements Value {

        public final long reference;

        public JsonReference(long reference) {
            this.reference = reference;
        }

        @Override
        public ValueType getType() {
            return ValueType.REF;
        }

        @Override
        public Object getValue() {
            return reference;
        }

    }

    private static class JsonReferenceRepository implements ReferenceRepository {

        @Override
        public Value get(long reference) {
            return new JsonReference(reference);
        }

        @Override
        public boolean contains(long reference) {
            return false;
        }

        @Override
        public void set(long reference, Value value) {
            // do nothing
        }
    }

}
