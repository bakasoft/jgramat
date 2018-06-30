package org.bakasoft.gramat.test;

import org.bakasoft.framboyan.Framboyan;

import org.bakasoft.gramat.test.integration.*;
import org.bakasoft.gramat.test.unit.*;
import org.bakasoft.gramat.test.unit.compiling.*;
import org.bakasoft.gramat.test.unit.io.ParserTestExpressions;
import org.bakasoft.gramat.test.unit.propertyExpressions.*;
import org.bakasoft.gramat.test.unit.regularExpressions.*;
import org.bakasoft.gramat.test.unit.runtime.*;

public class Main {

	public static void main(String[] args) {
		// unit tests
//		Framboyan.add(GrammarTest.class);
//		Framboyan.add(ExpressionTest.class);
//		Framboyan.add(StrictStringTest.class);
//		Framboyan.add(ConjunctionSequenceTest.class);
//		Framboyan.add(DisjunctionSequenceTest.class);
//		Framboyan.add(OneOrMoreTest.class);
//		Framboyan.add(ZeroOrMoreTest.class);
//		Framboyan.add(ZeroOrOneTest.class);
//		Framboyan.add(DefaultObjectBuilderTest.class);
//		Framboyan.add(TruePropertyTest.class);
//		Framboyan.add(FalsePropertyTest.class);
//		Framboyan.add(StringPropertyTest.class);
//		Framboyan.add(ObjectPropertyTest.class);
//		Framboyan.add(CompilerTest.class);
////		
////		// integration tests
//		Framboyan.add(PerformanceTest.class);
//		Framboyan.add(CaseTest.class);
		Framboyan.add(WildCharTest.class);
		
//		Framboyan.add(ParserTestExpressions.class);
//		Framboyan.add(BuildingTest.class);

		boolean result = Framboyan.run();
		
		System.exit(result ? 0 : 1);
	}

}
