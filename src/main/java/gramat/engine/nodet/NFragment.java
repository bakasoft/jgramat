package gramat.engine.nodet;

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
        public final Symbol symbol;
        public final NState target;

        public Target(List<NMark> marks, Symbol symbol, NState target) {
            this.marks = marks;
            this.symbol = symbol;
            this.target = target;
        }
    }

    public static class Source {
        public final NState source;
        public final Symbol symbol;
        public final List<NMark> marks;

        public Source(NState source, Symbol symbol, List<NMark> marks) {
            this.symbol = symbol;
            this.source = source;
            this.marks = marks;
        }
    }

}
