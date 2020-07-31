package gramat.engine.nodet;

import gramat.engine.actions.Action;
import gramat.expressions.Rule;
import gramat.tools.NamedCounts;

import java.util.*;

public class NBuilder {

    public final NRoot root;
    public final NamedCounts counts;

    private final List<Runnable> transitionHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    private final List<NFragment> fragments;

    private final List<NGroup> groups;

    public NBuilder(NRoot root) {
        this.root = root;
        transitionHooks = new ArrayList<>();
        recursiveHooks = new ArrayList<>();
        actionHooks = new ArrayList<>();
        groups = new ArrayList<>();
        fragments = new ArrayList<>();
        counts = new NamedCounts();
    }

    public NFragment makeFragment(Rule rule) {
        // search in public fragments to avoid infinite recursion
        for (var fragment : fragments) {
            if (Objects.equals(fragment.name, rule.name)) {
                return fragment;
            }
        }

        var fragment = new NFragment(rule.name);

        // make it public so infinite recursion is handled
        fragments.add(fragment);

        // create isolated state for the fragment
        var initial = root.newState();

        // build expression normally (infinite recursion should be handled at this point)
        var accepted = rule.expression.build(this, initial);

        // create trash collectors
        var trashTransitions = new HashSet<NTransition>();
        var trashStates = new HashSet<NState>();

        // generate targets for the fragment
        for (var trn : NTool.findOutgoingSymbolTransitions(initial)) {
            // TODO what about non-null checks? we shouldn't ignore them
            fragment.targets.add(new NFragment.Target(trn.getSymbol(), trn.target));

            // delete transition and source (won't be used since we are building a fragment)
            trashTransitions.add(trn);
            trashStates.add(trn.source);
        }

        // generate sources for the fragment
        for (var trn : NTool.findIncomingSymbolTransitions(accepted)) {
            // TODO what about non-null checks? we shouldn't ignore them
            fragment.sources.add(new NFragment.Source(trn.source, trn.getSymbol()));

            // delete transition and target (won't be used since we are building a fragment)
            trashTransitions.add(trn);
            trashStates.add(trn.target);
        }

        // empty trash collectors
        for (var trn : trashTransitions) {
            root.delete(trn);
        }
        for (var state : trashStates) {
            root.delete(state);
        }

        fragment.ready = true;

        return fragment;
    }

    public NMachine compile(Rule rule) {
        var builder = new NBuilder(root);
        var initial = builder.root.newState();
        var accepted = rule.build(builder, initial);

        for (var hook : builder.transitionHooks) {
            hook.run();
        }

        for (var hook : builder.recursiveHooks) {
            hook.run();
        }

        for (var hook : builder.actionHooks) {
            hook.run();
        }

        var machine = new NMachine(rule.name, initial, accepted);

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
