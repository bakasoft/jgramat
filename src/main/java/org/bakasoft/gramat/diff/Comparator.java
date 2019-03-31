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

    public Diff diff(Object left, Object right) {
        Object normalizedLeft;
        Object normalizedRight;

        if (normalizer != null) {
            normalizedLeft = normalizer.apply(left);
            normalizedRight = normalizer.apply(right);
        }
        else {
            normalizedLeft = left;
            normalizedRight = right;
        }

        if (Objects.equals(left, right)) {
            return null;
        }
        else if (normalizedLeft instanceof String && normalizedRight instanceof String) {
            return diffString((String)normalizedLeft, (String)normalizedRight);
        }
        else if (normalizedLeft instanceof List && normalizedRight instanceof List) {
            return diffList((List<?>)normalizedLeft, (List<?>)normalizedRight);
        }
        else if (normalizedLeft instanceof Map && normalizedRight instanceof Map) {
            return diffMap((Map<?,?>)normalizedLeft, (Map<?,?>) normalizedRight);
        }

        return new DiffValue(left, right);
    }

    public DiffValue diffString(String left, String right) {
        if (Objects.equals(left, right)) {
            return null;
        }

        return new DiffValue(left, right);
    }

    public Diff diffList(List<?> left, List<?> right) {
        if (left == null && right == null) {
            return null;
        }
        else if (left == null || right == null) {
            return new DiffValue(left, right);
        }

        int minSize = Math.min(left.size(), right.size());

        for (int i = 0; i < minSize; i++) {
            Object leftValue = left.get(i);
            Object rightValue = right.get(i);
            Diff diff = diff(leftValue, rightValue);

            if (diff != null) {
                return new DiffList(i, diff);
            }
        }

        if (left.size() != right.size()) {
            return new DiffSize(left.size(), right.size());
        }

        return null;
    }

    public DiffObject diffMap(Map<?,?> left, Map<?,?> right) {
        ArrayList<DiffProperty> properties = new ArrayList<>();

        Set<?> leftKeys = left.keySet();

        for (Object leftKey : leftKeys) {
            Object leftValue = left.get(leftKey);
            Object rightValue = right.get(leftKey);
            Diff item = diff(leftValue, rightValue);

            if (item != null) {
                properties.add(new DiffProperty(leftKey, item));
            }
        }

        Set<?> rightKeys = right.keySet();

        for (Object rightKey : rightKeys) {
            if (!leftKeys.contains(rightKey)) {
                Object leftValue = left.get(rightKey);
                Object rightValue = right.get(rightKey);
                Diff item = diff(leftValue, rightValue);

                if (item != null) {
                    properties.add(new DiffProperty(rightKey, item));
                }
            }
        }

        if (properties.isEmpty()) {
            return null;
        }

        return new DiffObject(properties.toArray(new DiffProperty[0]));
    }

}
