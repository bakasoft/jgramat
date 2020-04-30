package gramat.runtime;

import gramat.compiling.ValueParser;
import gramat.util.parsing.Location;
import gramat.util.parsing.Source;

public class EditSendSegment extends Edit {

    public final int pos0;
    public final int posF;
    public final ValueParser parser;

    public EditSendSegment(Source source, int position, int pos0, int posF, ValueParser parser) {
        super(source, position);
        this.pos0 = pos0;
        this.posF = posF;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return "EditSendSegment{" +
                "pos0=" + pos0 +
                ", posF=" + posF +
                ", parser='" + parser + '\'' +
                '}';
    }
}
