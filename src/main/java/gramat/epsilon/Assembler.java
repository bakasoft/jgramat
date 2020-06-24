package gramat.epsilon;

import gramat.automata.raw.RawAutomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembler {

    private final List<Runnable> linkHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;
    private final Map<String, Machine> machines;

    public Assembler() {
        this.linkHooks = new ArrayList<>();
        this.recursiveHooks = new ArrayList<>();
        this.actionHooks = new ArrayList<>();
        this.machines = new HashMap<>();
    }

    public Machine assemble(RawAutomaton automaton) {
        var language = new Language();
        var builder = new Builder(this, language, null);
        var initial = language.newState();
        var machine = builder.machine(automaton, initial);

        for (var hook : linkHooks) {
            hook.run();
        }

        for (var hook : recursiveHooks) {
            hook.run();
        }

        for (var hook : actionHooks) {
            hook.run();
        }

        System.out.println("NDFA -----------");
        System.out.println(machine.getAmCode());

        return machine;
    }

    public void linkHook(State state, StateHook hook) {
        Runnable runnable = () -> hook.run(state);

        linkHooks.add(runnable);
    }

    public void recursiveHook(Machine machine, State initial, State accepted, RecursiveHook hook) {
        recursiveHooks.add(() -> hook.run(machine, initial, accepted));
    }

    public void actionHook(Machine machine, MachineHook hook) {
        actionHooks.add(() -> hook.run(machine));
    }

    public Machine reuseMachine(String name, RawAutomaton automaton, Builder builder, State initial) {
        var machine = machines.get(name);

        if (machine == null) {
            machine = builder.machine(automaton, initial);

            machines.put(name, machine);
        }

        return machine;
    }
}
