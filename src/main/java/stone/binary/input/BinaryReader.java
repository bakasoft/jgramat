package stone.binary.input;

import stone.binary.input.impl.DefaultCreatorRepository;
import stone.binary.translate.Bytes;
import stone.binary.translate.INT32;
import stone.binary.translate.INT64;
import stone.binary.translate.STR;

import java.io.IOException;
import java.io.InputStream;

public class BinaryReader {

    private final InputStream input;
    private final CreatorRepository creators;

    public BinaryReader(InputStream input) {
        this.input = input;
        this.creators = new DefaultCreatorRepository();
    }

    public int readValueMark() throws IOException {
        int mark = input.read();

        if (mark == -1) {
            throw new IOException();
        }

        var invalid = true;

        for (var allowed : Bytes.ALLOWED_VALUES) {
            if (mark == allowed) {
                invalid = false;
                break;
            }
        }

        if (invalid) {
            throw new RuntimeException("invalid value mark: " + mark);
        }

        return mark;
    }

    public int readInt32Content() throws IOException {
        return INT32.readRawInt(input);
    }

    public String readStringContent() throws IOException {
        return STR.readRawString(input);
    }

    public boolean readArrayItem() throws IOException {
        var b = input.read();

        if (b == -1) {
            throw new IOException();
        }
        else if (b == Bytes.ARRAY_ITEM) {
            return true;
        }
        else if (b == Bytes.ARRAY_END) {
            return false;
        }
        else {
            throw new IOException("expected array item or array end");
        }
    }

    public boolean readObjectField() throws IOException {
        var b = input.read();

        if (b == -1) {
            throw new IOException();
        }
        else if (b == Bytes.OBJECT_FIELD) {
            return true;
        }
        else if (b == Bytes.OBJECT_END) {
            return false;
        }
        else {
            throw new IOException("expected object field or object end");
        }
    }

    public long readInt64Content() throws IOException {
        return INT64.readRawLong(input);
    }
}
