package stone.binary.input.impl;

import gramat.util.ReflectionUtils;
import stone.binary.input.ObjectCreator;
import stone.binary.input.Value;
import stone.binary.schema.IndexedField;
import stone.binary.schema.impl.DefaultIndexedField;
import stone.binary.schema.impl.DefaultIndexedType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CreatorBuilder<T> {

    private final DefaultObjectCreator<T> product;

    CreatorBuilder(Class<T> typeClass) {
        this.product = new DefaultObjectCreator<>(typeClass);
    }

    public CreatorBuilder<T> withTypeIndex(int typeIndex) {
        // TODO implement type repositories
        return withTypeIndex(new DefaultIndexedType(typeIndex));
    }

    public CreatorBuilder<T> withTypeIndex(DefaultIndexedType typeIndex) {
        product.setTypeIndex(typeIndex);
        return this;
    }

    public CreatorBuilder<T> withInstantiator(Supplier<T> instantiator) {
        product.setInstantiator(instantiator);
        return this;
    }

    public CreatorBuilder<T> withSetter(int fieldIndex, BiConsumer<T, Value> setter) {
        return withSetter(new DefaultIndexedField(fieldIndex), setter);
    }

    public CreatorBuilder<T> withSetter(IndexedField field, BiConsumer<T, Value> setter) {
        product.addField(field, setter);
        return this;
    }

    public ObjectCreator build() {
        if (product.getInstantiator() == null) {
            product.setInstantiator(ReflectionUtils.emptyConstructor(product.getTypeClass()));
        }
        return product;
    }
}
