package org.bakasoft.gramat.test.util;

import java.util.function.BiConsumer;

public interface PerformanceTool {
	
	public static long measureNanos(Runnable action) {
		long t0 = System.nanoTime();
		
		action.run();
		
		long tF = System.nanoTime();
		
		return tF - t0;
	}

	public static void deathMatch(long repetitions, Runnable action1, Runnable action2, BiConsumer<Double, Double> results) {
		long total1 = 0;
		long total2 = 0;
		
		for (long i = 0; i < repetitions; i++) {
			total1 += measureNanos(action1);
			total2 += measureNanos(action2);
		}
		
		double avg1 = total1 / (double)repetitions;
		double avg2 = total2 / (double)repetitions;
		
		results.accept(avg1, avg2);
	}
	
}
