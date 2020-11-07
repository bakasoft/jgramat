package stone.producers;

import stone.common.GenericObject;
import stone.errors.StoneException;

public class GenericObjectProducer implements ObjectProducer {

    @Override
    public Object newInstance(String type) {
        return new GenericObject(type);
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof GenericObject) {
            ((GenericObject) obj).set(key, value);
        }
        else {
            throw new StoneException();
        }
    }
}
