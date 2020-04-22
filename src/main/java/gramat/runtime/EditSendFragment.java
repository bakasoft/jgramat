package gramat.runtime;

public class EditSendFragment extends Edit {

    public final String fragment;

    public EditSendFragment(String fragment) {
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return "EditSendSegment{" +
                "fragment='" + fragment + "'}";
    }
}
