package gramat.eval;

import gramat.badges.Badge;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.framework.Logger;
import gramat.input.Tape;
import gramat.machine.State;
import gramat.machine.Transition;
import gramat.symbols.SymbolWild;
import gramat.util.PP;
import gramat.util.StringUtils;

import java.util.LinkedHashSet;

public class Evaluator extends DefaultComponent {

    private final Tape tape;
    private final Logger logger;

    public Evaluator(Component parent, Tape tape, Logger logger) {
        super(parent);
        this.tape = tape;
        this.logger = logger;
    }

    public Object evalValue(State initial) {
        var context = new Context(logger, tape);

        context.pushContainer();

        eval_state(initial, context);

        return context.popValue();
    }

    private void eval_state(State initial, Context context) {
        var heap = new Heap(gramat.badges.empty());

        var result = run_state(initial, context, heap);

        if (heap.notEmpty()) {
            throw new RejectedException("heap is not empty");
        }

        if (tape.alive()) {
            // TODO not always should be alive
            throw new RejectedException("unexpected char: " + PP.str(tape.peek()) + ", options: " + list_options(result));
        }

        if (!result.isAccepted()) {
            throw new RejectedException("not accepted state");
        }
    }

    private State run_state(State initial, Context context, Heap heap) {
        var state = initial;

        while (true) {
            logger.debug("evaluating state %s", state.id);

            // Find matching transition
            var chr = tape.peek();
            var badge = heap.peek();
            var transition = (Transition) null;
            var wild = (Transition) null;
            for (var t : state) {
                if (t.symbol instanceof SymbolWild) {
                    wild = t;
                }
                else if (enter_transition(t, chr, badge, heap)) {
                    transition = t;
                    break;
                }
            }

            // Fallback in wild transition (if available)
            if (transition == null && wild != null) {
                transition = wild;
            }

            // This is the end of the run
            if (transition == null) {
                break;
            }

            logger.debug("transition %s -> %s with %s", state.id, transition.target.id, transition.symbol);

            if (context != null && transition.before != null) {
                for (var action : transition.before) {
                    logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            tape.move();
            logger.debug("tape moved to position: %s", tape.getPosition());

            if (context != null && transition.after != null) {
                for (var action : transition.after) {
                    logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            // Go for next state!
            state = transition.target;
        }

        return state;
    }

    private boolean enter_transition(Transition t, char chr, Badge badge, Heap heap) {
        if (t.symbol.test(chr)) {
            switch (t.mode) {
                case NONE:
                    return true;
                case PUSH:
                    logger.debug("heap push " + t.badge);
                    return heap.push(t.badge);
                case PEEK:
                    return t.badge == badge;
                case POP:
                    if (t.badge != badge) {
                        return false;
                    }
                    logger.debug("heap pop " + t.badge);
                    return heap.pop(t.badge);
            }
        }
        return false;
    }

    private static String list_options(State state) {
        var symbols = new LinkedHashSet<>();

        for (var transition : state) {
            symbols.add(transition.symbol);
        }

        return StringUtils.join(",", symbols);
    }

}
