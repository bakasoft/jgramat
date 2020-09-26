package gramat.parsers;

abstract public class NamedParser implements ValueParser {

    private final String name;

    public NamedParser(String name) {
         this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
