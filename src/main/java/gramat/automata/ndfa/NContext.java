package gramat.automata.ndfa;

import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;

import java.util.ArrayList;
import java.util.HashMap;
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

    public NMachine machine(NMachineBuilder builder) {
        language.openGroup();

        var segment = builder.build(this);

        var group = language.closeGroup();

        return segment.toMachine(group);
    }

    public static NAutomaton compileAutomaton(NLanguage language, String name, NMachineBuilder builder) {
        var automaton = language.createAutomaton(name);
        var context = new NContext(language);
        var machine = context.machine(builder);

        machine.wrap(automaton.initial, automaton.accepted);

        for (var hook : context.postBuildHooks) {
            hook.run();
        }

        System.out.println("NDFA -----------");
        System.out.println(machine.getAmCode());
        return automaton;
    }

    public static DState compile(String name, NMachineBuilder builder) {
        var language = new NLanguage();
        var automaton = compileAutomaton(language, name, builder);
        return DMaker.transform(language, name, automaton.initial, automaton.accepted, new HashMap<>());
    }

    public NSegment segment(NState initial, NState accepted) {
        return new NSegment(language, initial, accepted);
    }
}
