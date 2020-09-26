package gramat.framework;

import gramat.GramatConfig;

import java.io.PrintStream;

public class PrintStreamLogger extends DefaultComponent implements Logger {

    private final PrintStream out;
    private final PrintStream err;

    public PrintStreamLogger(Component parent, PrintStream out, PrintStream err) {
        super(parent);
        this.out = out;
        this.err = err;
    }

    @Override
    public void log(LogLevel level, String format, Object... args) {
        var printLevel = gramat.config.get(GramatConfig.LOG_LEVEL, LogLevel.class);

        if (level.ordinal() >= printLevel.ordinal()) {
            switch (level) {
                case ERROR:
                case WARNING:
                    err.printf(prefix(level) + format + "%n", args);
                    break;
                case INFO:
                default:
                case DEBUG:
                    out.printf(prefix(level) + format + "%n", args);
                    break;
            }
        }
    }

    private String prefix(LogLevel level) {
        return level.name() + ": ";
    }
}
