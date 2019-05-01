package org.gramat;

import java.nio.file.Path;

@FunctionalInterface
public interface PathResolver {

    Path resolve(String path);

}
