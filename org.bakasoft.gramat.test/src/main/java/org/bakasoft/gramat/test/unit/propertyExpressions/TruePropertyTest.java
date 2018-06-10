package org.bakasoft.gramat.test.unit.propertyExpressions;

import org.bakasoft.framboyan.Framboyan;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class TruePropertyTest extends Framboyan {

	{
		describe("TrueProperty class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("enabled", false, "1").eval("1").toString()).toBe("{enabled=true}");
				expect(build("flags", true, "1").eval("1").toString()).toBe("{flags=[true]}");
			});

			it("should satisfy the negative test cases", () -> {
				try {
					build("enabled", false, "1").eval("0");
					build("flags", true, "1").eval("0");
					
					fail();
				} catch(Exception e) {
					expect(e).not.toBe(null);
				}
			});
		});
	}
	
	private TrueProperty build(String name, boolean isArray, String expression) {
		Grammar grammar = new Grammar();
		return new TrueProperty(grammar, name, isArray, new StrictString(grammar, expression));
	}
}
