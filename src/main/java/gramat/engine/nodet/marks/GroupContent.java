package gramat.engine.nodet.marks;

import gramat.engine.nodet.NMark;

public class GroupContent extends NMark {

    public final int number;

    public GroupContent(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Content[" + number + "]";
    }
}
