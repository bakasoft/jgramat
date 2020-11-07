package stone.binary.output;

import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;
import stone.binary.translate.Bytes;
import stone.binary.translate.INT32;
import stone.binary.translate.INT64;
import stone.binary.translate.STR;

import java.io.IOException;
import java.io.OutputStream;

public class BinaryWriter {

    private final OutputStream output;

    public BinaryWriter(OutputStream output) {
        this.output = output;
    }

    public void writeString(String value) throws IOException {
        output.write(Bytes.STRING_VALUE);
        var data = STR.bytesOf(value);
        output.write(INT32.bytesOfInt(data.length));
        output.write(data);
    }

    public void writeInteger(int value) throws IOException {
        output.write(Bytes.INTEGER_VALUE);
        output.write(INT32.bytesOfInt(value));
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

    public void writeReference(long reference) throws IOException {
        output.write(Bytes.REFERENCE_VALUE);
        output.write(INT64.bytesOfLong(reference));
    }

    public void writePointer(long reference) throws IOException {
        output.write(Bytes.POINTER_VALUE);
        output.write(INT64.bytesOfLong(reference));
    }

    public void writeObjectBegin(IndexedType type) throws IOException {
        output.write(Bytes.OBJECT_VALUE);
        output.write(INT32.bytesOfInt(type.getIndex()));
    }

    public void writeObjectField(IndexedField field) throws IOException {
        output.write(Bytes.OBJECT_FIELD);
        output.write(INT32.bytesOfInt(field.getIndex()));
    }

    public void writeObjectEnd() throws IOException {
        output.write(Bytes.OBJECT_END);
    }

    public void writeArrayBegin(int length) throws IOException {
        output.write(Bytes.ARRAY_VALUE);
        output.write(INT32.bytesOfInt(length));
    }

    public void writeArrayItem() throws IOException {
        output.write(Bytes.ARRAY_ITEM);
    }

    public void writeArrayEnd() throws IOException {
        output.write(Bytes.ARRAY_END);
    }

}
