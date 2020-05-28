package gramat.automata.raw;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CollapseContext {

    public final Map<String, RawAutomaton> cache;

    public CollapseContext() {
        cache = new HashMap<>();
    }

}
