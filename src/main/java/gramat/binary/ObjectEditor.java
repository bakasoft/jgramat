package gramat.binary;

import java.util.Collection;

public interface ObjectEditor {

    IndexedType getType();

    Object newInstance();

    void setValue(Object object, IndexedField field, Value value);

    Value getValue(Object object, IndexedField field);

    Collection<IndexedField> getFields();

    default IndexedField findField(int index) {
        for (var field : getFields()) {
            if (field.getIndex() == index) {
                return field;
            }
        }

        throw new RuntimeException();
    }
}
