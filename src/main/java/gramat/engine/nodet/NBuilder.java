package gramat.engine.nodet;

import gramat.engine.checks.CheckSource;
import gramat.engine.symbols.Symbol;
import gramat.engine.symbols.SymbolSource;
import gramat.expressions.Rule;
import gramat.tools.NamedCounts;

import java.util.*;

public class NBuilder {

    public final NLanguage lang;
    public final NamedCounts counts;
    public final CheckSource checks;
    public final SymbolSource symbols;

    private final List<Runnable> transitionHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    private final List<NFragment> fragments;

    public NBuilder(NLanguage lang) {
        this.lang = lang;
        checks = new CheckSource();
        symbols = new SymbolSource();
        transitionHooks = new ArrayList<>();
        recursiveHooks = new ArrayList<>();
        actionHooks = new ArrayList<>();
        fragments = new ArrayList<>();
        counts = new NamedCounts();
    }

    public NMachine compile(Rule rule) {
        var initial = lang.newState();
        var accepted = rule.build(this, initial);

        for (var hook : transitionHooks) {
            hook.run();
        }

        for (var hook : recursiveHooks) {
            hook.run();
        }

        for (var hook : actionHooks) {
            hook.run();
        }

        var machine = new NMachine(rule.name, initial, accepted);

        System.out.println("N-DFA >>>>>>>>>>");
        System.out.println(machine.getAmCode());
        System.out.println("<<<<<<<<<< N-DFA");

        return machine;
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
        var initial = lang.newState();

        // build expression normally (infinite recursion should be handled at this point)
        var accepted = rule.expression.build(this, initial);

        // create trash collectors
        var trashTransitions = new HashSet<NTransition>();
        var trashStates = new HashSet<NState>();

        // generate targets for the fragment
        for (var trn : NTool.findOutgoingSymbolTransitions(initial)) {
            fragment.targets.add(new NFragment.Target(trn.actions, trn.symbol, trn.target));

            // delete transition and source (won't be used since we are building a fragment)
            trashTransitions.add(trn);
            trashStates.add(trn.source);
        }

        // generate sources for the fragment
        for (var trn : NTool.findIncomingSymbolTransitions(accepted)) {
            fragment.sources.add(new NFragment.Source(trn.source, trn.symbol, trn.actions));

            // delete transition and target (won't be used since we are building a fragment)
            trashTransitions.add(trn);
            trashStates.add(trn.target);
        }

        // empty trash collectors
        for (var trn : trashTransitions) {
            lang.delete(trn);
        }
        for (var state : trashStates) {
            lang.delete(state);
        }

        fragment.ready = true;

        return fragment;
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
