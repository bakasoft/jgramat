package gramat.parsing.test;

import gramat.util.ClassUtils;

import java.util.List;
import java.util.Map;

abstract public class TestValue {

    protected static TestValue of(Object value) {
        if (value instanceof String) {
            return new TestString((String)value);
        }
        if (value instanceof Number) {
            return new TestString(value.toString());
        }
        else if (value instanceof Map) {
            var map = new TestMap();

            for (var entry : ((Map<?,?>)value).entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new RuntimeException();
                }

                String key = (String)entry.getKey();

                map.set(key, TestValue.of(entry.getValue()));
            }

            return map;
        }
        else if (value instanceof List) {
            var list = new TestList();

            for (var item : ((List<?>)value)) {
                list.add(TestValue.of(item));
            }

            return list;
        }
        else {
            throw new RuntimeException("unsupported value: " + ClassUtils.prettyType(value));
        }
    }

    public void validate(Object actual) {
        TestValue actualValue;

        if (actual instanceof TestValue) {
            actualValue = (TestValue) actual;
        }
        else {
            actualValue = TestValue.of(actual);
        }

        validate("", actualValue);
    }

    protected abstract void validate(String parent, TestValue actual);
}
