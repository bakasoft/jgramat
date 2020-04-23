package gramat.runtime;

import gramat.util.parsing.Location;

public class EditSendFragment extends Edit {

    public final String fragment;

    public EditSendFragment(Location location, String fragment) {
        super(location);
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return "EditSendSegment{" +
                "fragment='" + fragment + "'}";
    }
}
