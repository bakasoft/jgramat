package gramat.proto;

public class LinkReference extends Link {

    public final String name;

    public LinkReference(Node source, Node target, String name) {
        super(source, target);
        this.name = name;
    }

}
