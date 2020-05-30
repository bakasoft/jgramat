package gramat.automata.ndfa;

import gramat.automata.dfa.DMachine;
import gramat.automata.dfa.DMaker;
import gramat.automata.dfa.DState;

import java.util.*;

public class NContext {

    public final NLanguage language;

    private final List<Runnable> postBuildHooks;

    private final Map<String, NMachine> amMap;

    private final List<MachineHookItem> machineHooks;

    public NContext(NLanguage language) {
        this.language = language;
        this.postBuildHooks = new ArrayList<>();
        this.amMap = new HashMap<>();
        this.machineHooks = new ArrayList<>();
    }

    public void postBuildHook(Runnable hook) {
        postBuildHooks.add(hook);
    }

    public void machineHook(NMachine machine, MachineHook hook) {
        machineHooks.add(new MachineHookItem(machine, hook));
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

        for (var hook : context.postBuildHooks) {
            hook.run();
        }

        System.out.println("NDFA -----------");
        System.out.println(machine.getAmCode());
        var maker = new DMaker(language, name, machine.initial, machine.accepted, new HashMap<>());
        var result = maker.run();

        for (var item : context.machineHooks) {
            var dMachineItem = maker.computeMachine(item.machine);

            item.hook.run(dMachineItem);
        }

        return result;
    }

    public NSegment segment(NState initial, NState accepted) {
        return new NSegment(language, initial, accepted);
    }

    private static class MachineHookItem {
        public final NMachine machine;
        public final MachineHook hook;
        public MachineHookItem(NMachine machine, MachineHook hook) {
            this.machine = machine;
            this.hook = hook;
        }
    }

    public interface MachineHook {

        void run(DMachine machine);

    }
}
