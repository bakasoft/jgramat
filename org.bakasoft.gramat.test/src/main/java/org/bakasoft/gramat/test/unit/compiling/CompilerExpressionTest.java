package org.bakasoft.gramat.test.unit.compiling;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.compiling.Compiler;
import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;

public class CompilerExpressionTest extends Framboyan {

	{
		describe("Compiler class", () -> {
			it("should compile strict strings", () -> {
				expect(compileExpression("''")).toBeInstanceOf(StrictString.class);
				expect(compileExpression("\"\"")).toBeInstanceOf(StrictString.class);
				expect(compileExpression("'abc'")).toBeInstanceOf(StrictString.class);
				expect(compileExpression("\"abc\"")).toBeInstanceOf(StrictString.class);
			});

			it("should understand escaping sequences", () -> {
				expect(compileExpression("'\r\n'").test("\r\n")).toBe(true);
				// TODO
			});
			
			it("should compile one or more quantifier", () -> {
				expect(compileExpression("'1'+")).toBeInstanceOf(OneOrMore.class);
			});
			
			it("should compile zero or more quantifier", () -> {
				expect(compileExpression("'1'*")).toBeInstanceOf(ZeroOrMore.class);
			});
			
			it("should compile zero or one quantifier", () -> {
				expect(compileExpression("'1'?")).toBeInstanceOf(ZeroOrOne.class);
			});
			
			it("should compile conjuntion sequences", () -> {
				expect(compileExpression("'1' '2'")).toBeInstanceOf(ConjunctionSequence.class);
				expect(compileExpression("'a' 'b' 'c'")).toBeInstanceOf(ConjunctionSequence.class);
			});
			
			it("should compile disjuntion sequences", () -> {
				expect(compileExpression("'1' | '2'")).toBeInstanceOf(DisjunctionSequence.class);
				expect(compileExpression("'a' | 'b' | 'c'")).toBeInstanceOf(DisjunctionSequence.class);
			});
			
			it("should compile true properties", () -> {
				expect(compileExpression("<enabled:? 'on'>")).toBeInstanceOf(TrueProperty.class);
				expect(compileExpression("<enableds +:? 'on'>")).toBeInstanceOf(TrueProperty.class);
			});

			it("should compile false properties", () -> {
				expect(compileExpression("<enabled:! 'off'>")).toBeInstanceOf(FalseProperty.class);
				expect(compileExpression("<enableds +:! 'off'>")).toBeInstanceOf(FalseProperty.class);
			});
			
			it("should compile string properties", () -> {
				expect(compileExpression("<name: 'mat'>")).toBeInstanceOf(StringProperty.class);
				expect(compileExpression("<names +: 'mat'>")).toBeInstanceOf(StringProperty.class);
			});
			
			it("should compile object properties", () -> {
				expect(compileExpression("{node: ''}")).toBeInstanceOf(ObjectProperty.class);
				expect(compileExpression("{nodes +: ''}")).toBeInstanceOf(ObjectProperty.class);
			});
		});
	}
	
	private static Expression compileExpression(String code) {
		return Compiler.compileExpression(new Grammar(), new StringTape(code));
	}
	
}
