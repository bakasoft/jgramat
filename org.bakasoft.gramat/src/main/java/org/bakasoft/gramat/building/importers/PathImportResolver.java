package org.bakasoft.gramat.building.importers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bakasoft.gramat.building.GrammarBuilder;
import org.bakasoft.gramat.io.Parser;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;

public class PathImportResolver implements ImportResolver {

	private final Path root;
	
	public PathImportResolver(Path root) {
		this.root = root;
	}
	
	@Override
	public GrammarBuilder getGrammar(String path) {
		Path file = root.resolve(path);
		String code;
		
		try {
			byte[] data = Files.readAllBytes(file);
		
			code = new String(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Tape tape = new StringTape(code);
		
		return Parser.parseGrammar(tape);
	}

	public Path getRoot() {
		return root;
	}

	
	
}
