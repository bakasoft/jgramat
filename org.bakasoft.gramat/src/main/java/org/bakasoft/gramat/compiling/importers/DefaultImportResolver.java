package org.bakasoft.gramat.compiling.importers;

import org.bakasoft.gramat.Engine;
import org.bakasoft.gramat.Grammar;

public class DefaultImportResolver implements ImportResolver {

	private final Engine engine;

	public DefaultImportResolver(Engine engine) {
		this.engine = engine;
	}

	@Override
	public Grammar resolveGrammar(String id) {
		return engine.findGrammar(id);
	}

}
