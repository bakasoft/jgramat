package gramat.pivot.data;

import gramat.pivot.XTransitionData;

import java.io.PrintStream;

public class TDReference extends XTransitionData {

    public String reference;

    public TDReference(String reference) {
        this.reference = reference;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("REF " + reference);
    }
}
