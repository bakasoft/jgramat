package gramat.output;

import gramat.expressions.Expression;
import gramat.util.CodeWriter;
import gramat.util.GramatWriter;

import java.util.HashMap;

public class GrammarWriter extends CodeWriter {

    private final HashMap<Object, Integer> objects;
    private int next_id;

    public GrammarWriter(Appendable output) {
        super(output, "  ");
        objects = new HashMap<>();
        next_id = 1;
    }

    public boolean open(Object source, String type) {
        var id = objects.get(source);

        if (id != null) {
            writeChar('*');
            writeString(type);
            writeChar('(');
            writeString(String.valueOf(id));
            writeChar(')');
            prettyBreak();
            return false;
        }

        id = next_id;

        objects.put(source, id);

        next_id++;

        writeChar('+');
        writeString(type);
        writeChar('(');
        writeString(String.valueOf(id));
        writeChar(')');
        prettySeparator();
        writeChar('{');
        prettyBreak();
        indent(+1);
        return true;
    }

    public void close() {
        indent(-1);
        writeChar('}');
        prettyBreak();
    }

    public void write(Expression... expressions) {
        if (expressions != null) {
            for (var expr : expressions) {
                if (expr != null) {
                    expr.write(this);
                }
            }
        }
    }

    public void attribute(String name, Object value) {
        writeChar('-');
        writeString(name);
        writeChar('(');

        if (value instanceof Expression) {
            indent(+1);
            prettyBreak();
            write((Expression)value);
            indent(-1);
        }
        else if (value == null) {
            writeString("null");
        }
        else {
            // TODO format values
            writeDelimitedString(String.valueOf(value), '\"');
        }

        writeChar(')');
        prettyBreak();
    }
}
