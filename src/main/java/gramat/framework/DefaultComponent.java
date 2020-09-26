package gramat.framework;

import gramat.Gramat;

public class DefaultComponent implements Component {

    protected final Logger logger;
    protected final Gramat gramat;

    public DefaultComponent(Component parent) {
        this.logger = parent.getLogger();
        this.gramat = parent.getGramat();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Gramat getGramat() {
        return gramat;
    }
}
