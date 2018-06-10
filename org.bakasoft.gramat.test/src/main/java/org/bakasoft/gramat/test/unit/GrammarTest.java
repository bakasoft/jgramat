package org.bakasoft.gramat.test.unit;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.errors.GramatException;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class GrammarTest extends Framboyan {{

	describe("Grammar class", () -> {

		it("should generate an Expression compiling valid code", () -> {
			Grammar grammar = new Grammar();
			Expression expression = grammar.compile("'0'");
			
			expect(expression).not.toBe(null);
		});
	
		it("should throw an error compiling an invalid code", () -> {
			Grammar grammar = new Grammar();
			expect(() -> {
				grammar.compile("=====");
			}).toThrow(GramatException.class);
		});
		
		it("should register and then find a Expression", () -> {
			Grammar grammar = new Grammar();
			Expression expression0 = new StrictString(grammar, "");
			
			grammar.registerRule("test", expression0);
			grammar.registerRule("pending");
			
			Expression expressionF = grammar.findRule("test");
			
			expect(expression0 == expressionF).toBe(true);
			expect(grammar.findRule("pending")).toBe(null);
		});
		
		it("should throw an error when registering a duplicated Expression", () -> {
			Grammar grammar = new Grammar();
			Expression expression = new StrictString(grammar, "");
			
			grammar.registerRule("test", expression);
			
			expect(() -> {
				grammar.registerRule("test", expression);
			}).toThrow(GramatException.class);
		});
		
		it("should register the expression despite it was previouly registered with null", () -> {
			Grammar grammar = new Grammar();
			grammar.registerRule("test");
			
			expect(() -> {
				// two times with null is not possible
				grammar.registerRule("test");
			}).toThrow(GramatException.class);
			
			grammar.registerRule("test", new StrictString(grammar, ""));
		});
		
		it("should throw an error finding a not registered Expression", () -> {
			Grammar grammar = new Grammar();
			expect(() -> {
				grammar.findRule("test");
			}).toThrow(GramatException.class);
		});
	});	
	
}}
