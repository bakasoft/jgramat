package gramat.runtime;

public class EditMark extends Edit {

    private final int pos0;
    private final int posF;
    private final String name;

    public EditMark(int pos0, int posF, String name) {
        this.pos0 = pos0;
        this.posF = posF;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mark(" + name + ": " + pos0 + ", " + posF + ")";
    }
}
