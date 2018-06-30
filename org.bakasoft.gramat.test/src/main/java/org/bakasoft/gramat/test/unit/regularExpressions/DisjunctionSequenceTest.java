package org.bakasoft.gramat.test.unit.regularExpressions;

import java.util.Arrays;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class DisjunctionSequenceTest extends Framboyan {

	{
		describe("DisjunctionSequence class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("123").test("123")).toBe(true);
				expect(build("a", "b", "c").test("a")).toBe(true);
				expect(build("a", "b", "c").test("b")).toBe(true);
				expect(build("a", "b", "c").test("c")).toBe(true);
				expect(build("abc", "def", "ghi").test("abc")).toBe(true);
				expect(build("abc", "def", "ghi").test("def")).toBe(true);
				expect(build("abc", "def", "ghi").test("ghi")).toBe(true);
			});

			it("should satisfy the negative test cases", () -> {
				expect(build("").test("")).toBe(false);
				expect(build("abc").test("abcdef")).toBe(false);
				expect(build("a", "b").test("c")).toBe(false);
				expect(build("1", "2", "3").test("123")).toBe(false);
			});
		
		});
	}
	
	private DisjunctionSequence build(String... strictStrings) {
		return new DisjunctionSequence( 
				Arrays.stream(strictStrings)
				.map(item -> new StrictString(item))
				.toArray(Expression[]::new));
	}
	
}
