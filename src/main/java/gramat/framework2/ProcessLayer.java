package gramat.framework2;

public interface ProcessLayer extends AutoCloseable {

    String getName();

    @Override
    void close();

}
