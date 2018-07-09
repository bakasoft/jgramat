package org.bakasoft.gramat.test.integration;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.building.GrammarBuilder;
import org.bakasoft.gramat.io.Parser;
import org.bakasoft.gramat.util.StringTape;

public class BuildingTest extends Framboyan {{

	describe("Building", () -> {
		it("should build wild chars", () -> {
			String code = "a = .;b = a* 'test';";
			GrammarBuilder grammarBuilder = Parser.parseGrammar(new StringTape(code));
			
			Grammar grammar = grammarBuilder.build();
			
			Expression expr = grammar.findRule("b");
			
//			expect(expr.toString()).toBe(".* \"test\"");
			// TODO: fix this test
		});
	});
	
}}