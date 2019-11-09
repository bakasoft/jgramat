package org.gramat;

import java.nio.file.Path;

@FunctionalInterface
public interface PathResolver {

    String read(String path);

}
