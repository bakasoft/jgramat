package org.bakasoft.gramat.test.unit.regularExpressions;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class StrictStringTest extends Framboyan {

	{
		describe("StrictString class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("abc").test("abc")).toBe(true);
				expect(build("A B C").test("A B C")).toBe(true);
				expect(build(" 1 2 ").test(" 1 2 ")).toBe(true);
				expect(build("\r\n\t\b").test("\r\n\t\b")).toBe(true);
			});

			it("should satisfy the negative test cases", () -> {
				expect(build("").test("")).toBe(false);
				expect(build("abc").test("Abc")).toBe(false);
				expect(build("abc").test("abc ")).toBe(false);
				expect(build(" ").test("  ")).toBe(false);
			});
		});	
	}
	
	private static StrictString build(String code) {
		StrictString expr = new StrictString(code);
		return expr;
	}
	
}
