package org.bakasoft.gramat;

public interface GrammarElement {

    void setBeginLocation(Location location);

    Location getBeginLocation();

    void setEndLocation(Location location);

    Location getEndLocation();

}
