package gramat.eval;

import gramat.scheme.common.badges.BadgeSource;
import gramat.framework.Context;
import gramat.input.Tape;
import gramat.scheme.State;
import gramat.util.PP;
import gramat.util.StringUtils;

import java.util.LinkedHashSet;

public class Evaluator {

    private final Context ctx;
    private final Tape tape;
    private final BadgeSource badges;

    public Evaluator(Context ctx, Tape tape, BadgeSource badges) {
        this.ctx = ctx;
        this.tape = tape;
        this.badges = badges;
    }

    public Object evalValue(State initial) {
        var heap = new Heap(badges.empty());
        var context = new EvalContext(ctx, tape, heap);

        context.pushContainer();

        eval_state(initial, context);

        return context.popValue();
    }

    private void eval_state(State initial, EvalContext context) {
        var result = run_state(initial, context);

        if (context.heap.notEmpty()) {
            throw new RejectedException("heap is not empty: " + context.heap);
        }

        if (tape.alive()) {
            // TODO not always should be alive
            throw new RejectedException("unexpected char: " + PP.str(tape.peek()) + ", options: " + list_options(result), tape.getLocation());
        }

        if (!result.accepted) {
            throw new RejectedException("not matched: " + PP.str(tape.peek()) + ", options: " + list_options(result), tape.getLocation());
        }
    }

    private State run_state(State initial, EvalContext context) {
        var state = initial;

        while (true) {
            if (context.tape.completed()) {
                break;
            }

            ctx.debug("evaluating state %s", state.id);

            // Find matching transition
            var badge = context.heap.peek();
            var chr = context.tape.peek();
            var effect = state.transition.match(badge, chr);

            // This is the end of the run
            if (effect == null) {
                ctx.debug("missing transition from %s with %s / %s", state.id, PP.str(chr), PP.str(badge.toString()));
                break;
            }

            ctx.debug("transition %s -> %s with %s / %s", state.id, effect.target.id, PP.str(chr), PP.str(badge.toString()));

            if (effect.before != null) {
                for (var action : effect.before) {
                    ctx.debug("running action %s", action);
                    action.run(context);
                }
            }

            tape.move();
            ctx.debug("tape moved to position: %s", tape.getPosition());

            if (effect.after != null) {
                for (var action : effect.after) {
                    ctx.debug("running action %s", action);
                    action.run(context);
                }
            }

            // Go for next state!
            state = effect.target;

            context.manager.commit();
        }

        context.manager.flush();

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
