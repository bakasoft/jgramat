package gramat.automata.raw;

import gramat.automata.ndfa.DState;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NState;

import java.util.HashSet;
import java.util.Set;


public class RawRepetitionAutomaton extends RawAutomaton {

    private final RawAutomaton content;
    private final RawAutomaton separator;
    private final Integer minimum;
    private final Integer maximum;

    public RawRepetitionAutomaton(RawAutomaton content, RawAutomaton separator, Integer minimum, Integer maximum) {
        this.content = content;
        this.separator = separator;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(
                content.collapse(),
                separator != null ? separator.collapse() : null,
                minimum,
                maximum
        );
    }

    @Override
    public NAutomaton build(Language lang) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min == 0 && max == 0 && separator == null) {  // unbounded loop without separator
            return lang.automaton((initialSet, acceptedSet) -> {
                var root = lang.state();
                initialSet.add(root);
                acceptedSet.add(root);

                var amMain = content.build(lang);

                lang.transition(root, amMain.initial, null);
                lang.transition(amMain.accepted, root, null);
            });
        }
        else if (min == 0 && max == 0) { // unbounded loop WITH separator
            return lang.automaton((initialSet, acceptedSet) -> {
                var amMain = content.build(lang);
                var initial = initialSet.create();
                var accepted = acceptedSet.create();

                acceptedSet.add(initial);

                lang.transition(initial, amMain.initial, null);
                lang.transition(amMain.accepted, accepted, null);

                var amLoopSep = separator.build(lang);
                var amLoopCon = content.build(lang);

                lang.transition(accepted, amLoopSep.initial, null);
                lang.transition(amLoopSep.accepted, amLoopCon.initial, null);
                lang.transition(amLoopCon.accepted, accepted, null);
            });
        }
        else if (min == 1 && max == 0) {
            return lang.automaton((initialSet, acceptedSet) -> {
                var amMain = content.build(lang);
                if (separator != null) {
                    var amSep = separator.build(lang);
                    lang.transition(amMain.accepted, amSep.initial, null);
                    lang.transition(amSep.accepted, amMain.initial, null);
                }
                else {
                    lang.transition(amMain.accepted, amMain.initial, null);
                }

                initialSet.add(amMain.initial);
                acceptedSet.add(amMain.accepted);
            });
        }
        else {
            throw new RuntimeException("not implemented: min=" + min + ", max=" + max + ", sep=" + (separator != null));
        }
    }

}
