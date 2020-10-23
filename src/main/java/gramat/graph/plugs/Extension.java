package gramat.graph.plugs;

import gramat.graph.plugs.Plug;

import java.util.List;
import java.util.Set;

import static gramat.util.Validations.requireNotEmpty;

public class Extension {

    public final int id;
    public final Set<Plug> plugs;

    public Extension(int id, Set<Plug> plugs) {
        this.id = id;
        this.plugs = requireNotEmpty(plugs);
    }

}
