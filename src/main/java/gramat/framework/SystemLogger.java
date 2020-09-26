package gramat.framework;

public class SystemLogger extends PrintStreamLogger {

    public SystemLogger(Component parent) {
        super(parent, System.out, System.err);
    }
}
