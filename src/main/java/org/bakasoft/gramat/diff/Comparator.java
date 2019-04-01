package org.bakasoft.gramat.diff;

import java.util.*;
import java.util.function.Function;

public class Comparator {

    // TODO implement strict mode and diff List-Array

    private final Function<Object, ?> normalizer;

    public Comparator() {
        this.normalizer = null;
    }

    public Comparator(Function<Object, ?> normalizer) {
        this.normalizer = normalizer;
    }

    public Diff diff(Object expected, Object actual) {
        Object normalizedExpected;
        Object normalizedActual;

        if (normalizer != null) {
            normalizedExpected = normalizer.apply(expected);
            normalizedActual = normalizer.apply(actual);
        }
        else {
            normalizedExpected = expected;
            normalizedActual = actual;
        }

        if (Objects.equals(expected, actual)) {
            return null;
        }
        else if (normalizedExpected instanceof String && normalizedActual instanceof String) {
            return diffString((String)normalizedExpected, (String)normalizedActual);
        }
        else if (normalizedExpected instanceof List && normalizedActual instanceof List) {
            return diffList((List<?>)normalizedExpected, (List<?>)normalizedActual);
        }
        else if (normalizedExpected instanceof Map && normalizedActual instanceof Map) {
            return diffMap((Map<?,?>)normalizedExpected, (Map<?,?>) normalizedActual);
        }

        return new DiffValue(expected, actual);
    }

    public DiffValue diffString(String expected, String actual) {
        if (Objects.equals(expected, actual)) {
            return null;
        }

        return new DiffValue(expected, actual);
    }

    public Diff diffList(List<?> expected, List<?> actual) {
        if (expected == null && actual == null) {
            return null;
        }
        else if (expected == null || actual == null) {
            return new DiffValue(expected, actual);
        }

        int minSize = Math.min(expected.size(), actual.size());

        for (int i = 0; i < minSize; i++) {
            Object expectedValue = expected.get(i);
            Object actualValue = actual.get(i);
            Diff diff = diff(expectedValue, actualValue);

            if (diff != null) {
                return new DiffList(i, diff);
            }
        }

        if (expected.size() != actual.size()) {
            return new DiffSize(expected.size(), actual.size());
        }

        return null;
    }

    public DiffObject diffMap(Map<?,?> expected, Map<?,?> actual) {
        ArrayList<DiffProperty> properties = new ArrayList<>();

        Set<?> expectedKeys = expected.keySet();

        for (Object expectedKey : expectedKeys) {
            Object expectedValue = expected.get(expectedKey);
            Object actualValue = actual.get(expectedKey);
            Diff item = diff(expectedValue, actualValue);

            if (item != null) {
                properties.add(new DiffProperty(expectedKey, item));
            }
        }

        Set<?> actualKeys = actual.keySet();

        for (Object actualKey : actualKeys) {
            if (!expectedKeys.contains(actualKey)) {
                Object expectedValue = expected.get(actualKey);
                Object actualValue = actual.get(actualKey);
                Diff item = diff(expectedValue, actualValue);

                if (item != null) {
                    properties.add(new DiffProperty(actualKey, item));
                }
            }
        }

        if (properties.isEmpty()) {
            return null;
        }

        return new DiffObject(properties.toArray(new DiffProperty[0]));
    }

}
