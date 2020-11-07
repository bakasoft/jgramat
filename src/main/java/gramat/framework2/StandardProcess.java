package gramat.framework2;

import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class StandardProcess implements Process {

    private final String globalName;
    private final PrintStream output;
    private final Deque<Layer> layers;

    private long lastProgressLog;
    private String lastLevel;
    private String lastMessage;

    public StandardProcess(String name, PrintStream output) {
        this.globalName = name;
        this.output = output;
        this.layers = new ArrayDeque<>();

        layers.push(new Layer(name));
    }

    private Layer layer() {
        return layers.getFirst();
    }

    private void log(String level, String message) {
        var layer = layer();
        var result = new StringBuilder();

        result.append('[');
        result.append(Instant.now());
        result.append(' ');
        result.append(layer.name);
        result.append(' ');
        result.append(level);
        result.append("] ");
        result.append(message);

        if (layer.total != null || layer.current != null) {
            result.append(" (");
            result.append(Objects.requireNonNullElse(layer.current, "---"));
            result.append('/');
            result.append(Objects.requireNonNullElse(layer.total, "---"));
            result.append(')');
        }

        output.println(result);

        lastLevel = level;
        lastMessage = message;
    }

    @Override
    public String getName() {
        return globalName;
    }

    @Override
    public void info(String format, Object... args) {
        log("INFO", String.format(format, args));
    }

    @Override
    public void warn(String format, Object... args) {
        log("WARN", String.format(format, args));
    }

    @Override
    public void debug(String format, Object... args) {
        log("DEBUG", String.format(format, args));
    }

    @Override
    public void error(String format, Object... args) {
        log("ERROR", String.format(format, args));
    }

    @Override
    public void error(Throwable cause, String format, Object... args) {
        log("ERROR", String.format(format, args));
        cause.printStackTrace(output);
    }

    @Override
    public void progress(int current, int total) {
        var layer = layer();
        layer.current = current;
        layer.total = total;
        tryPrintProgress();
    }

    @Override
    public void progress(int current) {
        var layer = layer();
        layer.current = current;
        tryPrintProgress();
    }

    private void tryPrintProgress() {
        var now = System.currentTimeMillis();

        if (now - lastProgressLog > 100) {
            log(Objects.requireNonNullElse(lastLevel, "INFO"), Objects.requireNonNullElse(lastMessage, ""));
            lastProgressLog = System.currentTimeMillis();
        }
    }

    @Override
    public ProcessLayer pushLayer(String name) {
        var layer = new Layer(name);

        layers.push(layer);

        return layer;
    }

    @Override
    public void popLayer() {
        if (layers.size() == 1) {
            throw new RuntimeException();
        }
        layers.pop();
    }

    private class Layer implements ProcessLayer {

        final String name;

        Integer total;

        Integer current;

        Layer(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void close() {
            popLayer();
        }
    }

}
