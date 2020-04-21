package gramat.runtime;

public class EditSendSegment extends Edit {

    public final int pos0;
    public final int posF;
    public final String parser;

    public EditSendSegment(int pos0, int posF, String parser) {
        this.pos0 = pos0;
        this.posF = posF;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return "EditSendSegment{" +
                "pos0=" + pos0 +
                ", posF=" + posF +
                ", parser='" + parser + '\'' +
                '}';
    }
}
