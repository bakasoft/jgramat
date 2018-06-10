package org.bakasoft.gramat.compiling.importers;

import org.bakasoft.gramat.Grammar;

public interface ImportResolver {

	Grammar resolveGrammar(String id);
	
}
