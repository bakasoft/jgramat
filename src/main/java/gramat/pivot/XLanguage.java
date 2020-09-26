package gramat.pivot;

import gramat.pivot.data.*;
import gramat.symbols.Symbol;
import gramat.symbols.SymbolStore;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class XLanguage {

    protected final SymbolStore symbols;
    public final List<XTransition> transitions;
    protected final List<XState> states;
    public final List<XMachine> machines;
    protected final Set<String> tokens;

    private int nextStateID;

    public XLanguage() {
        symbols = new SymbolStore();
        transitions = new ArrayList<>();
        states = new ArrayList<>();
        machines = new ArrayList<>();
        tokens = new HashSet<>();
        nextStateID = 0;
    }

    public String generateToken(String base) { // TODO is this method fine here?
        var result = base + "_0";
        int count = 1;

        while (tokens.contains(result)) {
            result = base + "_" + count;

            count++;
        }

        tokens.add(result);

        return result;
    }

    public XState createState() {
        var state = new XState(this, nextStateID);

        nextStateID++;

        states.add(state);

        return state;
    }

    public XTransition createTransitionRef(XState source, XState target, String reference) {
        return createTransition(source, target, new TDReference(reference));
    }

    public XTransition createTransitionChar(XState source, XState target, char c) {
        var symbol = symbols.makeChar(c);

        return createTransition(source, target, new TDSymbol(symbol));
    }

    public XTransition createTransitionRange(XState source, XState target, char begin, char end) {
        var symbol = symbols.makeRange(begin, end);

        return createTransition(source, target, new TDSymbol(symbol));
    }

    public XTransition createTransitionWild(XState source, XState target) {
        return createTransition(source, target, new TDSymbol(symbols.makeWild()));
    }

    public XTransition createTransitionEmpty(XState source, XState target) {
        return createTransition(source, target, null);
    }

    public XTransition createTransition(XState source, XState target, XTransitionData data) {
        var transition = new XTransition(this, source, target, data);

        transitions.add(transition);

        return transition;
    }

    public XMachine findMachine(String name) {
        var machine = searchMachine(name);

        if (machine == null) {
            throw new RuntimeException("Machine not found: " + name);
        }

        return machine;
    }

    public XMachine searchMachine(String name) {
        for (var machine : machines) {
            if (Objects.equals(name, machine.name)) {
                return machine;
            }
        }
        return null;
    }

    public XMachine createMachine(String name, XSegment segment) { {
        return createMachine(name, segment.initial, segment.accepted);
    }}

    public XMachine createMachine(String name, XState initial, XState accepted) {
        if (searchMachine(name) != null) {
            throw new RuntimeException("machine already exists: " + name);
        }
        var machine = new XMachine(this, name, initial, accepted);

        machines.add(machine);

        return machine;
    }

    public List<XTransition> findTransitionsFrom(XState state) {
        return transitions.stream()
                .filter(t -> t.source == state)
                .collect(Collectors.toList());
    }

    public List<XTransition> findTransitionsTo(XState state) {
        return transitions.stream()
                .filter(t -> t.target == state)
                .collect(Collectors.toList());
    }

    public List<XTransition> findTransitionsFrom(Set<XState> states, Symbol symbol) {
        return transitions.stream()
                .filter(t -> {
                    if (t.data instanceof TDSymbol && states.contains(t.source)) {
                        var data = (TDSymbol)t.data;

                        return Objects.equals(data.symbol, symbol);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public Set<XState> computeEmptyClosure(Set<XState> states) {
        var closure = new LinkedHashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.addAll(states);

        while (queue.size() > 0) {
            var source = queue.remove();

            if (closure.add(source)) {
                for (var trn : findTransitionsFrom(source)) {
                    if (!(trn.data instanceof TDSymbol)) {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return closure;
    }

    public Set<XState> computeEmptyInverseClosure(Set<XState> states) {
        var closure = new LinkedHashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.addAll(states);

        while (queue.size() > 0) {
            var target = queue.remove();

            if (closure.add(target)) {
                for (var trn : findTransitionsTo(target)) {
                    if (!(trn.data instanceof TDSymbol)) {
                        queue.add(trn.source);
                    }
                }
            }
        }

        return closure;
    }

    public Set<XTransition> computeSymbolClosure(XState source) {
        var result = new LinkedHashSet<XTransition>();
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(source);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsFrom(state)) {
                    if (trn.data instanceof TDSymbol) {
                        result.add(trn);
                    }
                    else {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return result;
    }

    public Set<XTransition> computeSymbolInverseClosure(XState target) {
        var result = new LinkedHashSet<XTransition>();
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(target);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsTo(state)) {
                    if (trn.data instanceof TDSymbol) {
                        result.add(trn);
                    }
                    else {
                        queue.add(trn.source);
                    }
                }
            }
        }

        return result;
    }

    public boolean areForwardLinked(XState source, Set<XState> targets) {
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(source);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsFrom(state)) {
                    if (targets.contains(trn.target)) {
                        return true;
                    }
                    else {
                        queue.add(trn.target);
                    }
                }
            }
        }

        return false;
    }

    public boolean areBackwardLinked(XState target, Set<XState> sources) {
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(target);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsTo(state)) {
                    if (sources.contains(trn.source)) {
                        return true;
                    }
                    else {
                        queue.add(trn.source);
                    }
                }
            }
        }

        return false;
    }

    public Set<XTransition> findSymbolTransitionsTo(XState target) {
        var result = new LinkedHashSet<XTransition>();
        var control = new HashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(target);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsTo(state)) {
                    if (trn.data instanceof TDSymbol) {
                        result.add(trn);
                    }
                    else {
                        queue.add(trn.source);
                    }
                }
            }
        }

        return result;
    }

    public void printAmCode(PrintStream out) {
        for (var transition : transitions) {
            transition.printAmCode(out);
        }
    }

    public Set<XState> listStatesFrom(XState initial) {
        var control = new LinkedHashSet<XState>();
        var queue = new LinkedList<XState>();

        queue.add(initial);

        while (queue.size() > 0) {
            var state = queue.remove();

            if (control.add(state)) {
                for (var trn : findTransitionsFrom(state)) {
                    queue.add(trn.target);
                }
            }
        }

        return control;
    }
}
