package gramat.automata.builder;

class StateData {

    @Override
    public String toString() {
        return "S" + Integer.toHexString(this.hashCode());
    }
}
