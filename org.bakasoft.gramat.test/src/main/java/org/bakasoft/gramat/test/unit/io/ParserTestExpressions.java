package org.bakasoft.gramat.test.unit.io;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.building.ComplementBuilder;
import org.bakasoft.gramat.building.ConjunctionSequenceBuilder;
import org.bakasoft.gramat.building.DisjunctionSequenceBuilder;
import org.bakasoft.gramat.building.ExpressionBuilder;
import org.bakasoft.gramat.building.PropertyBuilder;
import org.bakasoft.gramat.building.RepetitionBuilder;
import org.bakasoft.gramat.building.StringLiteralBuilder;
import org.bakasoft.gramat.building.WildCharBuilder;
import org.bakasoft.gramat.io.Parser;
import org.bakasoft.gramat.util.StringTape;

public class ParserTestExpressions extends Framboyan {

	{
		describe("Parsing string literals", () -> {
			it("should parse string literals", () -> {
				expect(parseExpression("''")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("\"\"")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("'abc'")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("\"abc\"")).toBeInstanceOf(StringLiteralBuilder.class);
			});

			it("should understand escaping sequences", () -> {
				ExpressionBuilder e = parseExpression("'\\r\\n\\\"\\'\\`\\\\\\/\\b\\f\\n\\r\\t\\u2Af6'");
				
				expect(e).toBeInstanceOf(StringLiteralBuilder.class);
				expect(((StringLiteralBuilder)e).getText()).toBe("\r\n\"'`\\/\b\f\n\r\t⫶");
			});
		});
		
		describe("Parsing quantifiers", () -> {
			it("should parse one or more quantifier", () -> {
				expect(parseExpression("'1'+")).toBeInstanceOf(RepetitionBuilder.class);
			});
			
			it("should parse zero or more quantifier", () -> {
				expect(parseExpression("'1'*")).toBeInstanceOf(RepetitionBuilder.class);
			});
			
			it("should parse zero or one quantifier", () -> {
				expect(parseExpression("'1'?")).toBeInstanceOf(RepetitionBuilder.class);
			});
			
			it("should parse exact quantifier", () -> {
				expect(parseExpression("'1'{1}")).toBeInstanceOf(RepetitionBuilder.class);
			});
			
			it("should parse min-max quantifier", () -> {
				expect(parseExpression("'1'{1, 2}")).toBeInstanceOf(RepetitionBuilder.class);
			});
			
			it("should parse min- quantifier", () -> {
				expect(parseExpression("'1'{1,}")).toBeInstanceOf(RepetitionBuilder.class);
			});
		});
		
		describe("Parsing sequences", () -> {
			it("should parse conjuntion sequences", () -> {
				expect(parseExpression("'1' '2'")).toBeInstanceOf(ConjunctionSequenceBuilder.class);
				expect(parseExpression("'a' 'b' 'c'")).toBeInstanceOf(ConjunctionSequenceBuilder.class);
			});
			
			it("should parse disjuntion sequences", () -> {
				expect(parseExpression("'1' | '2'")).toBeInstanceOf(DisjunctionSequenceBuilder.class);
				expect(parseExpression("'a' | 'b' | 'c'")).toBeInstanceOf(DisjunctionSequenceBuilder.class);
			});
		});
		
		describe("Parsing properties", () -> {
			it("should parse true properties", () -> {
				expect(parseExpression("enabled :? <'on'>")).toBeInstanceOf(PropertyBuilder.class);
				expect(parseExpression("enableds +:? <'on'>")).toBeInstanceOf(PropertyBuilder.class);
			});

			it("should parse false properties", () -> {
				expect(parseExpression("enabled :! <'off'>")).toBeInstanceOf(PropertyBuilder.class);
				expect(parseExpression("enableds +:! <'off'>")).toBeInstanceOf(PropertyBuilder.class);
			});
			
			it("should parse string properties", () -> {
				expect(parseExpression("name : <'mat'>")).toBeInstanceOf(PropertyBuilder.class);
				expect(parseExpression("names +: <'mat'>")).toBeInstanceOf(PropertyBuilder.class);
			});
			
			it("should parse object properties", () -> {
				expect(parseExpression("node : {'...'}")).toBeInstanceOf(PropertyBuilder.class);
				expect(parseExpression("nodes +: {'...'}")).toBeInstanceOf(PropertyBuilder.class);
			});
			// TODO test properties name, type and array
		});
		
		describe("Parsing other features", () -> {
			it("should parse wild chars", () -> {
				expect(parseExpression(".")).toBeInstanceOf(WildCharBuilder.class);
			});
			
			it("should parse groups", () -> {
				expect(parseExpression("('a')")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("(('a'))")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("((('a')))")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("(((('a'))))")).toBeInstanceOf(StringLiteralBuilder.class);
			});
			
			it("should parse complement", () -> {
				expect(parseExpression("(!'a')")).toBeInstanceOf(ComplementBuilder.class);
			});
			
			it("should simplify double complement", () -> {
				expect(parseExpression("(!(!'a'))")).toBeInstanceOf(StringLiteralBuilder.class);
				expect(parseExpression("(!(!(!(!'a'))))")).toBeInstanceOf(StringLiteralBuilder.class);
			});
		});
	}
	
	private static ExpressionBuilder parseExpression(String code) {
		return Parser.parseExpression(new StringTape(code));
	}
	
}
