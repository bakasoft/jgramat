package gramat.automata.nondet;

import gramat.automata.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NLanguage {

    final List<NTransition> transitions;
    final List<NState> states;

    private int nextNumber;

    public NLanguage() {
        this.transitions = new ArrayList<>();
        this.states = new ArrayList<>();
        this.nextNumber = 1;
    }

    public void replace(NState drop, NState keep) {
        if (drop != keep) {
            if (drop.isWild()) {
                keep.makeWild();
            }

            drop.number = keep.number;

            for (var t : transitions) {
                if (t.source == drop) {
                    t.source = keep;
                }
                if (t.target == drop) {
                    t.target = keep;
                }
            }

            states.remove(drop);
        }
    }

    public NState state() {
        var state = new NState(this, nextNumber);
        nextNumber++;
        states.add(state);
        return state;
    }

    NTransition _transition(NState source, NSymbol symbol, NState target) {
        var t = new NTransition(source, symbol, target);
        transitions.add(t);
        return t;
    }



    public void makeDeterministic() {
        boolean again;

        do {
            again = resolveEmptyTransition();
        } while (again);

        print(transitions, "without empty");

        do {
            again = resolveAmbiguousTransitions();
        } while (again);

        do {
            again = resolveWildStates();
        } while (again);

        do {
            again = removeDuplicatedWildTransitions();
        } while (again);
    }

    private boolean resolveWildStates() {
        int hits = 0;

        for (var wild : states) {
            if (wild.isWild()) {
                var next = new ArrayList<NState>();
                var state = wild;
                while (state != null) {
                    var trs = findTransitions(state);

                    if (trs.size() == 1 && trs.get(0).isChar()) {
                        state = trs.get(0).target;

                        next.add(state);
                    }
                    else {
                        state = null;
                    }
                }

                if (next.size() >= 1) {
                    next.remove(next.size() - 1);
                }

                wild.linkWild(wild);
                wild.makeNormal();

                for (var n : next) {
                    n.linkWild(wild);
                }

                hits++;
            }
        }

        return hits > 0;
    }

    private boolean resolveEmptyTransition() {
        NTransition toDelete = null;

        for (var t : transitions) {
            if (t.symbol instanceof NSymbolEmpty) {
                replace(t.source, t.target);
                toDelete = t;
                break;
            }
        }

        if (toDelete != null) {
            transitions.remove(toDelete);
            print(transitions, "removed empty");
            return true;
        }

        return false;
    }

    private void join_transitions(List<NTransition> drop, NTransition keep) {
        for (var tr : drop) {
            replace(tr.source, keep.source);
            replace(tr.target, keep.target);
            transitions.remove(tr);
        }
    }

    private boolean resolveAmbiguousTransitions() {
        int hits = 0;
        var trs = transitions.toArray(NTransition[]::new);
        for (int i = 0; i < trs.length; i++) {
            var iTr = trs[i];

            if (iTr != null) {
                trs[i] = null;

                for (int j = 0; j < trs.length; j++) {
                    var jTr = trs[j];

                    if (jTr != null && jTr.source == iTr.source && jTr.symbol.matches(iTr.symbol)) {
                        trs[j] = null;
                        if (iTr.target != jTr.target) {
                            System.out.println(jTr.target + " >>>> " + iTr.target);
                            replace(jTr.target, iTr.target);
                            hits++;
                        }
                        transitions.remove(jTr);
                        print(transitions, "removed ambiguous");
                    }
                }
            }
        }
        return hits > 0;
    }

    private boolean removeDuplicatedWildTransitions() {
        int hits = 0;

        for (var state : new ArrayList<>(states)) {
            var toDrop = new ArrayList<NTransition>();
            NTransition keep = null;

            for (var tr : findTransitions(state)) {
                if (tr.symbol instanceof NSymbolWild) {
                    if (keep == null) {
                        keep = tr;
                    }
                    else {
                        toDrop.add(tr);
                    }
                }
            }

            if (toDrop.size() > 0) {
                join_transitions(toDrop, keep);
                print(transitions, "make wild");
                hits++;
            }
        }

        return hits > 0;
    }

    public State compile(NState start, NState accept) {
        print(transitions, "compile");
        var statesMap = new HashMap<NState, State>();

        for (var s : states) {
            var st = new State(s.number == accept.number);

            statesMap.put(s, st);
        }

        for (var t : transitions) {
            var source = statesMap.get(t.source);
            var target = statesMap.get(t.target);

            if (source == null || target == null) {
                throw new RuntimeException();
            }

            if (t.symbol instanceof NSymbolChar) {
                var cc = (NSymbolChar) t.symbol;
                source.addTransition(target, cc.value);
            }
            else if (t.symbol instanceof NSymbolRange) {
                var cr = (NSymbolRange) t.symbol;
                source.addTransition(target, cr.begin, cr.end);
            }
            else if (t.symbol instanceof NSymbolWild) {
                source.addTransitionWild(target);
            }
            else if (t.symbol instanceof NSymbolEmpty) {
                throw new UnsupportedOperationException();
            }
            else {
                throw new RuntimeException();
            }
        }

        var result = statesMap.get(start);

        if (result == null) {
            throw new RuntimeException("There is no initial state");
        }

        return result;
    }

    private List<NTransition> findTransitions(NState source) {
        return transitions.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toUnmodifiableList());
    }

    public void print(List<NTransition> trs, String message) {
        System.out.println("------------->>> " + message);
        for (var tr : trs) {
            System.out.println(tr);
        }
        System.out.println("-------------");
    }

}
