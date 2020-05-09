package gramat.compiling;

import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
import gramat.util.GramatWriter;
import gramat.util.parsing.Location;
import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

import java.util.HashMap;

public class GrammarTest {

    private final Location location;
    private final String expressionName;
    private final Source source;
    private final boolean expectedMatch;

    public GrammarTest(Location location, String expressionName, Source source, boolean expectedMatch) {
        this.location = location;
        this.expressionName = expressionName;
        this.source = source;
        this.expectedMatch = expectedMatch;
    }

    public void run(Compiler compiler) {
        System.out.println("Testing " + expressionName + " <- " + location);

        Expression expression = compiler.compileRule(expressionName);

        if (expression == null) {
            throw new ParseException("Expression not found: " + expressionName, location);
        }

        EvalContext evalContext = new EvalContext(source, new HashMap<>());

        boolean matches = expression.eval(evalContext);

        if (expectedMatch && !matches) {
            var errLoc = source.locationOf(evalContext.lastCommitPosition);
            throw new ParseException(expressionName + " did not match: " + evalContext.lastCommitName, errLoc);
        }
        else if (!expectedMatch && matches) {
            throw new ParseException("Expected to fail: " + expressionName, location);
        }

        // TODO check eval result
        var writer = new GramatWriter(System.out, "  ");

        if (expression.hasValues()) {
            writer.writeValue(evalContext.getValue());
        }
    }

    public String getExpressionName() {
        return expressionName;
    }

    public Source getSource() {
        return source;
    }

    public boolean getExpectedMatch() {
        return expectedMatch;
    }

    public Location getLocation() {
        return location;
    }
}
