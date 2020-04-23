package gramat.compiling;

import gramat.expressions.Expression;
import gramat.runtime.EvalContext;
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

    public void run(LinkContext context) {
        System.out.println("Testing " + expressionName + " <- " + location);

        Expression expression = context.getExpression(expressionName);

        if (expression == null) {
            throw new ParseException("Expression not found: " + expressionName, location);
        }

        EvalContext evalContext = new EvalContext(source, new HashMap<>());

        boolean matches = expression.eval(evalContext);

        if (expectedMatch && !matches) {
            throw new ParseException("Expected to pass: " + expressionName, location);
        }
        else if (!expectedMatch && matches) {
            throw new ParseException("Expected to fail: " + expressionName, location);
        }

        // TODO check eval result
        System.out.println(
                evalContext.getValue());
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
