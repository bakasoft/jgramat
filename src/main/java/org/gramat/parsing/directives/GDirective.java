package org.gramat.parsing.directives;

import org.gramat.Gramat;
import org.gramat.PathResolver;
import org.gramat.parsing.GTest;
import org.gramat.parsing.literals.GArray;

import java.nio.file.Path;
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
