package gramat.eval;

import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.framework.Logger;
import gramat.input.Tape;
import gramat.machine.State;
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
        var heap = new Heap(gramat.badges.empty());
        var context = new Context(logger, tape, heap);

        context.pushContainer();

        eval_state(initial, context);

        return context.popValue();
    }

    private void eval_state(State initial, Context context) {
        var result = run_state(initial, context);

        if (context.heap.notEmpty()) {
            throw new RejectedException("heap is not empty");
        }

        if (tape.alive()) {
            // TODO not always should be alive
            throw new RejectedException("unexpected char: " + PP.str(tape.peek()) + ", options: " + list_options(result));
        }

        if (!result.accepted) {
            throw new RejectedException("not matched: " + PP.str(tape.peek()) + ", options: " + list_options(result));
        }
    }

    private State run_state(State initial, Context context) {
        var state = initial;

        while (true) {
            logger.debug("evaluating state %s", state.id);

            // Find matching transition
            var badge = context.heap.peek();
            var chr = context.tape.peek();
            var effect = state.transition.match(badge, chr);

            // This is the end of the run
            if (effect == null) {
                logger.debug("missing transition from %s with %s / %s", state.id, PP.str(chr), PP.str(badge.toString()));
                break;
            }

            logger.debug("transition %s -> %s with %s / %s", state.id, effect.target.id, PP.str(chr), PP.str(badge.toString()));

            if (effect.before != null) {
                for (var action : effect.before) {
                    logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            tape.move();
            logger.debug("tape moved to position: %s", tape.getPosition());

            if (effect.after != null) {
                for (var action : effect.after) {
                    logger.debug("running action %s", action);
                    action.run(context);
                }
            }

            // Go for next state!
            state = effect.target;
        }

        return state;
    }

    private static String list_options(State state) {
        var symbols = new LinkedHashSet<>();

        for (var badge : state.transition.getBadges()) {
            symbols.addAll(state.transition.getSymbols(badge));
        }

        return StringUtils.join(",", symbols);
    }

}
