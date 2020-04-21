package gramat.runtime;

public class EditSet extends Edit {

    public final String name;

    public EditSet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EditSet{" +
                "name='" + name + '\'' +
                '}';
    }
}
