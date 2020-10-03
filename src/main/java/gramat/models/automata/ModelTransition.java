package gramat.models.automata;

import java.util.ArrayList;
import java.util.List;

public class ModelTransition {

    public ModelState source;
    public ModelState target;

    public ModelSymbol symbol;
    public ModelBadge badge;

    public List<ModelAction> preActions;
    public List<ModelAction> postActions;

    public ModelTransition() {
        preActions = new ArrayList<>();
        postActions = new ArrayList<>();
    }

}
