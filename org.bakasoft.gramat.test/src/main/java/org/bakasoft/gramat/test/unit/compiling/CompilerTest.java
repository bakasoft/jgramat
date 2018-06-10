package org.bakasoft.gramat.test.unit.compiling;

import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;

import java.util.function.Consumer;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Engine;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.compiling.Compiler;
import org.bakasoft.gramat.compiling.importers.DefaultImportResolver;
import org.bakasoft.gramat.propertyExpressions.AbstractProperty;
import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.propertyExpressions.NullProperty;
import org.bakasoft.gramat.propertyExpressions.NumberProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.FuzzyString;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.ReferencedExpression;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;


public class CompilerTest extends Framboyan {
	
	{
		describe("Compiler class", () -> {
			it("should ignore whitespaces and comments", () -> {
				test("", tape -> Compiler.ignoreWhitespace(tape));
				test(" ", tape -> Compiler.ignoreWhitespace(tape));
				test(" \t\r\n", tape -> Compiler.ignoreWhitespace(tape));
				test(" /* comment */ ", tape -> Compiler.ignoreWhitespace(tape));
			});
			
			it("should be able to read delimited strings", () -> {
				test("#123#", tape -> expect(Compiler.readDelimitedString('#', tape)).toBe("123"));
				test("'ab \\' cd'", tape -> expect(Compiler.readDelimitedString('\'', tape)).toBe("ab ' cd"));
				test("\"\\\"\\'\\`\\\\\\/\\b\\f\\n\\r\\t\"", tape -> expect(Compiler.readDelimitedString('"', tape)).toBe("\"'`\\/\b\f\n\r\t"));
				test("*\\u003D\\u005f\\u007E\\u002b*", tape -> expect(Compiler.readDelimitedString('*', tape)).toBe("=_~+"));
			});
			
			it("should understand valid identifier formats", () -> {
				test("id", tape -> expect(Compiler.readIdentifier(tape)).toBe("id"));
				test("a_b_c", tape -> expect(Compiler.readIdentifier(tape)).toBe("a_b_c"));
				test("123", tape -> expect(Compiler.readIdentifier(tape)).toBe("123"));
				test("`/*weird id*/`", tape -> expect(Compiler.readIdentifier(tape)).toBe("/*weird id*/"));
				test("`\\``", tape -> expect(Compiler.readIdentifier(tape)).toBe("`"));
			});
			
			it("should compile character sequences", () -> {
				test("'abc'", tape -> expect(Compiler.compileCharacterSequence(new Grammar(), tape)).toBeInstanceOf(StrictString.class));
				test("\"abc\"", tape -> expect(Compiler.compileCharacterSequence(new Grammar(), tape)).toBeInstanceOf(StrictString.class));
				test("~abc~", tape -> expect(Compiler.compileCharacterSequence(new Grammar(), tape)).toBeInstanceOf(FuzzyString.class));
			});
			
			it("should compile different kinds of properties", () -> {
				testProperty("name", false, StringProperty.class,
						"<name:'abc'>",
						"<`name`: 'abc'>",
						"< name : 'abc' >");
				testProperty("names", true, StringProperty.class,
						"<names+:'abc'>",
						"<`names` +: 'abc'>",
						"< names +: 'abc' >");
				testProperty("index", false, NumberProperty.class,
						"<index:#'abc'>",
						"< index :# 'abc' >",
						"<`index`:# 'abc'>");
				testProperty("indexes", true, NumberProperty.class,
						"<indexes+:#'abc'>",
						"<`indexes` +:# 'abc'>",
						"< indexes +:# 'abc' >");
				testProperty("enabled", false, TrueProperty.class,
						"<enabled:?'abc'>",
						"< enabled :? 'abc' >",
						"<`enabled`:? 'abc'>");
				testProperty("enableds", true, TrueProperty.class,
						"<enableds+:?'abc'>",
						"<`enableds` +:? 'abc'>",
						"< enableds +:? 'abc' >");
				testProperty("disabled", false, FalseProperty.class,
						"<disabled:!'abc'>",
						"<`disabled`:! 'abc'>",
						"< disabled :! 'abc' >");
				testProperty("disableds", true, FalseProperty.class,
						"<disableds+:!'abc'>",
						"<`disableds`+:! 'abc'>",
						"< disableds +:! 'abc' >");
				testProperty("reference", false, NullProperty.class,
						"<reference:@'abc'>",
						"<`reference`:@ 'abc'>",
						"< reference :@ 'abc' >");
				testProperty("references", true, NullProperty.class,
						"<references+:@'abc'>",
						"<`references`+:@ 'abc'>",
						"< references +:@ 'abc' >");
				testProperty("node", false, ObjectProperty.class,
						"{node:'abc'}",
						"{`node`: 'abc' }",
						"{ node : 'abc' }");
				testProperty("nodes", true, ObjectProperty.class,
						"{nodes+:'abc'}",
						"{`nodes`+: 'abc' }",
						"{ nodes +: 'abc' }");
			});
			
			it("should compile referenced expressions", () -> {
				Grammar grammar = new Grammar();
				Expression test = new StrictString(grammar, "abc");
				
				grammar.registerRule("a", test);
				grammar.registerRule("b");
				
				test("a", tape -> expect(Compiler.compileExpression(grammar, tape) == test).toBe(true));
				test("b", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(ReferencedExpression.class));
			});
			
			it("should compile different kinds of expressions", () -> {
				Grammar grammar = new Grammar();
				Expression test = new StrictString(grammar, "abc");
				grammar.registerRule("test", test);
				
				test("test", tape -> expect(Compiler.compileExpression(grammar, tape) == test).toBe(true));
				test("(test)", tape -> expect(Compiler.compileExpression(grammar, tape) == test).toBe(true));
				test("'abc'", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(StrictString.class));
				test("\"abc\"", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(StrictString.class));
				test("~abc~", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(FuzzyString.class));
				test("{id: test}", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(ObjectProperty.class));
				test("<id: test>", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(StringProperty.class));
				test("<id:# test>", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(NumberProperty.class));
				test("<id:? test>", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(TrueProperty.class));
				test("<id:! test>", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(FalseProperty.class));
				test("<id:@ test>", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(NullProperty.class));
				test("test?", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(ZeroOrOne.class));
				test("test*", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(ZeroOrMore.class));
				test("test+", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(OneOrMore.class));
				test("test test", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(ConjunctionSequence.class));
				test("test | test", tape -> expect(Compiler.compileExpression(grammar, tape)).toBeInstanceOf(DisjunctionSequence.class));
			});
			
			it("should compile conjunction sequences and disjunction sequences", () -> {
				Grammar grammar = new Grammar();
				Expression a = new StrictString(grammar, "aaa");
				Expression b = new StrictString(grammar, "bbb");
				Expression c = new StrictString(grammar, "ccc");
				grammar.registerRule("a", a);
				grammar.registerRule("b", b);
				grammar.registerRule("c", c);
				
				test("a b c", tape -> {
					Expression expr = Compiler.compileExpression(grammar, tape);
					
					expect(expr).toBeInstanceOf(ConjunctionSequence.class);
					
					ConjunctionSequence cseq = (ConjunctionSequence)expr;
					expect(cseq.getExpressions()).not.toBe(null);
					expect(cseq.getExpressions().length).toBe(3);
					expect(cseq.getExpressions()[0] == a).toBe(true);
					expect(cseq.getExpressions()[1] == b).toBe(true);
					expect(cseq.getExpressions()[2] == c).toBe(true);
				});
				
				test("a | b c", tape -> {
					Expression expr = Compiler.compileExpression(grammar, tape);
					
					expect(expr).toBeInstanceOf(DisjunctionSequence.class);
					
					DisjunctionSequence dseq = (DisjunctionSequence)expr;
					expect(dseq.getExpressions()).not.toBe(null);
					expect(dseq.getExpressions().length).toBe(2);
					expect(dseq.getExpressions()[0] == a).toBe(true);
					expect(dseq.getExpressions()[1]).toBeInstanceOf(ConjunctionSequence.class);
					
					ConjunctionSequence cseq = (ConjunctionSequence) dseq.getExpressions()[1];
					expect(cseq.getExpressions()).not.toBe(null);
					expect(cseq.getExpressions().length).toBe(2);
					expect(cseq.getExpressions()[0] == b).toBe(true);
					expect(cseq.getExpressions()[1] == c).toBe(true);
				});
			});
			
			it("should understand @require directive", () -> {
				test("@require test;", tape -> {
					Engine engine = new Engine();
					DefaultImportResolver resolver = new DefaultImportResolver(engine);
					Grammar grammar = Compiler.compileGrammar(engine, tape, resolver);
					
					expect(grammar.findRule("test")).toBe(null);
				});
			});
			
			it("should declare rules", () -> {
				test("test = (<id: 'a'+> ('b'* {x +: 'c'})?);", tape -> {
					Engine engine = new Engine();
					DefaultImportResolver resolver = new DefaultImportResolver(engine);
					Grammar grammar = Compiler.compileGrammar(engine, tape, resolver);
					
					expect(grammar.findRule("test")).toBeInstanceOf(ConjunctionSequence.class);
				});
			});
		});	
	}
	
	private void testProperty(String name, boolean isArray, Class<?> type, String... codes) {
		for (String code : codes) {
			test(code, tape -> {
				Expression expr = Compiler.compileProperty(new Grammar(), tape);
				
				expect(expr).toBeInstanceOf(AbstractProperty.class);
				
				AbstractProperty property = (AbstractProperty)expr;
				
				expect(property.getPropertyName()).toBe(name);
				expect(property.isArray()).toBe(isArray);
				expect(property).toBeInstanceOf(type);
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
