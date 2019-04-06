package org.bakasoft.gramat;

import org.bakasoft.gramat.elements.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParsingErrors {

    private final Tape tape;

    public final ArrayList<Element> lastLogs = new ArrayList<>(); // TODO
    public Location lastSuccess; // TODO

    public ParsingErrors(Tape tape) {
        this.tape = tape;
    }

    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        Location location = getLocation();
        Set<String> symbols = getAllowedSymbols();

        builder.append(location);
        builder.append(": Invalid syntax, expected: " + String.join(", ", symbols));

        return builder.toString();
    }

    public Location getLocation() {
        return lastSuccess != null ? tape.getLocationOf(lastSuccess.getPosition() + 1) : null;
    }

    public Set<String> getAllowedSymbols() {
        HashSet<String> symbols = new HashSet<>();
        HashSet<Element> control = new HashSet<>();

        for (Element element : lastLogs) {
            element.collectFirstAllowedSymbol(control, symbols);
        }

        return symbols;
    }

    public void flush() {
        Location location = tape.getLocation();
        if (lastSuccess == null || location.getPosition() > lastSuccess.getPosition()) {
            lastSuccess = location;
            lastLogs.clear();
        }
    }

    public void log(Element element) {
        Location location = tape.getLocation();

        if (lastSuccess != null && lastSuccess.equals(location)) {
            lastLogs.add(element);
        }
    }
}
