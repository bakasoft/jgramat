package gramat.machine.binary;

import gramat.Gramat;
import gramat.binary.*;
import gramat.models.automata.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelRepository extends Repository {

    private final Gramat gramat;
    private final List<ModelState> states;

    public ModelRepository() {
        gramat = new Gramat();
        states = new ArrayList<>();

        initMachine(createEditor(1, ModelMachine.class));
        initState(createEditor(2, ModelState.class));
    }

    public Gramat getGramat() {
        return gramat;
    }

    private ModelState findState(String id) {
        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                return state;
            }
        }

        throw new RuntimeException();
    }

    private void initMachine(DefaultObjectEditor<ModelMachine> editor) {
        editor.addField(1,
                (machine, value) -> machine.initial = findState(value.asString()),
                machine -> Value.of(machine.initial.id));
        editor.addField(2,
                (machine, value) -> machine.transitions = value.asListOf(ModelTransition.class),
                machine -> Value.of(machine.transitions));
        editor.addField(3,
                (machine, value) -> machine.states = value.asListOf(ModelState.class),
                machine -> Value.of(machine.states));
    }

    private void initState(DefaultObjectEditor<ModelState> editor) {
        editor.addField(1,
                (state, value) -> state.id = value.asString(),
                state -> Value.of(state.id));
        editor.addField(2,
                (state, value) -> state.accepted = value.asBoolean(),
                state -> Value.of(state.accepted));
    }

}
