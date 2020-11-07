package stone.binary.output;

import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;

import java.util.Collection;

public interface ObjectDescriber {
    boolean accepts(Object any);

    IndexedType getTypeIndex();

    Collection<IndexedField> getFields();

    Object getValue(Object obj, IndexedField field);
}
