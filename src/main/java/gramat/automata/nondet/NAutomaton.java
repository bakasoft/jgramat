package gramat.automata.nondet;

import gramat.automata.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NAutomaton {

    final NLanguage lang;

    public NState start;
    public NState reject;
    public NState accept;

    NAutomaton(NLanguage lang, NState start, NState reject, NState accept) {
        this.lang = lang;
        this.start = start;
        this.accept = accept;
        this.reject = reject;
    }

    public void makeDeterministic() {
        boolean again;

        do {
            again = resolveEmptyTransition();
        } while (again);

        print(lang.transitions, "without empty");

        do {
            again = resolveCancelledTransitions();
            again |= resolveAmbiguousTransitions();
        } while (again);

        do {
            again = resolveWildTransitions();
        } while (again);
    }

    private boolean resolveEmptyTransition() {
        NTransition toDelete = null;

        for (var t : lang.transitions) {
            if (t.symbol instanceof NSymbolEmpty) {
                lang.replace(t.target, t.source);
                toDelete = t;
                break;
            }
        }

        if (toDelete != null) {
            lang.transitions.remove(toDelete);
//            print(lang.transitions, "removed empty");
            return true;
        }

        return false;
    }

    private void join_transitions(List<NTransition> drop, NTransition keep) {
        for (var tr : drop) {
            lang.replace(tr.source, keep.source);
            lang.replace(tr.target, keep.target);
            lang.transitions.remove(tr);
        }
    }

    private boolean resolveCancelledTransitions() {
        var transitions = lang.transitions.toArray(NTransition[]::new);

        for (var trNC : transitions) {
            if (trNC.symbol instanceof NSymbolNotChar) {
                var value = ((NSymbolNotChar)trNC.symbol).value;

                for (var trC : transitions) {
                    if (trC.symbol instanceof NSymbolChar &&  trC.source == trNC.source && trC.target == trNC.target) {
                        if (((NSymbolChar)trC.symbol).value == value) {
                            lang.transitions.remove(trC);
                            lang.transitions.remove(trNC);
                            //print(lang.transitions, "removed cancelled");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean resolveAmbiguousTransitions() {
        int hits = 0;
        var transitions = lang.transitions.toArray(NTransition[]::new);
        for (int i = 0; i < transitions.length; i++) {
            var iTr = transitions[i];

            if (iTr != null) {
                transitions[i] = null;

                for (int j = 0; j < transitions.length; j++) {
                    var jTr = transitions[j];

                    if (jTr != null && jTr.source == iTr.source && jTr.symbol.matches(iTr.symbol)) {
                        transitions[j] = null;
                        if (iTr.target != jTr.target) {
                            System.out.println(jTr.target + " >>>> " + iTr.target);
                            lang.replace(jTr.target, iTr.target);
                            hits++;
                        }
                        lang.transitions.remove(jTr);
                        print(lang.transitions, "removed ambiguous");
                    }
                }
            }
        }
        return hits > 0;
    }

    private boolean resolveWildTransitions() {
        int hits = 0;

        for (var state : new ArrayList<>(lang.states)) {
            var toMakeWild = new ArrayList<NTransition>();
            NTransition keep = null;

            for (var tr : findTransitions(state)) {
                if (tr.symbol instanceof NSymbolNotChar) {
                    toMakeWild.add(tr);
                }
                else if (tr.symbol instanceof NSymbolWild) {
                    if (keep == null) {
                        keep = tr;
                    }
                    else {
                        toMakeWild.add(tr);
                    }
                }
            }

            if (toMakeWild.size() > 0) {
                if (keep == null) {
                    var first = toMakeWild.get(0);

                    keep = lang._transition(first.source, new NSymbolWild(), first.target);
                }

                join_transitions(toMakeWild, keep);
                print(lang.transitions, "make wild");
                hits++;
            }
        }

        return hits > 0;
    }

    public State compile() {
        print(lang.transitions, "compile");
        return compile(start, accept);
    }

    private State compile(NState start, NState accept) {
        var statesMap = new HashMap<NState, State>();

        for (var s : lang.states) {
            var st = new State(s == accept);

            statesMap.put(s, st);
        }

        for (var t : lang.transitions) {
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
            else if (t.symbol instanceof NSymbolNotChar) {
                var nc = (NSymbolNotChar) t.symbol;
                source.addTransitionNot(target, nc.value);
            }
            else if (t.symbol instanceof NSymbolWild) {
                source.addTransitionWild(target);
            }
            else if (t.symbol instanceof NSymbolEmpty) {
                throw new RuntimeException();
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
        return lang.transitions.stream()
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
