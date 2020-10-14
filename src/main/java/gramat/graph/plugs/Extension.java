package gramat.graph.plugs;

import gramat.graph.plugs.Plug;

import java.util.List;

import static gramat.util.Validations.requireNotEmpty;

public class Extension {

    public final int id;
    public final List<Plug> plugs;

    public Extension(int id, List<Plug> plugs) {
        this.id = id;
        this.plugs = requireNotEmpty(plugs);
    }

}
