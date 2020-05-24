package gramat.automata.ndfa;

public interface NMachineBuilder {

    void build(NContext context, NStateSet initial, NStateSet accepted);

}
