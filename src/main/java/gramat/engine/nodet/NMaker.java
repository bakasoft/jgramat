package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.Action;
import gramat.expressions.Expression;

import java.util.*;
import java.util.stream.Collectors;

public class NMaker  {

    public static NMachine compile(String name, Expression expression) {
        var root = new NRoot();
        var maker = new NMaker(root);
        var builder = new NBuilder(maker);
        var initial = root.newState();
        var machine = expression.machine(builder, initial);

        maker.setMachine(name, machine);

        for (var hook : maker.transitionHooks) {
            hook.run();
        }

        for (var hook : maker.recursiveHooks) {
            hook.run();
        }

        for (var hook : maker.actionHooks) {
            hook.run();
        }

        // recreate machine with all states and transitions created by the hooks
        var result = new NMachine(root, machine.initial, machine.accepted, root.states, root.transitions);

        System.out.println("NDFA >>>>>>>>>>");
        System.out.println(result.getAmCode());
        System.out.println("<<<<<<<<<< NDFA");
        return result;
    }

    public final NRoot root;

    private final List<Runnable> transitionHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    private final List<NPlaceholder> placeholders;
    private final Map<String, NMachine> namedMachines;
    private final Set<String> recursiveNames;

    private final List<NGroup> groups;

    private NMaker(NRoot root) {
        this.root = root;
        transitionHooks = new ArrayList<>();
        recursiveHooks = new ArrayList<>();
        actionHooks = new ArrayList<>();
        placeholders = new ArrayList<>();
        namedMachines = new HashMap<>();
        recursiveNames = new HashSet<>();
        groups = new ArrayList<>();
    }

    public NGroup newGroup(Action begin, Action commit, Action rollback) {
        var number = groups.size() + 1;
        var group = new NGroup(number, begin, commit, rollback);

        groups.add(group);

        return group;
    }

    public boolean addRecursiveName(String name) {
        return recursiveNames.add(name);
    }

    public NPlaceholder makePlaceholder(NBuilder builder, String name, NState initial) {
        for (var placeholder : placeholders) {
            if (placeholder.name.equals(name) && placeholder.initial == initial) {
                return placeholder;
            }
        }

        var accepted = builder.newState();
        var placeholder = new NPlaceholder(name, initial, accepted);

        placeholders.add(placeholder);

        return placeholder;
    }

    public List<NPlaceholder> getPlaceholders(String name) {
        return placeholders.stream().filter(p -> p.name.equals(name)).collect(Collectors.toUnmodifiableList());
    }

    public NMachine getMachine(String name) {
        return namedMachines.get(name);
    }

    public void setMachine(String name, NMachine machine) {
        // TODO is it ok to override the machine?
//        if (acceptedStates.containsKey(name)) {
//            throw new GramatException("already registered " + name);
//        }

        namedMachines.put(name, machine);
    }

    public void addTransitionHook(Runnable hook) {
        this.transitionHooks.add(hook);
    }

    public void addRecursiveHook(Runnable hook) {
        this.recursiveHooks.add(hook);
    }

    public void addActionHook(Runnable hook) {
        this.actionHooks.add(hook);
    }
}
