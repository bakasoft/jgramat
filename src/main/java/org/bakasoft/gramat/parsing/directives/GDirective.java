package org.bakasoft.gramat.parsing.directives;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.PathResolver;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.GTest;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.literals.GArray;
import org.bakasoft.gramat.parsing.literals.GLiteral;
import org.bakasoft.gramat.parsing.literals.GToken;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GDirective {

    public final String name;
    public final GArray arguments;

    public GDirective(String name, GArray arguments) {
        this.name = Objects.requireNonNull(name);
        this.arguments = Objects.requireNonNull(arguments);
    }

    public void evalDirective(Gramat gramat, PathResolver pathResolver) {
        if ("import".equals(name)) {
            List<String> paths = arguments.forceStringList();

            for (String path : paths) {
                Path resolvedPath = pathResolver.resolve(path);

                if (resolvedPath == null) {
                    throw new RuntimeException("cannot resolve: " + path);
                }

                gramat.load(resolvedPath);
            }
        }
        else if ("pass".equals(name)) {
            if (arguments.size() > 3) {
                throw new RuntimeException("Expected <=3 options for test");
            }

            gramat.addTest(new GTest(
                    arguments.get(0).forceString(),
                    arguments.get(1).forceString(),
                    arguments.size() == 3 ? arguments.get(2) : null,
                    false
            ));
        }
        else if ("fail".equals(name)) {
            if (arguments.size() != 2) {
                throw new RuntimeException("Expected 2 options for test");
            }

            gramat.addTest(new GTest(
                arguments.get(0).forceString(),
                arguments.get(1).forceString(),
                null,
                true
            ));
        }
        else if ("define".equals(name)) {
            if (arguments.size() != 2) {
                throw new RuntimeException("Expected 2 options for test");
            }

            gramat.setVariable(arguments.get(0).forceString(), arguments.get(1));
        }
        else {
            throw new RuntimeException("directive not supported: #" + name);
        }
    }

}
