package gramat.automata.ndfa;

import java.util.*;

public class Language {

    final Set<NState> states;
    final Set<NState> wilds;
    final Set<Symbol> symbols;
    final List<NTransition> transitions;
    final List<NAutomaton> automata;
    final List<Runnable> postBuildActions;

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

    public NAutomaton automaton(NState initial, NState... accepted) {
        return automaton(initial, Set.of(accepted));
    }

    public NAutomaton automaton(NState initial, Set<NState> accepted) {
        return new NAutomaton(this, initial, accepted);
    }

    private Symbol search_symbol(char c) {
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

    private Symbol search_symbol(char begin, char end) {
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
        else if (value instanceof Character) {
            var c = (char)value;
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
            throw new RuntimeException("Unsupported symbol: " + value);
        }
    }

    public NState state() {
        var state = new NState(this, next_id);

        next_id++;

        states.add(state);

        return state;
    }

    public void transition(NState source, NState target, Object symbol) {
        transition(Set.of(Objects.requireNonNull(source)), target, symbol, List.of());
    }

    public void transition(NState source, NState target, Object symbol, String action) {
        transition(Set.of(Objects.requireNonNull(source)), target, symbol, List.of(action));
    }

    public void transition(NState source, NState target, Object symbol, List<String> actions) {
        transition(Set.of(Objects.requireNonNull(source)), target, symbol, actions);
    }

    public void transition(Set<NState> sources, NState target, Object symbol) {
        transition(sources, target, symbol, List.of());
    }

    public void transition(Set<NState> sources, NState target, Object symbol, String action) {
        transition(sources, target, symbol, List.of(action));
    }

    public void transition(Set<NState> sources, NState target, Object symbol, List<String> actions) {
        var s = make_symbol(symbol);

        for (var source : sources) {
            var t = new NTransition(source, s, target, actions);

            transitions.add(t);
        }
    }
}
