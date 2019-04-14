package org.bakasoft.gramat.parsing.elements.templates;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpressionNC;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

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
        return new GInvocation(location, gramat, functionName, simplifyAll(simpleExpressions));
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
