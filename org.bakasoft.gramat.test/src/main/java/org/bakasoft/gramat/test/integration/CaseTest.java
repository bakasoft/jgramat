package org.bakasoft.gramat.test.integration;

import java.util.Map;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.framboyan.util.Resources;
import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.debug.DefaultDebugger;
import org.bakasoft.gramat.test.util.JsonTool;
import org.bakasoft.gramat.test.util.ExpectTool;

public class CaseTest extends Framboyan {{
	describe("Integration cases testing", () -> {
		var runner = template((String caseName, String mainRule) -> {
			String code = Resources.loadString("/" + caseName + "/main.gmt", CaseTest.class);
			
			Grammar grammar = new Grammar(code);
			Expression rule = grammar.findRule(mainRule);
			
			System.out.println(rule);
			
			for (int n = 1; n <= 99; n++) {
				String baseName = "test" + (n <= 9 ? "0" + n : String.valueOf(n));
				String input = Resources.loadString("/" + caseName + "/" + baseName + "-input.txt", CaseTest.class);
				String output = Resources.loadString("/" + caseName + "/" + baseName + "-output.json", CaseTest.class);
				
				if (input == null && output == null) {
					break;
				}
				else if (input == null || output == null) {
					throw new AssertionError();
				}
				
				Map<String, Object> expectedMap = JsonTool.parse(output);
				
				Context context = rule.debug(input, new DefaultDebugger(System.out)); // TODO take the print stream from framboyan?
				
				Map<String, Object> actualMap = context.builder.build();
				
				console.log("Actual: " + actualMap);
				console.log("Expected: " + expectedMap);
				
				ExpectTool.matchMap(actualMap, expectedMap, true);
				
				expect(context.tape.isClosed()).toBe(true);
			}	
		});
		
		it("should pass the case01", () -> {
			runner.test("case01", "url");		
		});
//		it("should pass the case02", (out) -> {
//			runner.test("case02", "request", out);		
//		});
	});
}}
