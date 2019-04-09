package org.bakasoft.gramat.parsing.directives;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.PathResolver;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GTest;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class GDirective {

    public final String name;
    public final GLiteral[] arguments;

    public GDirective(String name, GLiteral[] arguments) {
        this.name = Objects.requireNonNull(name);
        this.arguments = Objects.requireNonNull(arguments);
    }

    public void evalDirective(Gramat gramat, PathResolver pathResolver) {
        if ("import".equals(name)) {
            if (arguments.length == 0) {
                throw new RuntimeException("expected import paths");
            }

            for (Object rawPath : arguments) {
                if (!(rawPath instanceof GToken)) {
                    throw new RuntimeException("expected a token");
                }

                GToken path = (GToken)rawPath;
                Path resolvedPath = pathResolver.resolve(path.content);

                if (resolvedPath == null) {
                    throw new RuntimeException("cannot resolve: " + path);
                }

                gramat.load(resolvedPath);
            }
        }
        else if ("pass".equals(name)) {
            if (arguments.length > 3) {
                throw new RuntimeException("Expected <=3 options for test");
            }

            gramat.addTest(new GTest(
                    arguments[0].resolveString(),
                    arguments[1].resolveString(),
                    arguments.length == 3 ? arguments[2] : null,
                    false
            ));
        }
        else if ("fail".equals(name)) {
            if (arguments.length != 2) {
                throw new RuntimeException("Expected 2 options for test");
            }

            gramat.addTest(new GTest(
                    arguments[0].resolveString(),
                    arguments[1].resolveString(),
                    null,
                    true
            ));
        }
        else {
            throw new RuntimeException("directive not supported: @" + name);
        }
    }

}
