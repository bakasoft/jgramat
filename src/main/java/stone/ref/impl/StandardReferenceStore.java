package stone.ref.impl;

import stone.ref.StoneReferenceStore;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StandardReferenceStore implements StoneReferenceStore {

    private static class Tracker {

        private final Map<Object, Object> refValues;
        private final Map<Object, Object> valueRefs;

        private long nextRef;

        public Tracker() {
            nextRef = 1;
            refValues = new HashMap<>();
            valueRefs = new HashMap<>();
        }

        public Object getReference(Object value) {
            return valueRefs.get(value);
        }

        public Object getValue(Object reference) {
            return refValues.get(reference);
        }

        public void set(Object value, Object reference) {
            valueRefs.put(value, reference);
            refValues.put(reference, value);
        }

        public Object add(Object value) {
            var reference = nextRef;

            nextRef++;

            set(value, reference);

            return reference;
        }

        public boolean containsReference(Object reference) {
            return refValues.containsKey(reference);
        }
    }

    private final Map<String, Tracker> trackers;

    public StandardReferenceStore() {
        trackers = new LinkedHashMap<>();
    }

    private Tracker tracker(String type) {
        return trackers.computeIfAbsent(type, k -> new Tracker());
    }

    @Override
    public Object getReference(String type, Object value) {
        return tracker(type).getReference(value);
    }

    @Override
    public Object getValue(String type, Object reference) {
        return tracker(type).getValue(reference);
    }

    @Override
    public void set(String type, Object value, Object reference) {
        tracker(type).set(value, reference);
    }

    @Override
    public Object add(String type, Object value) {
        return tracker(type).add(value);
    }

    @Override
    public boolean containsReference(String type, Object reference) {
        return tracker(type).containsReference(reference);
    }
}
