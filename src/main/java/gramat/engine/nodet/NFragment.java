package gramat.engine.nodet;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NFragment {

    public final String name;
    public final List<Target> targets;
    public final List<Source> sources;
    public boolean ready;

    public NFragment(String name) {
        this.name = name;
        this.targets = new ArrayList<>();
        this.sources = new ArrayList<>();
    }

    public static class Target {
        public final ActionList actions;
        public final Symbol symbol;
        public final NState target;

        public Target(ActionList actions, Symbol symbol, NState target) {
            this.actions = Objects.requireNonNull(actions);
            this.symbol = Objects.requireNonNull(symbol);
            this.target = Objects.requireNonNull(target);
        }
    }

    public static class Source {
        public final NState source;
        public final Symbol symbol;
        public final ActionList actions;

        public Source(NState source, Symbol symbol, ActionList actions) {
            this.symbol = Objects.requireNonNull(symbol);
            this.source = Objects.requireNonNull(source);
            this.actions = Objects.requireNonNull(actions);
        }
    }

}
