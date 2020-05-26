package gramat.parsing.test;

import java.util.Objects;

public class TestString extends TestValue {

    private final String value;

    public TestString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void validate(String parent, TestValue actual) {
        if (!(actual instanceof TestString)) {
            throw new AssertionError(parent + ": expected a string");
        }

        var actualString = ((TestString)actual).value;

        if (!Objects.equals(value, actualString)) {
            throw new AssertionError(parent + ": expected " + value + " instead of " + actualString);
        }
    }
}
