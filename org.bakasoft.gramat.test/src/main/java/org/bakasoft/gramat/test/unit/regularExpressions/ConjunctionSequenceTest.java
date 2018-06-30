package org.bakasoft.gramat.test.unit.regularExpressions;

import java.util.Arrays;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class ConjunctionSequenceTest extends Framboyan {

	{
		describe("ConjunctionSequence class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("123").test("123")).toBe(true);
				expect(build("a", "b", "c").test("abc")).toBe(true);
				expect(build("abc", "def", "ghi").test("abcdefghi")).toBe(true);
			});

			it("should satisfy the negative test cases", () -> {
				expect(build("").test("")).toBe(false);
				expect(build("abc").test("abcdef")).toBe(false);
				expect(build("a", "b", "c").test("abcdef")).toBe(false);
				expect(build("1", "2", "3").test(" 123 ")).toBe(false);
			});
		});
	}
	
	private ConjunctionSequence build(String... strictStrings) {
		return new ConjunctionSequence( 
				Arrays.stream(strictStrings)
				.map(item -> new StrictString(item))
				.toArray(Expression[]::new));
	}
	
}
