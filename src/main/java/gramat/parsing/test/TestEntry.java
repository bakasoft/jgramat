package gramat.parsing.test;

import java.util.Objects;

public class TestEntry extends TestValue {

    public final String name;
    public final TestValue value;

    public TestEntry(String name, TestValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    protected void validate(String parent, TestValue actual) {
        if (!(actual instanceof TestEntry)) {
            throw new AssertionError(parent + ": expected entry");
        }

        var actualEntry = (TestEntry) actual;

        if (!Objects.equals(name, actualEntry.name)) {
            throw new AssertionError(parent + ": expected key " + name + " instead of " + actualEntry.name);
        }

        value.validate(parent + "/" + name, actualEntry.value);
    }
}
