package stone.binary.output;

import stone.binary.output.impl.DefaultDescriberRepository;
import stone.binary.output.impl.DisabledReferenceStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Objects;

public class BinarySerializer {

    private final DescriberRepository describers;

    private final ReferenceStrategy references;

    public BinarySerializer() {
        this(new DefaultDescriberRepository());
    }

    public BinarySerializer(DescriberRepository describers) {
        this(describers, new DisabledReferenceStrategy());
    }

    public BinarySerializer(DescriberRepository describers, ReferenceStrategy references) {
        this.describers = Objects.requireNonNull(describers);
        this.references = Objects.requireNonNull(references);
    }

    public void write(Object any, OutputStream output) throws IOException{
        write(any, new BinaryWriter(output));
    }

    public void write(Object any, BinaryWriter writer)  throws IOException {
        if (references.accepts(any)) {
            var ref = references.lookUp(any);

            if (ref.isPresent()) {
                writer.writePointer(ref.getAsLong());
                return;
            }
            else {
                writer.writeReference(references.save(any));
            }
        }

        if (any == null) {
            writer.writeNull();
        }
        else if (any instanceof String) {
            writer.writeString((String)any);
        }
        else if (any instanceof Integer) {
            writer.writeInteger((Integer)any);
        }
        else if (any instanceof Boolean) {
            writer.writeBoolean((Boolean)any);
        }
        else if (any instanceof Collection) {
            writeList((Collection<?>)any, writer);
        }
        else {
            writeObject(any, writer);
        }
    }

    private void writeObject(Object obj, BinaryWriter writer) throws IOException {
        var describer = describers.findDescriberFor(obj);

        writer.writeObjectBegin(describer.getTypeIndex());

        for (var field : describer.getFields()) {
            var value = describer.getValue(obj, field);

            writer.writeObjectField(field);
            write(value, writer);
        }

        writer.writeObjectEnd();
    }

    private void writeList(Collection<?> items, BinaryWriter writer) throws IOException {
        writer.writeArrayBegin(items.size());

        for (var item : items) {
            writer.writeArrayItem();
            write(item, writer);
        }

        writer.writeArrayEnd();
    }

}
