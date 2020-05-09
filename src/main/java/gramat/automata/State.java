package gramat.automata;

import gramat.output.GrammarWriter;

import java.util.*;

public class State {

    private final boolean accepted;
    private final List<Transition> transitions;

    public State(boolean accepted) {
        this.accepted = accepted;
        this.transitions = new ArrayList<>();
    }

    public State move(char value) {
        State fallback = null;

        for (var t : transitions) {
            if (t.isWild()) {
                fallback = t.state;
            }
            else if (t.test(value)) {
                return t.state;
            }
        }

        return fallback;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void addTransition(State target, char value) {
        // TODO validate transitions
        transitions.add(new CharTransition(target, value));
    }

    public void addTransitionWild(State target) {
        // TODO validate transitions
        transitions.add(new WildTransition(target));
    }

    public void addTransition(State target, char begin, char end) {
        // TODO validate transitions
        transitions.add(new RangeTransition(target, begin, end));
    }

    private List<Transition> findTransitionsFor(char value) {
        var matching = new ArrayList<Transition>();

        for (var tr : transitions) {
            if (tr instanceof CharTransition) {
                if (tr.test(value)) {
                    matching.add(tr);
                }
            }
            else if (tr instanceof RangeTransition) {
                var rt = (RangeTransition) tr;

                if (value >= rt.begin && value <= rt.end) {
                    matching.add(rt);
                }
            }
            else {
                throw new RuntimeException();
            }
        }

        return matching;
    }

    private List<Transition> findTransitionsFor(char begin, char end) {
        var matching = new ArrayList<Transition>();

        for (var tr : transitions) {
            if (tr instanceof CharTransition) {
                var ct = (CharTransition) tr;

                if (ct.value == begin && ct.value == end) {
                    matching.add(ct);
                }
            }
            else if (tr instanceof RangeTransition) {
                var rt = (RangeTransition) tr;

                if (rt.begin <= begin && rt.end >= end) {
                    matching.add(rt);
                }
            }
        }

        return matching;
    }

    public boolean isFinal() {
        return transitions.isEmpty();
    }

    public void write(GrammarWriter writer) {
        if (writer.open(this, "state")) {
            writer.attribute("accepted", accepted);
            for (var t : transitions) {
                t.write(writer);
            }
            writer.close();
        }
    }
}
