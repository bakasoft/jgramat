package gramat.engine.nodet;

import gramat.GramatException;
import gramat.engine.actions.Action;
import gramat.expressions.Expression;

import java.util.*;
import java.util.stream.Collectors;

public class NBuilder {

    public final NRoot root;

    private final List<Runnable> transitionHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    private final List<NPlaceholder> placeholders;
    private final List<NMachine> machines;
    private final Set<String> recursiveNames;

    private final List<NGroup> groups;

    public NBuilder(NRoot root) {
        this.root = root;
        transitionHooks = new ArrayList<>();
        recursiveHooks = new ArrayList<>();
        actionHooks = new ArrayList<>();
        placeholders = new ArrayList<>();
        machines = new ArrayList<>();
        recursiveNames = new HashSet<>();
        groups = new ArrayList<>();
    }

    public NMachine compile(String name, Expression expression) {
        var builder = new NBuilder(root);
        var machine = expression.buildOnce(builder, name);

        for (var hook : builder.transitionHooks) {
            hook.run();
        }

        for (var hook : builder.recursiveHooks) {
            hook.run();
        }

        for (var hook : builder.actionHooks) {
            hook.run();
        }

        // recreate machine with all states and transitions created by the hooks
        System.out.println("NDFA >>>>>>>>>>");
        System.out.println(machine.getAmCode());
        System.out.println("<<<<<<<<<< NDFA");

        return machine;
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

        var accepted = root.newState();
        var placeholder = new NPlaceholder(name, initial, accepted);

        placeholders.add(placeholder);

        return placeholder;
    }

    public List<NPlaceholder> getPlaceholders(String name) {
        return placeholders.stream().filter(p -> p.name.equals(name)).collect(Collectors.toUnmodifiableList());
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

    public NMachine getMachine(String name) {
        for (var machine : machines) {
            if (Objects.equals(machine.name, name)) {
                return machine;
            }
        }
        return null;
    }

    public void addMachine(NMachine machine) {
        if (getMachine(machine.name) != null) {
            throw new GramatException("already registered: " + machine.name);
        }
        machines.add(machine);
    }

}
