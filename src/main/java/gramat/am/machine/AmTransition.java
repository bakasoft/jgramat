package gramat.am.machine;

import java.util.ArrayList;
import java.util.List;

public class AmTransition {

    public AmState source;
    public AmState target;

    public AmSymbol symbol;
    public AmBadge badge;

    public List<AmAction> preActions;
    public List<AmAction> postActions;

    public AmTransition() {
        preActions = new ArrayList<>();
        postActions = new ArrayList<>();
    }

}
