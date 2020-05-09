package gramat.automata.builder;

public class StateReference {

    StateData data;

    StateReference(StateData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "R:" + data;
    }
}
