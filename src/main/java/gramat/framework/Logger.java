package gramat.framework;

public interface Logger {

    void log(LogLevel level, String format, Object... args);

    default void error(String format, Object... args) {
        log(LogLevel.ERROR, format, args);
    }

    default void warn(String format, Object... args) {
        log(LogLevel.WARNING, format, args);
    }

    default void info(String format, Object... args) {
        log(LogLevel.INFO, format, args);
    }

    default void debug(String format, Object... args) {
        log(LogLevel.DEBUG, format, args);
    }

}
