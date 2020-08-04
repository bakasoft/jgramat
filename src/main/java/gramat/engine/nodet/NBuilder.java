package gramat.engine.nodet;

import gramat.engine.symbols.Symbol;
import gramat.expressions.Rule;
import gramat.tools.NamedCounts;

import java.util.*;

public class NBuilder {

    public final NLanguage lang;
    public final NamedCounts counts;

    private final List<Runnable> transitionHooks;
    private final List<Runnable> recursiveHooks;
    private final List<Runnable> actionHooks;

    private final List<NFragment> fragments;

    public NBuilder(NLanguage lang) {
        this.lang = lang;
        transitionHooks = new ArrayList<>();
        recursiveHooks = new ArrayList<>();
        actionHooks = new ArrayList<>();
        fragments = new ArrayList<>();
        counts = new NamedCounts();
    }

    public NMachine compile(Rule rule) {
        var builder = new NBuilder(lang);
        var initial = builder.lang.newState();
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

        join_mono_transitions(initial, accepted);

        var machine = new NMachine(rule.name, initial, accepted);

        System.out.println("N-DFA >>>>>>>>>>");
        System.out.println(machine.getAmCode());
        System.out.println("<<<<<<<<<< N-DFA");

        return machine;
    }

    private void join_mono_transitions(NState initial, NState accepted) {
        boolean keepJoining = true;

        while(keepJoining) {
            keepJoining = false;

            for (var i = 0; i < lang.transitions.size(); i++) {
                var trn1 = lang.transitions.get(i);

                if (trn1.target != accepted) {
                    var targetOutgoing = lang.findTransitionsBySource(trn1.target);
                    var targetIncoming = lang.findTransitionsByTarget(trn1.target);

                    targetIncoming.remove(trn1);  // exclude this transition

                    // check if there is a candidate to join
                    if (targetIncoming.isEmpty() && targetOutgoing.size() == 1) {
                        var trn2 = targetOutgoing.get(0);
                        Symbol symbol;

                        if (trn1.symbol != null && trn2.symbol != null) {
                            // skip this candidate: two symbols cannot be joined
                            continue;
                        }
                        else if (trn1.symbol != null) {
                            symbol = trn1.symbol;
                        }
                        else if (trn2.symbol != null) {
                            symbol = trn2.symbol;
                        }
                        else {
                            symbol = null;
                        }

                        // join actions
                        var trn3 = lang.newTransition(trn1.source, trn2.target, symbol, null); // TODO null check? nani?!
                        trn3.actions.addAll(trn1.actions);
                        trn3.actions.addAll(trn2.actions);

                        lang.delete(trn1);
                        lang.delete(trn2);

                        keepJoining = true;
                    }
                }
            }
        }
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
            if (trn.check != null) {
                throw new RuntimeException("expected null-check");
            }

            fragment.targets.add(new NFragment.Target(trn.actions, trn.symbol, trn.target));

            // delete transition and source (won't be used since we are building a fragment)
            trashTransitions.add(trn);
            trashStates.add(trn.source);
        }

        // generate sources for the fragment
        for (var trn : NTool.findIncomingSymbolTransitions(accepted)) {
            if (trn.check != null) {
                throw new RuntimeException("expected null-check");
            }

            fragment.sources.add(new NFragment.Source(trn.source, trn.symbol , trn.actions));

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
