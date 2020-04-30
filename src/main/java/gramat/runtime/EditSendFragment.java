package gramat.runtime;

import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditSendFragment extends Edit {

    public final String fragment;

    public EditSendFragment(Source source, int position, String fragment) {
        super(source, position);
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return "EditSendSegment{" +
                "fragment='" + fragment + "'}";
    }
}
