package stone.binary.input;

import gramat.util.PP;
import stone.binary.input.impl.DefaultReferenceRepository;
import stone.binary.input.values.*;
import stone.binary.translate.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BinaryDeserializer {

    private final CreatorRepository creators;
    private final ReferenceRepository references;

    public BinaryDeserializer(CreatorRepository creators) {
        this(creators, new DefaultReferenceRepository());
    }

    public BinaryDeserializer(CreatorRepository creators, ReferenceRepository references) {
        this.creators = creators;
        this.references = references;
    }

    public Value readValue(InputStream input) throws IOException {
        return readValue(new BinaryReader(input));
    }

    private Value readValue(BinaryReader reader) throws IOException {
        var mark = reader.readValueMark();

        if (mark == Bytes.NULL_VALUE) {
            return NullValue.INSTANCE;
        }
        else if (mark == Bytes.INTEGER_VALUE) {
            return new IntegerValue(reader.readInt32Content());
        }
        else if (mark == Bytes.TRUE_VALUE) {
            return new BooleanValue(true);
        }
        else if (mark == Bytes.FALSE_VALUE) {
            return new BooleanValue(false);
        }
        else if (mark == Bytes.STRING_VALUE) {
            return new StringValue(reader.readStringContent());
        }
        else if (mark == Bytes.REFERENCE_VALUE) {
            return readReferenceContent(reader);
        }
        else if (mark == Bytes.POINTER_VALUE) {
            return readPointerContent(reader);
        }
        else if (mark == Bytes.ARRAY_VALUE) {
            return readArrayContent(reader);
        }
        else if (mark == Bytes.OBJECT_VALUE) {
            return readObjectContent(reader);
        }
        else {
            throw new IllegalStateException(String.format("Not implemented mark: %s", PP.hex(mark)));
        }
    }

    private Value readPointerContent(BinaryReader reader) throws IOException {
        var reference = reader.readInt64Content();
        var value = references.get(reference);

        if (value == null) {
            throw new RuntimeException();
        }

        return value;
    }

    private Value readReferenceContent(BinaryReader reader) throws IOException {
        var reference = reader.readInt64Content();
        var value = readValue(reader);

        if (references.contains(reference)) {
            throw new RuntimeException();
        }

        references.set(reference, value);

        return value;
    }

    private Value readArrayContent(BinaryReader reader) throws IOException {
        var length = reader.readInt32Content();
        var array = new Value[length];
        var i = 0;

        while (reader.readArrayItem() && i < length) {
            array[i] = readValue(reader);
            i++;
        }

        if (i != length) {
            throw new IOException();
        }

        return new ListValue(List.of(array));
    }

    private Value readObjectContent(BinaryReader reader) throws IOException {
        var typeIndex = reader.readInt32Content();
        var creator = creators.findCreatorFor(typeIndex);

        try {
            var obj = creator.newInstance();

            while (reader.readObjectField()) {
                var fieldIndex = reader.readInt32Content();
                var fieldItem = creator.findField(fieldIndex);

                try {
                    var fieldValue = readValue(reader);

                    creator.setValue(obj, fieldItem, fieldValue);
                }
                catch (Exception e) {  // TODO be more specific
                    throw new RuntimeException(String.format("%s: %s", fieldItem, e.getMessage()), e);
                }
            }

            return new ObjectValue(obj);
        }
        catch (Exception e) {  // TODO be more specific
            throw new RuntimeException(String.format("%s: %s", creator.getTypeIndex(), e.getMessage()), e);
        }
    }
}
