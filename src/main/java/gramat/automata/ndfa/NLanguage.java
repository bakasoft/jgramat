package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.expressions.flat.BeginSource;

import java.util.*;
import java.util.stream.Collectors;

public class NLanguage {

    private final List<NState> states;
    public final List<NTransition> transitions;  // TODO make private
    public final Set<Symbol> symbols;  // TODO make private
    private final Map<String, NAutomaton> amMap;

    private final Stack<NGroup> groupStack;

    private int nextStateID;

    public NLanguage() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        symbols = new HashSet<>();
        groupStack = new Stack<>();
        amMap = new HashMap<>();
        nextStateID = 1;
    }

    public void openGroup() {
        System.out.println("OPEN GROUP");
        groupStack.push(new NGroup());
    }

    public NGroup closeGroup() {
        System.out.println("CLOSE GROUP");
        return groupStack.pop();
    }

    public NState state() {
        var state = new NState(this, nextStateID);

        nextStateID++;

        states.add(state);

        for (var group : groupStack) {
            group.states.add(state);
        }

        return state;
    }

    public void transition(NState source, NState target, Symbol symbol) {
        symbol = make_symbol(symbol);

        // avoid duplicate transitions
        var transition = findTransition(source, target, symbol);

        if (transition == null) {
            transition = new NTransition(source, target, symbol);

            transitions.add(transition);
        }

        System.out.println("NEW " + transition);

        for (var group : groupStack) {
            if (!group.transitions.contains(transition)) {
                group.transitions.add(transition);
            }
        }
    }

    private NTransition findTransition(NState source, NState target, Symbol symbol) {
        for (var trn : transitions) {
            if (trn.source == source && trn.target == target && trn.symbol == symbol) {
                return trn;
            }
        }
        return null;
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

    public void transition(NStateSet sources, NStateSet targets, Symbol symbol) {
        if (sources.isEmpty()) {
            throw new RuntimeException("Missing source states");
        }
        else if (targets.isEmpty()) {
            throw new RuntimeException("Missing target states");
        }

        for (var source : sources) {
            for (var target : targets) {
                transition(source, target, symbol);
            }
        }
    }

    public void transitionChar(NStateSet sources, NState target, int value) {
        transition(sources, NStateSet.of(target), new SymbolChar(value));
    }

    public void transitionChar(NStateSet sources, NStateSet targets, int value) {
        transition(sources, targets, new SymbolChar(value));
    }

    public void transitionRange(NStateSet sources, NStateSet targets, int begin, int end) {
        transition(sources, targets, new SymbolRange(begin, end));
    }

    public void transitionWild(NState source, NStateSet targets) {
        transitionWild(NStateSet.of(source), targets);
    }

    public void transitionWild(NStateSet sources, NStateSet targets) {
        transition(sources, targets, new SymbolWild());
    }

    public NAutomaton getAutomaton(String name) {
        return amMap.get(name);
    }

    public NAutomaton createAutomaton(String name) {
        var initial = state();
        var accepted = new NStateSet();
        var automaton = new NAutomaton(name, initial, accepted);

        if (amMap.containsKey(name)) {
            throw new RuntimeException();
        }

        amMap.put(name, automaton);

        return automaton;
    }
}
