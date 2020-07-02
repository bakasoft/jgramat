package gramat.epsilon;

import java.util.*;

public class State {

    public final Language language;
    public final int id;
    public final List<Transition> transitions;

    public State(Language language, int id) {
        this.language = language;
        this.id = id;
        this.transitions = new ArrayList<>();
    }

    public Set<State> getNullClosure() {
        var closure = new HashSet<State>();
        var queue = new LinkedList<State>();

        queue.add(this);

        do {
            var source = queue.remove();

            if (closure.add(source)) {
                for (var trn : source.transitions) {
                    if (trn.symbol.isEmpty()) {
                        queue.add(trn.target);
                    }
                }
            }
        } while (queue.size() > 0);

        return closure;
    }

    public Set<State> getInverseNullClosure() {
        var result = new HashSet<State>();
        var queue = new LinkedList<State>();
        var control = new HashSet<State>();

        queue.add(this);

        result.add(this);

        do {
            var target = queue.remove();

            if (control.add(target)) {
                for (var trn : language.findTransitionsByTarget(target)) {
                    if (trn.symbol.isEmpty()) {
                        result.add(trn.source);

                        queue.add(trn.source);
                    }
                }
            }
        }
        while(queue.size() > 0);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        else {
            State state = (State) o;
            return id == state.id;
        }
    }

    @Override
    public int hashCode() {
        return id;
    }
}
