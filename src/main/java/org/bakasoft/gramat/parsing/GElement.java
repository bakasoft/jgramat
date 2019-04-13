package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaEntity;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

abstract public class GElement {

    public final LocationRange location;
    public final Gramat gramat;

    public GElement(LocationRange location, Gramat gramat) {
        this.location = location;
        this.gramat = gramat;
    }

}
