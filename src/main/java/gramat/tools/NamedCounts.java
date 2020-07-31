package gramat.tools;

import java.util.HashMap;
import java.util.Map;

public class NamedCounts {

    private final Map<String, Integer> map;

    public NamedCounts() {
        map = new HashMap<>();
    }

    public int next(String name) {
        var count = map.get(name);

        if (count == null) {
            map.put(name, 0);
            return 0;
        }

        count++;

        map.put(name, count);

        return count;
    }

}
