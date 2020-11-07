package gramat.framework;

public interface Context {

    String getName();

    void info(String format, Object... args);
    void warn(String format, Object... args);
    void debug(String format, Object... args);
    void error(String format, Object... args);
    void error(Throwable cause);

    void setStatus(String format, Object... args);

    void setTotal(int current, int total);

    void setProgress(int current);

    void addProgress(int count);

    void clear();

    ContextLayer pushLayer(String name);

    void popLayer();

}
