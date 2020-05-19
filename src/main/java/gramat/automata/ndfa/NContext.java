package gramat.automata.ndfa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NContext implements NContainer {

    private final NLanguage language;
    private final NContainer parent;

    private final List<NState> states;
    private final List<NTransition> transitions;

    private final List<NState> initialStates;
    private final List<NState> acceptedStates;

    private final List<Runnable> postBuildHooks;

    public NContext(NLanguage language, NContainer parent, List<Runnable> postBuildHooks) {
        this.language = language;
        this.parent = parent;
        this.postBuildHooks = postBuildHooks;
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.initialStates = new ArrayList<>();
        this.acceptedStates = new ArrayList<>();
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

    public NMachine subMachine(NMachineBuilder builder) {
        var context = new NContext(language, this, postBuildHooks);

        builder.build(context);

        return context.machine();
    }

    public NState initial() {
        var state = state();

        initialStates.add(state);

        return state;
    }

    public NState accepted() {
        var state = state();

        acceptedStates.add(state);

        return state;
    }

    public NState initialAccepted() {
        var state = state();

        initialStates.add(state);
        acceptedStates.add(state);

        return state;
    }

    public NMachine machine() {
        if (initialStates.isEmpty()) {
            throw new RuntimeException("Missing initial states");
        }

        if (acceptedStates.isEmpty()) {
            throw new RuntimeException("Missing accepted states");
        }

        return new NMachine(language, states, transitions, initialStates, acceptedStates);
    }

    public void accepted(Collection<NState> states) {
        for (var state : states) {
            accepted(state);
        }
    }

    public void accepted(NState state) {
        if (!acceptedStates.contains(state)) {
            acceptedStates.add(state);
        }
    }

    public void initial(Collection<NState> states) {
        for (var state : states) {
            initial(state);
        }
    }

    public void initial(NState state) {
        if (!initialStates.contains(state)) {
            initialStates.add(state);
        }
    }

    public List<NState> getInitialStates() {
        return initialStates;
    }
}
