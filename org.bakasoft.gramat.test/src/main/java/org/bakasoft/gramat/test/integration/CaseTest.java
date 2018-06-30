package org.bakasoft.gramat.test.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.debug.DefaultDebugger;
import org.bakasoft.gramat.test.util.JsonTool;
import org.bakasoft.gramat.test.util.ExpectTool;

public class CaseTest extends Framboyan {
	
	{
		describe("Integration cases testing", () -> {
			it("should pass the case01", (out) -> {
				test("case01", "url", out);		
			});
			it("should pass the case02", (out) -> {
				test("case02", "request", out);		
			});
		});
	}
	
	private static void test(String caseName, String mainRule, PrintStream out) {
		String code = fetchString("/" + caseName + "/main.gmt");
		
		Grammar grammar = new Grammar(code);
		Expression rule = grammar.findRule(mainRule);
		
		for (int n = 1; n <= 99; n++) {
			String baseName = "test" + (n <= 9 ? "0" + n : String.valueOf(n));
			String input = fetchString("/" + caseName + "/" + baseName + "-input.txt");
			String output = fetchString("/" + caseName + "/" + baseName + "-output.json");
			
			if (input == null && output == null) {
				break;
			}
			else if (input == null || output == null) {
				throw new AssertionError();
			}
			
			Map<String, Object> expectedMap = JsonTool.parse(output);
			
			Context context = rule.debug(input, new DefaultDebugger(out));
			
			Map<String, Object> actualMap = context.builder.build();
			
			out.println("Actual: " + actualMap);
			out.println("Expected: " + expectedMap);
			
			ExpectTool.matchMap(actualMap, expectedMap, true);
			
			expect(context.tape.isClosed()).toBe(true);
		}
	}
	
	private static String fetchString(String resource) {
		URL url = CaseTest.class.getResource(resource);
		
		if (url != null) {
			try(InputStream is = url.openStream()) {
				byte[] data = is.readAllBytes();
				
				return new String(data);
			}
			catch (IOException e) {
				throw new RuntimeException(e); // TODO add message
			}
		}
		
		return null;
	}
}
