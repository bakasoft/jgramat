package gramat.machine;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.symbols.Symbol;
import java.util.*;

public class State implements Iterable<Transition> {

    public final String id;

    private List<Transition> transitions;
    private boolean accepted;

    public State(String id) {
        this.id = id;
    }

    @Override
    public Iterator<Transition> iterator() {
        if (transitions == null) {
            return Collections.emptyIterator();
        }
        return transitions.iterator();
    }

    public void createTransition(Symbol symbol, Badge badge, BadgeMode mode, State state, Action[] before, Action[] after) {
        if (transitions == null) {
            transitions = new ArrayList<>();
        }

        var transition = new Transition(symbol, badge, mode, state, before, after);

        transitions.add(transition);
    }

    public void markAccepted() {
        accepted = true;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
