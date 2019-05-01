package org.gramat.parsing;

import org.gramat.Gramat;
import org.gramat.LocationRange;

abstract public class GElement {

    public final LocationRange location;
    public final Gramat gramat;

    public GElement(LocationRange location, Gramat gramat) {
        this.location = location;
        this.gramat = gramat;
    }

}
