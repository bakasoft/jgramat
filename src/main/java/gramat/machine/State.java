package gramat.machine;

public class State {

    public final String id;
    public final Transition transition;

    public boolean accepted;

    public State(String id) {
        this.id = id;
        this.transition = new Transition();
    }

}
