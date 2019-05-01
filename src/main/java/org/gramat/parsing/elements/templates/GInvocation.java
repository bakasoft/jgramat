package org.gramat.parsing.elements.templates;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpressionNC;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GInvocation extends GExpressionNC {

    public final String functionName;

    public GInvocation(LocationRange location, Gramat gramat, String functionName, GExpression[] expressions) {
        super(location, gramat, expressions);
        this.functionName = functionName;
    }

    @Override
    public GExpression simplify(GExpression[] simpleExpressions) {
        return new GInvocation(location, gramat, functionName, GExpression.simplifyAll(simpleExpressions));
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }

    @Override
    public void validate_r(GControl control) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        throw new GrammarException("Function invocation is not implemented yet.", location);
    }
}
