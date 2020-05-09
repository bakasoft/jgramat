package gramat.automata.builder;

class TransitionData {

    StateData source;
    Condition condition;
    StateData target;

    TransitionData(StateData source, Condition condition, StateData target) {
        this.source = source;
        this.condition = condition;
        this.target = target;
    }

    @Override
    public String toString() {
        return source + " -> " + condition + " -> " + target;
    }
}
