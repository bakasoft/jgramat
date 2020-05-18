package gramat.automata.ndfa;

import gramat.eval.Action;

import java.util.*;

public class Language {

    final Set<NState> states;
    public final Set<NState> wilds; // TODO public? it shouldn't
    final Set<Symbol> symbols;
    public final List<NTransition> transitions;
    final List<NAutomaton> automata;
    final List<Runnable> postBuildActions;  // TODO move out of here

    private int next_id;

    public Language() {
        states = new HashSet<>();
        symbols = new HashSet<>();
        wilds = new HashSet<>();
        transitions = new ArrayList<>();
        automata = new ArrayList<>();
        postBuildActions = new ArrayList<>();
        next_id = 0;
    }

    public void postBuild(Runnable action) {
        postBuildActions.add(action);
    }

    public void applyPostBuild() {
        for (var action : postBuildActions) {
            action.run();
        }
    }

    public void makeWild(NState state) {
        wilds.add(state);
    }

    public boolean isWild(NState state) {
        return wilds.contains(state);
    }

    @FunctionalInterface
    public interface AutomatonBuilder {

        void build(StateSubset initialSet, StateSubset acceptedSet);

    }

    public class StateSubset {

        final Set<NState> states = new HashSet<>();

        public NState create() {
            var state = state();

            states.add(state);

            return state;
        }

        public void add(Collection<NState> c) {
            this.states.addAll(c);
        }

        public void add(NState state) {
            this.states.add(state);
        }
    }

    public NAutomaton automaton(AutomatonBuilder builder) {
        pushStateCapturing();

        var initialSet = new StateSubset();
        var acceptedSet = new StateSubset();

        builder.build(initialSet, acceptedSet);

        var states = popStateCapturing();

        return new NAutomaton(this, initialSet.states, acceptedSet.states, states);
    }

    private Symbol search_symbol(int c) {
        for (var symbol : symbols) {
            if (symbol instanceof SymbolChar) {
                var sc = (SymbolChar)symbol;

                if (sc.value == c) {
                    return sc;
                }
            }
        }

        return null;
    }

    private Symbol search_symbol(int begin, int end) {
        if (begin == end) {
            return search_symbol(begin);
        }

        for (var symbol : symbols) {
            if (symbol instanceof SymbolRange) {
                var sr = (SymbolRange)symbol;

                if (sr.begin == begin && sr.end == end) {
                    return sr;
                }
            }
        }

        return null;
    }

    private Symbol search_wild() {
        for (var symbol : symbols) {
            if (symbol instanceof SymbolWild) {
                return symbol;
            }
        }

        return null;
    }

    private Symbol make_symbol(Object value) {
        if (value == null) {
            return null;
        }
        else if (value instanceof Integer) {
            var c = (int)value;
            var symbol = search_symbol(c);

            if (symbol != null) {
                return symbol;
            }

            symbol = new SymbolChar(c);
            symbols.add(symbol);
            return symbol;
        }
        else if (value instanceof SymbolChar) {
            var sc = (SymbolChar)value;
            var symbol = search_symbol(sc.value);

            if (symbol != null) {
                return symbol;
            }

            symbols.add(sc);
            return sc;
        }
        else if (value instanceof SymbolRange) {
            var sr = (SymbolRange)value;
            var symbol = search_symbol(sr.begin, sr.end);

            if (symbol != null) {
                return symbol;
            }

            symbols.add(sr);
            return sr;
        }
        else if (value instanceof SymbolWild) {
            var wild = search_wild();

            if (wild != null) {
                return wild;
            }

            wild = (SymbolWild) value;
            symbols.add(wild);
            return wild;
        }
        else {
            throw new RuntimeException("Unsupported symbol: " + value.getClass());
        }
    }

    public NState state() {
        var state = new NState(this, next_id);

        next_id++;

        states.add(state);

        if (stateCapturings.size() > 0) {
            stateCapturings.peek().add(state);
        }

        return state;
    }

    public void transition(NState source, NState target, Object symbol) {
        transition(Set.of(Objects.requireNonNull(source)), Set.of(target), symbol);
    }

    public void transition(Set<NState> sources, NState target, Object symbol) {
        transition(sources, Set.of(target), symbol);
    }

    public void transition(NState source, Set<NState> targets, Object symbol) {
        transition(Set.of(source), targets, symbol);
    }

    public void transition(Set<NState> sources, Set<NState> targets, Object symbol) {
        var s = make_symbol(symbol);

        for (var source : sources) {
            for (var target : targets) {
                var t = new NTransition(source, s, target);

                transitions.add(t);
            }
        }
    }

    private final Stack<Set<NState>> stateCapturings = new Stack<>();

    public void pushStateCapturing() {
        stateCapturings.push(new HashSet<>());
    }

    public Set<NState> popStateCapturing() {
        if (stateCapturings.size() > 0) {
            var capturing = stateCapturings.pop();

            if (stateCapturings.size() > 0) {
                stateCapturings.peek().addAll(capturing);
            }

            return capturing;
        }

        return states;
    }
}
