package gramat.engine.nodet;

import gramat.engine.actions.ActionList;
import gramat.engine.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;

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
        public final List<NMark> marks;
        public final ActionList actions;
        public final Symbol symbol;
        public final NState target;

        public Target(ActionList actions, List<NMark> marks, Symbol symbol, NState target) {
            this.actions = actions;
            this.marks = marks;
            this.symbol = symbol;
            this.target = target;
        }
    }

    public static class Source {
        public final NState source;
        public final Symbol symbol;
        public final List<NMark> marks;
        public final ActionList actions;

        public Source(NState source, Symbol symbol, List<NMark> marks, ActionList actions) {
            this.symbol = symbol;
            this.source = source;
            this.marks = marks;
            this.actions = actions;
        }
    }

}
