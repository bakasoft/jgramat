package gramat.engine.deter;

public class DState {

    public final int id;
    public final boolean accepted;

    /*
     * TODO: Confirm if transitions should be sorted (and implement it), example:
     *  1. Symbol+Check transitions
     *  2. Char transitions
     *  3. Range transitions
     *  4. Wild transitions
     *  This may affect how the automaton is executed.
     * TODO: Validate if range transitions are overlapped.
     */
    public DTransition[] transitions;

    public DState(int id, boolean accepted) {
        this.id = id;
        this.accepted = accepted;
        this.transitions = null;
    }

}
