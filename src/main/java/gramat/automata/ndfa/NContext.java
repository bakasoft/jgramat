package gramat.automata.ndfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NContext implements NContainer {

    public final NLanguage language;
    private final NContainer parent;

    private final NStateSet states;
    private final List<NTransition> transitions;

    private final List<Runnable> postBuildHooks;

    public NContext(NLanguage language, NContainer parent, List<Runnable> postBuildHooks) {
        this.language = language;
        this.parent = parent;
        this.postBuildHooks = postBuildHooks;
        this.states = new NStateSet();
        this.transitions = new ArrayList<>();
    }

    @Override
    public NState state() {
        var state = parent.state();

        states.add(state);

        return state;
    }

    @Override
    public NTransition transition(NState source, NState target, Symbol symbol) {
        var transition = parent.transition(source, target, symbol);

        transitions.add(transition);

        return transition;
    }

    public void postBuildHook(Runnable hook) {
        postBuildHooks.add(hook);
    }

    public NMachine machine(NMachineBuilder builder, NStateSet initial, NStateSet accepted) {
        var context = new NContext(language, this, postBuildHooks);

        builder.build(context, initial, accepted);

        if (initial.isEmpty()) {
            throw new RuntimeException("Missing initial states");
        }

        if (accepted.isEmpty()) {
            throw new RuntimeException("Missing accepted states");
        }

        var totalStates = new NStateSet();

        totalStates.add(initial);
        totalStates.add(states);
        totalStates.add(accepted);

        var rejected = new NStateSet();

        rejected.add(totalStates);
        rejected.remove(accepted);

        return new NMachine(
                language,
                states.toArray(),
                transitions.toArray(NTransition[]::new),
                initial.toArray(),
                accepted.toArray(),
                rejected.toArray());
    }

}
