package gramat.parsing.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class TestMap extends TestValue {

    private final ArrayList<TestEntry> entries;

    public TestMap() {
        entries = new ArrayList<>();
    }

    public void set(String name, TestValue value) {
        for (var entry : entries) {
            if (Objects.equals(entry.name, name)) {
                throw new RuntimeException("already defined: " + name);
            }
        }

        entries.add(new TestEntry(name, value));
    }

    @Override
    protected void validate(String parent, TestValue actualValue) {
        if (!(actualValue instanceof TestMap)) {
            throw new AssertionError(parent + ": expected a map");
        }
        var actual = ((TestMap) actualValue).entries;
        int expectedSize = entries.size();
        int actualSize = actual.size();
        int minSize = Math.min(expectedSize, actualSize);

        for (int i = 0; i < minSize; i++) {
            var expectedItem = entries.get(i);
            var actualItem = actual.get(i);

            expectedItem.validate(parent + "/" + i, actualItem);
        }

        if (expectedSize != actualSize) {
            throw new AssertionError(parent + ": expected map size " + expectedSize + " instead of " + actualSize);
        }
    }

}
