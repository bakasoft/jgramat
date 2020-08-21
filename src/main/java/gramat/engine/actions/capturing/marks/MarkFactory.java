package gramat.engine.actions.capturing.marks;

public class MarkFactory {

    public static Mark createMark(int beginPosition) {
        return new PositionMark(beginPosition);
    }

    public static Mark createMark(String name) {
        return new NameMark(name);
    }

}
