package gramat.engine.nodet;

public class NBuilder extends NContainer {

    public final NMaker maker;

    public NBuilder(NMaker maker) {
        this(maker, null);
    }

    public NBuilder(NBuilder parent) {
        this(parent.maker, parent);
    }

    private NBuilder(NMaker maker, NBuilder parent) {
        super(maker.root, parent);
        this.maker = maker;
    }

}
