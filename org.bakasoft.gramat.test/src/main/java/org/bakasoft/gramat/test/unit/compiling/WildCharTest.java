package org.bakasoft.gramat.test.unit.compiling;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.Expression;

public class WildCharTest extends Framboyan {

	public static class Template1<T1> {
		
		private final Consumer<T1> template;

		public Template1(Consumer<T1> template) {
			this.template = template;
		}
		
		public Template1<T1> test(T1 p1) {
			template.accept(p1);
			return this;
		}
		
	}

	public static class Template2<T1, T2> {
		
		private final BiConsumer<T1, T2> template;

		public Template2(BiConsumer<T1, T2> template) {
			this.template = template;
		}
		
		public Template2<T1, T2> test(T1 p1, T2 p2) {
			template.accept(p1, p2);
			return this;
		}
		
	}

	@FunctionalInterface
	public static interface Template3Action<T1, T2, T3> {
		
		void run(T1 p1, T2 p2, T3 p3);
		
	}
	
	public static class Template3<T1, T2, T3> {
		
		private final Template3Action<T1, T2, T3> action;

		public Template3(Template3Action<T1, T2, T3> action) {
			this.action = action;
		}
		
		public Template3<T1, T2, T3> test(T1 p1, T2 p2, T3 p3) {
			action.run(p1, p2, p3);
			return this;
		}
		
	}
	
	public static <T1> Template1<T1> template(Consumer<T1> template) {
		return new Template1<T1>(template);
	}
	
	public static <T1, T2> Template2<T1, T2> template(BiConsumer<T1, T2> template) {
		return new Template2<T1, T2>(template);
	}
	
	public static <T1, T2, T3> Template3<T1, T2, T3> template(Template3Action<T1, T2, T3> action) {
		return new Template3<T1, T2, T3>(action);
	}
	
{

	describe("Whild char construction with basic expressions", () -> {
		var comparer = template((String actual, String expected) -> {
			Expression actualExpr = Expression.compile(actual);
			Expression expectedExpr = Expression.compile(expected);
			
//			System.out.println(actualExpr.toString());
//			System.out.println(expectedExpr.toString());
			expect(actualExpr.toString()).toBe(expectedExpr.toString());
		});
		
		it("should take the next character to compute the expression", () -> {
			comparer
			.test("'x' . 'abc'"        , "'x' (!'a') 'abc'")
			.test("'x' . 'abc'+"       , "'x' (!'a') 'abc'+")
			.test("'x' . ('abc' 'def')", "'x' (!'a') ('abc' 'def')")
			.test("'x' . prop: <'abc'>", "'x' (!'a') prop: <'abc'>")
			;
		});
		
		it("should take the first character of next options to compute the expression", () -> {
			comparer
			.test(". ('ab' | 'cd')"                     , "(!'a'|'c') ('ab' | 'cd')")
			.test(". ('ab' | 'cd' | 'ef')"              , "(!'a'|'c'|'e') ('ab' | 'cd' | 'ef')")
			.test(". ('ab' | ('cd' | ('ef' | ('gh'))))" , "(!'a'|'c'|'e'|'g') ('ab' | ('cd' | ('ef' | ('gh'))))")
			;
		});
		
		it("should take all the possible next characters to compute the expression", () -> {
			comparer
			.test(". 'abc'?", "(!'a') 'abc'?")
			.test(". 'abc'? 'def'", "(!'a'|'d') 'abc'? 'def'")
			.test(". 'ab'? 'cd'* 'ef'", "(!'a'|'c'|'e') 'ab'? 'cd'* 'ef'")
			.test(". 'ab'* 'cd'? 'ef'", "(!'a'|'c'|'e') 'ab'* 'cd'? 'ef'")
			.test(". (('ab' | 'cd')? (!'ef' | 'gh')* 'ij')*", "(!('a'|'c'|(!'e'|'g')|'i')) (('ab' | 'cd')? (!'ef' | 'gh')* 'ij')*")
			;
		});
		
		it("should simplify the computed expression", () -> {
			comparer
			.test(". ('ab' | 'ac')", "(!'a') ('ab' | 'ac')")
			.test(". 'abc'? 'aeiou'", "(!'a') 'abc'? 'aeiou'")
			;
		});
	});
	
	describe("Whild char evaluation", () -> {
		var comparer = template((String code, String input, Boolean result) -> {
			Expression expr = Expression.compile(code);
			
			expect(expr.test(input)).toBe(result);
		});
		
		it("should pass with basic expressions", () -> {
			comparer
			.test(". 'abc'", "xabc", true)
			.test(".+ 'abc'", "xxabc", true)
			.test(".* 'abc'", "abc", true)
			.test(".+ 'a'? 'b'* 'c'", "xxxabc", true)
			.test(".+ 'a'? 'b'* 'c'", "abc", false)
			;
		});
	});
	
}}