package org.bakasoft.gramat.test.unit.propertyExpressions;

import org.bakasoft.framboyan.Framboyan;

import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class FalsePropertyTest extends Framboyan {

	{
		describe("FalseProperty class", () -> {
			it("should satisfy the positive test cases", () -> {
				expect(build("enabled", false, "1").eval("1").toString()).toBe("{enabled=false}");
				expect(build("flags", true, "1").eval("1").toString()).toBe("{flags=[false]}");
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
	
	private FalseProperty build(String name, boolean isArray, String expression) {
		// TODO compile instead of manually construct (in all tests)
		return new FalseProperty(name, isArray, new StrictString(expression));
	}
}
