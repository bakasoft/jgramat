package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.expressions.flat.BeginSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NLanguage implements NContainer {

    private final List<NState> states;
    public final List<NTransition> transitions;  // TODO make private
    public final List<NActionPattern> actionPatterns;  // TODO make private
    public final Set<Symbol> symbols;  // TODO make private
    private final Set<NState> wilds;

    private int nextStateID;

    public NLanguage() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        actionPatterns = new ArrayList<>();
        symbols = new HashSet<>();
        wilds = new HashSet<>();
        nextStateID = 1;
    }

    public NMachine machine(NMachineBuilder builder) {
        var postBuildHooks = new ArrayList<Runnable>();
        var context = new NContext(this, this, postBuildHooks);
        var initial = context.state();
        var accepted = new NStateSet();

        var machine = context.machine(builder, NStateSet.of(initial), accepted);

        for (var hook : postBuildHooks) {
            hook.run();
        }

        return machine;
    }

    public void addActionPattern(NState source, Symbol symbol, NState target, Action action, boolean begin) {
        if (begin) {
            actionPatterns.add(0, new NActionPattern(source, symbol, target, action));
        }
        else {
            actionPatterns.add(new NActionPattern(source, symbol, target, action));
        }
    }

    public void makeWild(NState state) {
        wilds.add(state);
    }

    public boolean isWild(NState state) {
        return wilds.contains(state);
    }

    @Override
    public NState state() {
        var state = new NState(this, nextStateID);

        nextStateID++;

        states.add(state);

        return state;
    }

    @Override
    public NTransition transition(NState source, NState target, Symbol symbol) {
        var transition = new NTransition(source, target, make_symbol(symbol));

        transitions.add(transition);

        return transition;
    }

    public List<NTransition> findTransitionsBySource(NState source) {
        return transitions.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toUnmodifiableList());
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
        if (value instanceof Integer) {
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
}
