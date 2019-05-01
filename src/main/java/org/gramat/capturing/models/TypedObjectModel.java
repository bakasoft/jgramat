package org.gramat.capturing.models;

import org.gramat.capturing.ObjectModel;
import org.bakasoft.polysynth.schemas.ArraySchema;
import org.bakasoft.polysynth.schemas.ObjectSchema;

public class TypedObjectModel implements ObjectModel {

    private final ObjectSchema schema;
    private final Object instance;

    public TypedObjectModel(ObjectSchema schema) {
        this.schema = schema;
        this.instance = schema.createEmpty();

    }

    public Object getInstance() {
        return instance;
    }

    public void setValue(String name, Object value) {
        schema.set(instance, name, value);
    }

    public void addValue(String name, Object value) {
        ArraySchema propertySchema = schema.getSchema(name).toArray();
        Object propertyValue = schema.get(instance, name);

        if (propertyValue == null) {
            propertyValue = propertySchema.createEmpty();

            schema.set(instance, name, propertyValue);
        }

        propertySchema.add(propertyValue, value);
    }

    @Override
    public void addValue(Object value) {
        throw new RuntimeException("can't add item to a non-collection instance");
    }
}
