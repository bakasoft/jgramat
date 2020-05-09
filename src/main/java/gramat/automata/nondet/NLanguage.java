package gramat.automata.nondet;

import java.util.ArrayList;
import java.util.List;

public class NLanguage {

    final List<NTransition> transitions;
    final List<NAutomaton> automata;
    final List<NState> states;

    private int nextNumber;

    public NLanguage() {
        this.transitions = new ArrayList<>();
        this.states = new ArrayList<>();
        this.automata = new ArrayList<>();
        this.nextNumber = 1;
    }

    public void replace(NState drop, NState keep) {
        if (drop != keep) {
            for (var t : transitions) {
                if (t.source == drop) {
                    t.source = keep;
                }
                if (t.target == drop) {
                    t.target = keep;
                }
            }
            for (var a : automata) {
                if (a.start == drop) {
                    a.start = keep;
                }
                if (a.accept == drop) {
                    a.accept = keep;
                }
            }
            states.remove(drop);
        }
    }

    public NAutomaton automaton(NState start, NState reject, NState accept) {
        var am = new NAutomaton(this, start, reject, accept);
        automata.add(am);
        return am;
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

}
