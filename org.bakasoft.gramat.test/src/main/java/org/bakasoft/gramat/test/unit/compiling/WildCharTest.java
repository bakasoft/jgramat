package org.bakasoft.gramat.test.unit.compiling;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.framboyan.templates.Template2;
import org.bakasoft.gramat.Expression;

public class WildCharTest extends Framboyan {{

	describe("Whild char evaluation", () -> {
		var evaluate = templater((String gramat, String java) -> {
			Expression expr = Expression.compile(gramat);
			Pattern pattern = Pattern.compile(java);
			
			return template((String input, Boolean result) -> {
				console.log("Java:   (", java, ").test(", input, ")");
				
				expect(pattern.matcher(input).matches()).toBe(result);
				
				console.log("Gramat: (", gramat, ").test(", input, ")");
				
				expect(expr.test(input)).toBe(result);
			});
		});
	
		it("should pass when there is only one kleene operator in the expression", () -> {
			evaluate
				.using(". 'abc'", ".abc")
				.test("xabc", true)
				.test("aabc", true)
			;
			evaluate
				.using(".+ 'abc'", ".+abc")
				.using(". .* 'abc'", "..*abc")
				.test("abc", false)
				.test("aabc", true)
				.test("ababc", true)
				.test("abcaabc", true)
				.test("abcababc", true)
				.test("abcabcabc", true)
			;
			evaluate
				.using(".* 'abc'", ".*abc")
				.test("abc", true)
				.test("aabc", true)
				.test("ababc", true)
				.test("abcaabc", true)
				.test("abcababc", true)
				.test("abcabcabc", true)
			;
			evaluate
				.using(".+ 'a'? 'b'* 'c'", ".+a?b*c")
				.test("abc", true)
			;
			evaluate
				.using(".* 'a'? 'b'* 'c'", ".+a?b*c")
				.test("xxxabc", true)
			;
			evaluate
				.using(".* 'abc'", ".*abc")
				.test("abcabc", true)
			;
			evaluate
				.using("'<a>' .* '</a>'", "<a>.*</a>")
				.test("<a>xyz</a>", true)
				.test("<a>x<a>y</a>z</a>", true)
			;
		});
	});
	
}}