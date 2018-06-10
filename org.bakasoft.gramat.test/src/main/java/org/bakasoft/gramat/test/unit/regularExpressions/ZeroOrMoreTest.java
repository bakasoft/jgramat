package org.bakasoft.gramat.test.unit.regularExpressions;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;

public class ZeroOrMoreTest extends Framboyan {

	{
		describe("ZeroOrMore class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("").test("")).toBe(true);
				expect(build("a").test("")).toBe(true);
				expect(build("a").test("a")).toBe(true);
				expect(build("a").test("aa")).toBe(true);
				expect(build("12").test("")).toBe(true);
				expect(build("12").test("12")).toBe(true);
				expect(build("12").test("1212")).toBe(true);
				expect(build("12").test("121212")).toBe(true);
			});

			it("should satisfy the negative test cases"
			, () -> {
				 // TODO fix following tests
				expect(build("aa").test("a")).toBe(false);
				expect(build("xy").test("xyz")).toBe(false);
			});
		});
	}
	
	private ZeroOrMore build(String str) {
		Grammar grammar = new Grammar();
		return new ZeroOrMore(grammar, new StrictString(grammar, str));
	}
	
}
