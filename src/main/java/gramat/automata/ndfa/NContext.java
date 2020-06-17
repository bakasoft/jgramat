package gramat.automata.ndfa;

import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;
import gramat.automata.ndfa.hooks.MachineHook;
import gramat.automata.ndfa.hooks.RecursiveHook;
import gramat.automata.ndfa.hooks.StateHook;

import java.util.*;

public class NContext {

    public final NLanguage language;

    private final Map<String, NMachine> amMap;

    private final List<Runnable> linkHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    public NContext(NLanguage language) {
        this.language = language;
        this.amMap = new HashMap<>();
        this.linkHooks = new ArrayList<>();
        this.recursiveHooks = new ArrayList<>();
        this.actionHooks = new ArrayList<>();
    }

    public void linkHook(NState state, StateHook hook) {
        linkHooks.add(() -> hook.run(state));
    }

    public void recursiveHook(NMachine machine, NState initial, NState accepted, RecursiveHook hook) {
        recursiveHooks.add(() -> hook.run(machine, initial, accepted));
    }

    public void actionHook(NMachine machine, MachineHook hook) {
        actionHooks.add(() -> hook.run(machine));
    }

    public NMachine machine(NMachineBuilder builder) {
        language.openGroup();

        var segment = builder.build(this);

        var group = language.closeGroup();

        return segment.toMachine(group);
    }

    public NMachine getMachine(String name) {
        return amMap.get(name);
    }

    public NMachine createMachine(String name, NMachineBuilder builder) {
        if (amMap.containsKey(name)) {
            throw new RuntimeException();
        }

        var machine = machine(builder);

        amMap.put(name, machine);

        return machine;
    }

    public static DState compile(String name, NMachineBuilder builder) {
        var language = new NLanguage();
        var context = new NContext(language);
        var machine = context.createMachine(name, builder);

        for (var hook : context.linkHooks) {
            hook.run();
        }

        for (var hook : context.recursiveHooks) {
            hook.run();
        }

        for (var hook : context.actionHooks) {
            hook.run();
        }

        System.out.println("NDFA -----------");
        System.out.println(machine.getAmCode());

        var maker = new DMaker(language, name, machine.initial, machine.accepted, new HashMap<>());
        return maker.run();
    }

    public NSegment segment(NState initial, NState accepted) {
        return new NSegment(language, initial, accepted);
    }

}
