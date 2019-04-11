package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.LocationRange;

abstract public class GElement {

    public final LocationRange location;
    public final Gramat gramat;

    public GElement(LocationRange location, Gramat gramat) {
        this.location = location;
        this.gramat = gramat;
    }

}
