package gramat.binary;

import gramat.binary.values.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BinaryReader {

    private final InputStream input;
    private final List<ObjectEditor> editors;

    public BinaryReader(InputStream input) {
        this.input = input;
        this.editors = new ArrayList<>();
    }

    public void addEditor(ObjectEditor editor) {
        editors.add(editor);
    }

    public List<ObjectEditor> getEditors() {
        return Collections.unmodifiableList(editors);
    }

    public Value readValue() throws IOException {
        int type = input.read();

        if (type == -1) {
            throw new IOException();
        }
        else if (type == Bytes.NULL_VALUE) {
            return NullValue.INSTANCE;
        }
        else if (type == Bytes.INTEGER_VALUE) {
            return new IntegerValue(readRawInt(input));
        }
        else if (type == Bytes.TRUE_VALUE) {
            return new BooleanValue(true);
        }
        else if (type == Bytes.FALSE_VALUE) {
            return new BooleanValue(false);
        }
        else if (type == Bytes.STRING_VALUE) {
            return new StringValue(readRawString(input));
        }
        else if (type == Bytes.ARRAY_VALUE) {
            var length = readRawInt(input);
            var array = new Value[length];
            var i = 0;

            for (i = 0; i < length; i++) {
                var b = input.read();

                if (b == -1) {
                    throw new IOException();
                }
                else if (b == Bytes.ARRAY_ITEM) {
                    array[i] = readValue();
                }
                else if (b == Bytes.ARRAY_END) {
                    break;
                }
                else {
                    throw new IOException();
                }
            }

            if (i != length) {
                throw new IOException();
            }

            return new ListValue(List.of(array));
        }
        else if (type == Bytes.OBJECT_VALUE) {
            var typeIndex = readRawInt(input);
            var editor = findEditorFor(typeIndex);
            var obj = editor.newInstance();

            while (true) {
                var b = input.read();

                if (b == -1) {
                    throw new IOException();
                }
                else if (b == Bytes.OBJECT_FIELD) {
                    var fieldIndex = readRawInt(input);
                    var fieldItem = editor.findField(fieldIndex);
                    var fieldValue = readValue();

                    editor.setValue(obj, fieldItem, fieldValue);
                }
                else if (b == Bytes.OBJECT_END) {
                    break;
                }
                else {
                    throw new IOException();
                }
            }

            return new ObjectValue(obj);
        }
        else {
            throw new IOException();
        }
    }

    private ObjectEditor findEditorFor(int typeIndex) {
        for (var editor : editors) {
            if (editor.getType().getIndex() == typeIndex) {
                return editor;
            }
        }

        throw new RuntimeException();
    }

    public static int readRawInt(InputStream input) throws IOException {
        var b0 = input.read();

        if (b0 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 7) == 0b00000000) {
            return b0;
        }

        var b1 = input.read();

        if (b1 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 6) == 0b00000010) {
            return ((b0 ^ Bytes.MASK_1B) << 8) | b1;
        }

        var b2 = input.read();
        if (b2 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 5) == 0b00000110) {
            return ((b0 ^ Bytes.MASK_2B) << 16) | (b1 << 8) | b2;
        }

        var b3 = input.read();
        if (b3 == -1) {
            throw new RuntimeException();
        }

        if ((b0 >>> 4) == 0b00001110) {
            return ((b0 ^ Bytes.MASK_3B) << 24) | (b1 << 16) | (b2 << 8) | b3;
        }

        var b4 = input.read();
        if (b4 == -1) {
            throw new RuntimeException();
        }
        else if ((b0 >>> 3) == 0b00011110) {
            return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
        }
        else {
            throw new IOException();
        }
    }

    public static String readRawString(InputStream input) throws IOException {
        int length = readRawInt(input);
        var data = input.readNBytes(length);
        return new String(data, StandardCharsets.UTF_8);
    }

}
