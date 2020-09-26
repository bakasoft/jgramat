package gramat.proto;

public class EdgeReference extends Edge {

    public final String name;

    public EdgeReference(Vertex source, Vertex target, String name) {
        super(source, target);
        this.name = name;
    }

}
