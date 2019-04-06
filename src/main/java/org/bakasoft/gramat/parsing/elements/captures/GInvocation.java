package org.bakasoft.gramat.parsing.elements.captures;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;

import java.util.Map;

public class GInvocation extends GCapture {

    public final String functionName;

    public final GElement[] arguments;

    public GInvocation(String functionName, GElement[] arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public GElement simplify() {
        return new GInvocation(functionName, simplifyAll(arguments));
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        throw new UnsupportedOperationException("function invocation is not implemented yet");
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        throw new UnsupportedOperationException();
    }
}
