package org.bakasoft.gramat.test.unit.compiling;

import java.util.function.Consumer;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.building.ConjunctionSequenceBuilder;
import org.bakasoft.gramat.building.DisjunctionSequenceBuilder;
import org.bakasoft.gramat.building.ExpressionBuilder;
import org.bakasoft.gramat.building.PropertyBuilder;
import org.bakasoft.gramat.building.PropertyType;
import org.bakasoft.gramat.building.ReferencedRuleBuilder;
import org.bakasoft.gramat.building.RepetitionBuilder;
import org.bakasoft.gramat.building.StringLiteralBuilder;
import org.bakasoft.gramat.io.Parser;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;


public class CompilerTest extends Framboyan {
	
	{
		describe("Compiler class", () -> {
			it("should ignore whitespaces and comments", () -> {
				test("", tape -> Parser.ignoreVoid(tape));
				test(" ", tape -> Parser.ignoreVoid(tape));
				test(" \t\r\n", tape -> Parser.ignoreVoid(tape));
				test(" /* comment */ ", tape -> Parser.ignoreVoid(tape));
			});
			
			it("should be able to read delimited strings", () -> {
				test("#123#", tape -> expect(Parser.readDelimitedString('#', tape)).toBe("123"));
				test("'ab \\' cd'", tape -> expect(Parser.readDelimitedString('\'', tape)).toBe("ab ' cd"));
				test("\"\\\"\\'\\`\\\\\\/\\b\\f\\n\\r\\t\"", tape -> expect(Parser.readDelimitedString('"', tape)).toBe("\"'`\\/\b\f\n\r\t"));
				test("*\\u003D\\u005f\\u007E\\u002b*", tape -> expect(Parser.readDelimitedString('*', tape)).toBe("=_~+"));
			});
			
			it("should understand valid identifier formats", () -> {
				test("id", tape -> expect(Parser.readIdentifier(tape)).toBe("id"));
				test("a_b_c", tape -> expect(Parser.readIdentifier(tape)).toBe("a_b_c"));
				test("123", tape -> expect(Parser.readIdentifier(tape)).toBe("123"));
				test("`/*weird id*/`", tape -> expect(Parser.readIdentifier(tape)).toBe("/*weird id*/"));
				test("`\\``", tape -> expect(Parser.readIdentifier(tape)).toBe("`"));
			});
			
			it("should compile character sequences", () -> {
				test("'abc'", tape -> expect(Parser.parseStringLiteral(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				test("\"abc\"", tape -> expect(Parser.parseStringLiteral(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				test("~abc~", tape -> expect(Parser.parseStringLiteral(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				// TODO test `text` and `fuzzy` properties
			});
			
			it("should compile different kinds of properties", () -> {
				testProperty("name", false, PropertyType.STRING,
						"name:<'abc'>",
						"`name`: <'abc'>",
						"name : < 'abc' >");
				testProperty("names", true, PropertyType.STRING,
						"names+:<'abc'>",
						"`names` +: <'abc'>",
						"names +: < 'abc' >");
				testProperty("index", false, PropertyType.NUMBER,
						"index:#<'abc'>",
						"index :# < 'abc' >",
						"`index`:# <'abc'>");
				testProperty("indexes", true, PropertyType.NUMBER,
						"indexes+:#<'abc'>",
						"`indexes` +:# <'abc'>",
						"indexes +:# < 'abc' >");
				testProperty("enabled", false, PropertyType.TRUE,
						"enabled:?<'abc'>",
						"enabled :? < 'abc' >",
						"`enabled`:? <'abc'>");
				testProperty("enableds", true, PropertyType.TRUE,
						"enableds+:?<'abc'>",
						"`enableds` +:? <'abc'>",
						"enableds +:? < 'abc' >");
				testProperty("disabled", false, PropertyType.FALSE,
						"disabled:!<'abc'>",
						"`disabled`:! <'abc'>",
						"disabled :! < 'abc' >");
				testProperty("disableds", true, PropertyType.FALSE,
						"disableds+:!<'abc'>",
						"`disableds`+:! <'abc'>",
						"disableds +:! < 'abc' >");
				testProperty("reference", false, PropertyType.NULL,
						"reference:@<'abc'>",
						"`reference`:@ <'abc'>",
						"reference :@ < 'abc' >");
				testProperty("references", true, PropertyType.NULL,
						"references+:@<'abc'>",
						"`references`+:@ <'abc'>",
						"references +:@ < 'abc' >");
				testProperty("node", false, PropertyType.OBJECT,
						"node:{'abc'}",
						"`node`: {'abc' }",
						"node : { 'abc' }");
				testProperty("nodes", true, PropertyType.OBJECT,
						"nodes+:{'abc'}",
						"`nodes`+: {'abc' }",
						"nodes +: { 'abc' }");
			});
			
			it("should compile different kinds of expressions", () -> {
				
				test("test", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(ReferencedRuleBuilder.class));
				test("(test)", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(ReferencedRuleBuilder.class));
				test("'abc'", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				test("\"abc\"", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				test("~abc~", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(StringLiteralBuilder.class));
				test("id: {test}", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("id: <test>", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("id:# <test>", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("id:? <test>", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("id:! <test>", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("id:@ <test>", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(PropertyBuilder.class));
				test("test?", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(RepetitionBuilder.class));
				test("test*", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(RepetitionBuilder.class));
				test("test+", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(RepetitionBuilder.class));
				test("test test", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(ConjunctionSequenceBuilder.class));
				test("test | test", tape -> expect(Parser.parseExpression(tape)).toBeInstanceOf(DisjunctionSequenceBuilder.class));
			});
			
			it("should compile conjunction sequences and disjunction sequences", () -> {
				test("a b c", tape -> {
					ExpressionBuilder expr = Parser.parseExpression(tape);
					
					expect(expr).toBeInstanceOf(ConjunctionSequenceBuilder.class);
					
					ConjunctionSequenceBuilder cseq = (ConjunctionSequenceBuilder)expr;
					expect(cseq.getChildren().size()).toBe(3);
					expect(cseq.getChildren().get(0)).toBeInstanceOf(ReferencedRuleBuilder.class);
					expect(cseq.getChildren().get(1)).toBeInstanceOf(ReferencedRuleBuilder.class);
					expect(cseq.getChildren().get(2)).toBeInstanceOf(ReferencedRuleBuilder.class);
					// TODO test name of rules
				});
				
				test("a | b c", tape -> {
					ExpressionBuilder expr = Parser.parseExpression(tape);
					
					expect(expr).toBeInstanceOf(DisjunctionSequenceBuilder.class);
					
					DisjunctionSequenceBuilder dseq = (DisjunctionSequenceBuilder)expr;
					expect(dseq.getChildren().size()).toBe(2);
					expect(dseq.getChildren().get(0)).toBeInstanceOf(ReferencedRuleBuilder.class);
					expect(dseq.getChildren().get(1)).toBeInstanceOf(ConjunctionSequenceBuilder.class);
					
					ConjunctionSequenceBuilder cseq = (ConjunctionSequenceBuilder) dseq.getChildren().get(1);
					expect(cseq.getChildren().size()).toBe(2);
					expect(cseq.getChildren().get(0)).toBeInstanceOf(ReferencedRuleBuilder.class);
					expect(cseq.getChildren().get(1)).toBeInstanceOf(ReferencedRuleBuilder.class);
				});
			});
			
			// TODO add directive tests
//			it("should understand @require directive", () -> {
//				test("@require test;", tape -> {
//					Engine engine = new Engine();
//					DefaultImportResolver resolver = new DefaultImportResolver(engine);
//					Grammar grammar = Parser.compileGrammar(engine, tape, resolver);
//					
//					expect(grammar.findRule("test")).toBe(null);
//				});
//			});
//			
//			it("should declare rules", () -> {
//				test("test = (<id: 'a'+> ('b'* {x +: 'c'})?);", tape -> {
//					Engine engine = new Engine();
//					DefaultImportResolver resolver = new DefaultImportResolver(engine);
//					Grammar grammar = Parser.compileGrammar(engine, tape, resolver);
//					
//					expect(grammar.findRule("test")).toBeInstanceOf(ConjunctionSequence.class);
//				});
//			});
		});	
	}
	
	private void testProperty(String name, boolean isArray, PropertyType type, String... codes) {
		for (String code : codes) {
			test(code, tape -> {
				ExpressionBuilder expr = Parser.parseExpression(tape);
				
				expect(expr).toBeInstanceOf(PropertyBuilder.class);
				
				PropertyBuilder property = (PropertyBuilder)expr;
				
				expect(property.getName()).toBe(name);
				expect(property.isArray()).toBe(isArray);
				expect(property.getType()).toBe(type);
			});	
		}
	}
	
	private static void test(String input, Consumer<Tape> action) {
		StringTape tape = new StringTape(input);
		
		action.accept(tape);
		
		if(tape.isOpen()) {
			throw new RuntimeException();
		}
	}
	
}
