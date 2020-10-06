package gramat.graph;

import gramat.graph.plugs.Plug;

import java.util.List;

import static gramat.util.Validations.requireNotEmpty;

public class Extension {

    public final List<Plug> plugs;

    public Extension(List<Plug> plugs) {
        this.plugs = requireNotEmpty(plugs);
    }

}
