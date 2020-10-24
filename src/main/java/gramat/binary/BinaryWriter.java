package gramat.binary;

import gramat.exceptions.UnsupportedValueException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class BinaryWriter {

    private final OutputStream output;
    private final Map<Class<?>, ObjectEditor> editors;

    public BinaryWriter(OutputStream output) {
        this.output = output;
        this.editors = new LinkedHashMap<>();
    }

    public void addEditor(Class<?> type, ObjectEditor editor) {
        editors.put(type, editor);
    }

    public ObjectEditor getEditor(Class<?> type) {
        return editors.get(type);
    }

    public ObjectEditor findEditorFor(Object obj) {
        for (var entry : editors.entrySet()) {
            if (entry.getKey().isInstance(obj)) {
                return entry.getValue();
            }
        }

        throw new RuntimeException("No encoder found for " + obj);
    }

    public void writeString(String value) throws IOException {
        output.write(Bytes.STRING_VALUE);
        var data = Bytes.bytesOf(value);
        output.write(Bytes.bytesOf(data.length));
        output.write(data);
    }

    public void writeInteger(int value) throws IOException {
        output.write(Bytes.INTEGER_VALUE);
        output.write(Bytes.bytesOf(value));
    }

    public void writeBoolean(boolean value) throws IOException {
        if (value) {
            output.write(Bytes.TRUE_VALUE);
        }
        else {
            output.write(Bytes.FALSE_VALUE);
        }
    }

    public void writeNull() throws IOException {
        output.write(Bytes.NULL_VALUE);
    }

    public void writeValue(Value value) throws IOException {
        switch (value.getType()) {
            case NULL:
                writeNull();
                break;
            case STRING:
                writeString(value.asString());
                break;
            case INTEGER:
                writeInteger(value.asInt());
                break;
            case BOOLEAN:
                writeBoolean(value.asBoolean());
                break;
            case OBJECT:
                writeObject(value.getValue());
                break;
            case LIST:
                wirteList(value.asList());
                break;
            default:
                throw new UnsupportedValueException(value);
        }
    }

    public void writeObject(Object obj) throws IOException {
        var editor = findEditorFor(obj);

        writeObject(obj, editor);
    }

    public <T> void writeObject(T obj, ObjectEditor editor) throws IOException {
        writeObjectBegin(editor.getType());

        for (var field : editor.getFields()) {
            var value = editor.getValue(obj, field);

            writeObjectField(field, value);
        }

        writeObjectEnd();
    }

    public void writeObjectBegin(IndexedType type) throws IOException {
        output.write(Bytes.OBJECT_VALUE);
        output.write(Bytes.bytesOf(type.getIndex()));
    }

    public void writeObjectField(IndexedField field, Value value) throws IOException {
        output.write(Bytes.OBJECT_FIELD);
        output.write(Bytes.bytesOf(field.getIndex()));
        writeValue(value);
    }

    public void writeObjectEnd() throws IOException {
        output.write(Bytes.OBJECT_END);
    }

    public void wirteList(Collection<Value> values) throws IOException {
        writeArrayBegin(values.size());

        for (var value : values) {
            writeArrayItem(value);
        }

        writeArrayEnd();
    }

    public void writeArrayBegin(int length) throws IOException {
        output.write(Bytes.ARRAY_VALUE);
        output.write(Bytes.bytesOf(length));
    }

    public void writeArrayItem(Value value) throws IOException {
        output.write(Bytes.ARRAY_ITEM);

        writeValue(value);
    }

    public void writeArrayEnd() throws IOException {
        output.write(Bytes.ARRAY_END);
    }

}
