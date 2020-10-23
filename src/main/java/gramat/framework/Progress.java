package gramat.framework;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class Progress implements AutoCloseable {

    private final String title;
    private final Instant startTime;
    private final AtomicBoolean active;

    private String message;

    private int total;
    private int value;

    public Progress(String title) {
        this.title = title;
        this.active = new AtomicBoolean(true);

        new Thread(() -> {
            while (active.get()) {
                print();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        log("Initializing...");

        startTime = Instant.now();
    }

    public void log(String message, Object... args) {
        this.message = String.format(message, args);
    }

    public void log(int value, String message, Object... args) {
        this.message = String.format(message, args);
        this.value = value;
    }

    public void log(int value, int total, String message, Object... args) {
        this.message = String.format(message, args);
        this.value = value;
        this.total = total;
    }

    public void log(int value) {
        this.value = value;
    }

    public void log(int value, int total) {
        this.value = value;
        this.total = total;
    }

    public void add(int delta) {
        this.value += delta;
    }

    public void add(int delta, String message, Object... args) {
        this.value += delta;
        this.message = String.format(message, args);
    }

    @Override
    public void close() {
        var end = Instant.now();
        var seconds = Duration.between(startTime, end).toMillis();

        active.set(false);

        log("Finalized (%s ms)", seconds);
        print();
    }

    private void print() {
        System.out.print(title);

        if (total != 0) {
            System.out.print(" " + value + "/" + total);
        }

        if (message != null) {
            System.out.print(": " + message);
        }

        System.out.println();
    }
}
