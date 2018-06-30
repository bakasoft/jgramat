package org.bakasoft.gramat.building.importers;

import org.bakasoft.gramat.building.GrammarBuilder;

public class DefaultImportResolver implements ImportResolver {

	@Override
	public GrammarBuilder getGrammar(String path) {
		throw new RuntimeException("not supported: " + path);
	}

}
