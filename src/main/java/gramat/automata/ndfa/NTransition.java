package gramat.automata.ndfa;

import gramat.eval.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NTransition {

    public final NState source;
    public final NState target;
    public final Symbol symbol;
    public final List<Action> actions;

    NTransition(NState source, NState target, Symbol symbol) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.symbol = symbol;
        this.actions = new ArrayList<>();
    }

    @Override
    public String toString() {
        var symbolPart = (symbol != null ? " : " + symbol : "");
        return source.id + " -> " + target.id + symbolPart + " #" + Integer.toHexString(hashCode());
    }
}
