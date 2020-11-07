package stone.binary.output.impl;

import stone.binary.output.ObjectDescriber;
import stone.binary.schema.IndexedField;
import stone.binary.schema.IndexedType;
import stone.binary.schema.impl.DefaultIndexedField;
import stone.binary.schema.impl.DefaultIndexedType;

import java.util.function.Function;

public class DescriberBuilder<T> {

    private final DefaultObjectDescriber<T> product;

    DescriberBuilder(Class<T> typeClass) {
        this.product = new DefaultObjectDescriber<>(typeClass);
    }

    public DescriberBuilder<T> withTypeIndex(int typeIndex) {
        return withTypeIndex(new DefaultIndexedType(typeIndex));
    }

    public DescriberBuilder<T> withTypeIndex(IndexedType typeIndex) {
        product.setTypeIndex(typeIndex);
        return this;
    }

    public DescriberBuilder<T> withGetter(int fieldIndex, Function<T, Object> getter) {
        return withGetter(new DefaultIndexedField(fieldIndex), getter);
    }

    public DescriberBuilder<T> withGetter(IndexedField fieldIndex, Function<T, Object> getter) {
        product.addField(fieldIndex, getter);
        return this;
    }

    public ObjectDescriber build() {
        return product;
    }

}
