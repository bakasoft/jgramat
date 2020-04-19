package gramat.compiling;

import gramat.util.parsing.Location;

public interface ParseContext {

    void warning(String message, Location location);

}
