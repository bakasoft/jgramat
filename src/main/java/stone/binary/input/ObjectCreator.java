package stone.binary.input;

import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;

public interface ObjectCreator {
    IndexedType getTypeIndex();

    Object newInstance();

    IndexedField findField(int index);

    void setValue(Object object, IndexedField field, Value value);
}
