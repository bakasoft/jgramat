package gramat.scheme.data.automata;

import java.util.ArrayList;
import java.util.List;

public class TransitionData {

    public StateData source;
    public StateData target;

    public SymbolData symbol;
    public BadgeData badge;

    public List<ActionData> preActions;
    public List<ActionData> postActions;

    public TransitionData() {
        preActions = new ArrayList<>();
        postActions = new ArrayList<>();
    }

}
