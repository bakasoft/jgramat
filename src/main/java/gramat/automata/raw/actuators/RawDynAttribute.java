package gramat.automata.raw.actuators;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.dynamicAttribute.DynamicAttributeCancel;
import gramat.eval.dynamicAttribute.DynamicAttributeSave;
import gramat.eval.dynamicAttribute.DynamicAttributeStart;

public class RawDynAttribute extends RawAutomaton {

    private final RawAutomaton name;
    private final RawAutomaton content;

    public RawDynAttribute(RawAutomaton name, RawAutomaton content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawDynAttribute(name.collapse(), content.collapse());
    }

    @Override
    public NAutomaton build(Language lang) {
        var am = lang.automaton((initialSet, acceptedSet) -> {
            var amName = name.build(lang);
            var amContent = content.build(lang);

            lang.transition(amName.accepted, amContent.initial, null);

            initialSet.add(amName.initial);
            acceptedSet.add(amContent.accepted);
        });

        var start = new DynamicAttributeStart();
        var save = new DynamicAttributeSave(start);
        var cancel = new DynamicAttributeCancel(start);
        lang.postBuild(() -> TRX.setupActions(am, start, save, cancel));
        return am;
    }
}
