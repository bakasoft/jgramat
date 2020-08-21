package gramat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MapStack<K, V> {

    private final Map<K, Stack<V>> map;

    public MapStack() {
        map = new HashMap<>();
    }

    public void push(K key, V value) {
        var stack = map.computeIfAbsent(key, k -> new Stack<>());

        stack.push(value);
    }

    public V tryPop(K key) {
        var stack = map.get(key);

        if (stack == null) {
            return null;
        }
        else if (stack.isEmpty()) {
            // Removing unused references may help to release memory
            map.remove(key);
            return null;
        }

        return stack.pop();
    }

}
