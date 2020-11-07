package gramat.data;

import java.util.Objects;

public class Comparer {

    public static void assertEquals(Object actual, Object expected) {
        if (expected == null) {
            assertNull(actual);
        }
        else if (expected instanceof String) {
            assertStringEquals(actual, (String)expected);
        }
        else if (expected instanceof MapData) {
            assertMapEquals(actual, (MapData)expected);
        }
        else {
            throw new RuntimeException("expected: " + expected);
        }
    }

    private static void assertMapEquals(Object actual, MapData expected) {
        if (actual instanceof MapData) {
            assertMapEquals((MapData)actual, expected);
        }
        else {
            throw new RuntimeException("expected: " + expected);
        }
    }

    private static void assertMapEquals(MapData actual, MapData expected) {
        for (var actualEntry : actual.entrySet()) {
            var key = actualEntry.getKey();
            var actualValue = actualEntry.getValue();
            var expectedValue = expected.get(key);

            assertEquals(actualValue, expectedValue);
        }

        for (var expectedEntry : expected.entrySet()) {
            var key = expectedEntry.getKey();
            var expectedValue = expectedEntry.getValue();
            var actualValue = actual.get(key);

            assertEquals(actualValue, expectedValue);
        }

        if (!Objects.equals(actual.getTypeHint(), expected.getTypeHint())) {
            throw new RuntimeException("expected type: " + expected.getTypeHint());
        }
    }

    private static void assertStringEquals(Object actual, String expected) {
        if (actual instanceof String) {
            assertStringEquals((String)actual, expected);
        }
        else {
            throw new RuntimeException("expected: " + expected);
        }
    }

    private static void assertStringEquals(String actual, String expected) {
        if (!Objects.equals(actual, expected)) {
            throw new RuntimeException("expected: " + expected);
        }
    }

    private static void assertNull(Object expected) {
        if (expected != null) {
            throw new RuntimeException("expected: null");
        }
    }

}
