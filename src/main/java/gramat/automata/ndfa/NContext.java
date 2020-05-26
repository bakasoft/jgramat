package gramat.automata.ndfa;

import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NContext {

    public final NLanguage language;

    private final List<Runnable> postBuildHooks;

    public NContext(NLanguage language) {
        this.language = language;
        this.postBuildHooks = new ArrayList<>();
    }

    public void postBuildHook(Runnable hook) {
        postBuildHooks.add(hook);
    }

    public NMachine machine(NMachineBuilder builder, NStateSet initial, NStateSet accepted) {
        if (initial.isEmpty()) {
            throw new RuntimeException("Missing initial states");
        }

        language.openGroup();

        builder.build(this, initial, accepted);

        if (accepted.isEmpty()) {
            throw new RuntimeException("Missing accepted states");
        }

        var group = language.closeGroup();

        var totalStates = new NStateSet();

        totalStates.add(initial);
        totalStates.add(group.states);
        totalStates.add(accepted);

        var rejected = new NStateSet();

        rejected.add(totalStates);
        rejected.remove(accepted);

        return new NMachine(
                language,
                NStateSet.of(group.states).toArray(),
                group.transitions.toArray(NTransition[]::new),
                initial.toArray(),
                accepted.toArray(),
                rejected.toArray());
    }

    public static DState compile(NMachineBuilder builder) {
        var language = new NLanguage();
        var context = new NContext(language);
        var initial = NStateSet.of(language.state());
        var accepted = new NStateSet();
        var machine = context.machine(builder, initial, accepted);

        for (var hook : context.postBuildHooks) {
            hook.run();
        }

//        System.out.println("NDFA -----------");
//        System.out.println(this.captureOutput());
        return DMaker.transform(language, initial, accepted);
    }

}
