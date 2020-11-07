package gramat.framework2;

public interface Process {

    String getName();

    void info(String format, Object... args);
    void warn(String format, Object... args);
    void debug(String format, Object... args);
    void error(String format, Object... args);
    void error(Throwable cause, String format, Object... args);

    void progress(int current, int total);

    void progress(int current);

    ProcessLayer pushLayer(String name);

    void popLayer();

}
