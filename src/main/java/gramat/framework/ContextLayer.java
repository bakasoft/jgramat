package gramat.framework;

public interface ContextLayer extends AutoCloseable {

    String getName();

    @Override
    void close();

}
