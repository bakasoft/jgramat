package gramat.automata.raw.actuators;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.Action;
import gramat.eval.object.ObjectCancel;
import gramat.eval.object.ObjectSave;
import gramat.eval.object.ObjectStart;
import gramat.eval.staticAttribute.StaticAttributeCancel;
import gramat.eval.staticAttribute.StaticAttributeSave;
import gramat.eval.staticAttribute.StaticAttributeStart;

public class RawAttribute extends RawAutomaton {

    private final RawAutomaton content;
    private final String name;

    public RawAttribute(RawAutomaton content, String name) {
        this.content = content;
        this.name = name;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawAttribute(content.collapse(), name);
    }

    @Override
    public NAutomaton build(Language lang) {
        var am = content.build(lang);
        var start = new StaticAttributeStart();
        var save = new StaticAttributeSave(start, name);
        var cancel = new StaticAttributeCancel(start);
        lang.postBuild(() -> TRX.setupActions(am, start, save, cancel));
        return am;
    }
}
