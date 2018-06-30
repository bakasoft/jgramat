package org.bakasoft.gramat.test.unit.propertyExpressions;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class StringPropertyTest extends Framboyan {

	{
		describe("StringProperty class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("id", false, "abc").eval("abc").toString()).toBe("{id=abc}");
				expect(build("names", true, "abc").eval("abc").toString()).toBe("{names=[abc]}");
			});

			it("should satisfy the negative test cases", () -> {
				try {
					build("id", false, "abc").eval("xyz");
					build("names", true, "abc").eval("xyz");
					
					fail();
				} catch(Exception e) {
					expect(e).not.toBe(null);
				}
			});
		});
	}
	
	private StringProperty build(String name, boolean isArray, String expression) {
		return new StringProperty(name, isArray, new StrictString(expression));
	}
}
