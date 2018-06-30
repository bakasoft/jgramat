package org.bakasoft.gramat.test.unit.regularExpressions;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class OneOrMoreTest extends Framboyan {

	{
		describe("OneOrMore class", () -> {
			
			it("should satisfy the positive test cases", () -> {
				expect(build("a").test("a")).toBe(true);
				expect(build("a").test("aa")).toBe(true);
				expect(build("a").test("aaa")).toBe(true);
				expect(build("12").test("12")).toBe(true);
				expect(build("12").test("1212")).toBe(true);
				expect(build("12").test("121212")).toBe(true);
			});

			it("should satisfy the negative test cases", () -> {
				expect(build("").test("")).toBe(false);
				expect(build("aa").test("")).toBe(false);
				expect(build("aa").test("a")).toBe(false);
				expect(build("xy").test("xyz")).toBe(false);
			});
			
		});		
	}
		
	private OneOrMore build(String str) {
		return new OneOrMore(new StrictString(str));
	}
	
}
