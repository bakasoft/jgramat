package gramat.automata.builder;

public class StateBuilder {

    private final AutomatonBuilder builder;

    protected Boolean accepted;

    public StateBuilder(AutomatonBuilder builder) {
        this.builder = builder;
    }

    public void makeAccepted() {
        if (accepted == null) {
            accepted = builder.acceptedValue;
        }
        else if (accepted != builder.acceptedValue) {
            throw new RuntimeException();
        }
    }

    public void makeRejected() {
        if (accepted == null) {
            accepted = builder.rejectedValue;
        }
        else if (accepted != builder.rejectedValue) {
            throw new RuntimeException();
        }
    }
}
