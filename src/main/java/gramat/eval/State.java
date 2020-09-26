package gramat.eval;

import gramat.framework.Logger;
import gramat.input.Tape;
import gramat.symbols.Symbol;
import gramat.symbols.SymbolWild;
import gramat.util.PP;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class State implements Iterable<Transition> {

    protected final String id;

    private List<Transition> transitions;
    private boolean accepted;

    public State(String id) {
        this.id = id;
    }

    public Object evalValue(String input, Logger logger) {
        var tape = new Tape(input);
        var context = new Context(tape, logger);

        context.pushContainer();

        var state = eval(context);

        if (context.tape.alive()) {
            throw new RuntimeException("unexpected char: " + PP.str(context.tape.peek()) + ", options: " + listOptions(state));
        }
        else if (!state.accepted) {
            throw new RuntimeException("not matched");
        }

        var root = context.popContainer();
        if (root.isEmpty()) {
            return null;
        }
        return root.popValue();
    }

    public boolean evalMatch(String input, Logger logger) {
        // TODO optimize only for matching
        var tape = new Tape(input);
        var context = new Context(tape, logger);

        context.pushContainer();

        try {
            var state = eval(context);

            if (context.tape.alive()) {
                return false;
            }

            return state.accepted;
        }
        catch (RejectedException e) {
            return false;
        }
    }

    public State eval(Context context) {
        var state = this;

        while (true) {
            var c = context.tape.peek();
            var transition = (Transition) null;

            if (state.transitions != null) {
                var wild = (Transition) null;
                for (var l : state.transitions) {
                    if (l.symbol instanceof SymbolWild) {
                        wild = l;
                    } else if (l.symbol.test(c)) {
                        transition = l;
                        break;
                    }
                }

                if (transition == null && wild != null) {
                    transition = wild;
                }
            }

            if (transition == null) {
                break;
            }

            context.logger.debug("transition %s -> %s with %s", state.id, transition.target.id, transition.symbol);

            if (transition.before != null) {
                for (var action : transition.before) {
                    context.logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            context.tape.move();
            context.logger.debug("moved to %s", context.tape.getPosition());

            if (transition.after != null) {
                for (var action : transition.after) {
                    context.logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            context.transactions.flush();

            state = transition.target;
        }

        return state;
    }

    private String listOptions(State state) {
        var output = new ByteArrayOutputStream();

        try (var printer = new PrintStream(output)) {
            for (var i = 0; i < state.transitions.size(); i++) {
                if (i > 0) {
                    printer.print(", ");
                }
                state.transitions.get(i).symbol.printAmCode(printer);
            }
        }

        return output.toString(Charset.defaultCharset());
    }

    @Override
    public Iterator<Transition> iterator() {
        if (transitions == null) {
            return Collections.emptyIterator();
        }
        return transitions.iterator();
    }

    public Transition addTransition(Symbol symbol, State state) {
        if (transitions == null) {
            transitions = new ArrayList<>();
        }

        var link = new Transition(symbol, state);

        transitions.add(link);

        return link;
    }

    public void markAccepted() {
        accepted = true;
    }

    public void writeAmCode(PrintStream out) {
        var control = new HashSet<State>();
        var queue = new LinkedList<State>();

        queue.add(this);

        while (queue.size() > 0) {
            var node = queue.remove();

            if (control.add(node)) {
                if (node.transitions != null) {
                    for (var link : node.transitions) {
                        link.writeAmCode(node, out);

                        queue.add(link.target);
                    }
                }

                if (node.accepted) {
                    out.println(id + " <=");
                }
            }
        }
    }

    public Transition searchLink(State target) {
        if (transitions != null) {
            for (var link : transitions) {
                if (link.target == target) {
                    return link;
                }
            }
        }

        return null;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
