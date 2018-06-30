package org.bakasoft.gramat.test.unit.propertyExpressions;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class ObjectPropertyTest extends Framboyan {

	{
		describe("ObjectProperty class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("node", false, "abc").eval("abc").toString()).toBe("{node={}}");
				expect(build("nodes", true, "abc").eval("abc").toString()).toBe("{nodes=[{}]}");
			});

			it("should satisfy the negative test cases", () -> {
				try {
					build("node", false, "abc").eval("xyz");
					build("nodess", true, "abc").eval("xyz");
					
					fail();
				} catch(Exception e) {
					expect(e).not.toBe(null);
				}
			});
		});
	}

	private ObjectProperty build(String name, boolean isArray, String expression) {
		return new ObjectProperty(name, isArray, new StrictString(expression));
	}
}
