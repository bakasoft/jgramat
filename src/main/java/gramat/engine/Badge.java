package gramat.engine;

public class Badge {

    public final int id;

    public Badge(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "badge[" + id + "]";
    }
}
