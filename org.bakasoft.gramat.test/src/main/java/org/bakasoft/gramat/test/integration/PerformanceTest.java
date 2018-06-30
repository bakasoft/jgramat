package org.bakasoft.gramat.test.integration;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.test.util.PerformanceTool;

public class PerformanceTest extends Framboyan {{

	describe("Gramat Performance vs Java Pattern", () -> {
		it("should be superior using disjunctions", () -> {
			String input = "e";
			Pattern javaOr = Pattern.compile("a|b|c|d|e|f|g|h|i");
			DisjunctionSequence gramatOr = new DisjunctionSequence( 
					Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i")
					.map(item -> new StrictString(item))
					.toArray(Expression[]::new)); // TODO compile
			
			PerformanceTool.deathMatch(1000000, 
					() -> { javaOr.matcher(input).matches(); }, 
					() -> { gramatOr.test(input); }, 
					(javaTime, gramatTime) -> {
						if (javaTime < gramatTime) {
							throw new AssertionError("Bad performance! Gramat: " + gramatTime + ", Java: " + javaTime);
						}	
					}
				);
		});
	});
	
}}
