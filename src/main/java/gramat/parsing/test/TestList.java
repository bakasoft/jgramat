package gramat.parsing.test;

import java.util.ArrayList;

public class TestList extends TestValue {

    private final ArrayList<TestValue> list;

    public TestList() {
        list = new ArrayList<>();
    }

    public void add(TestValue value) {
        list.add(value);
    }

    @Override
    protected void validate(String parent, TestValue actualValue) {
        if (!(actualValue instanceof TestList)) {
            throw new AssertionError(parent + ": expected a list");
        }
        var actual = ((TestList) actualValue).list;
        int expectedSize = list.size();
        int actualSize = actual.size();
        int minSize = Math.min(expectedSize, actualSize);

        for (int i = 0; i < minSize; i++) {
            var expectedItem = list.get(i);
            var actualItem = actual.get(i);

            expectedItem.validate(parent + "/" + i, actualItem);
        }

        if (expectedSize != actualSize) {
            throw new AssertionError(parent + ": expected list size " + expectedSize + " instead of " + actualSize);
        }
    }
}
