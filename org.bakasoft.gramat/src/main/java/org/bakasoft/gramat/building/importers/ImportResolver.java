package org.bakasoft.gramat.building.importers;

import org.bakasoft.gramat.building.GrammarBuilder;

public interface ImportResolver {

	GrammarBuilder getGrammar(String path);
	
}
