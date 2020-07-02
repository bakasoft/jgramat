package gramat.engine.nodet;

public class NPlaceholder {

    public final String name;
    public final NState initial;
    public final NState accepted;

    public NPlaceholder(String name, NState initial, NState accepted) {
        this.name = name;
        this.initial = initial;
        this.accepted = accepted;
    }
}
