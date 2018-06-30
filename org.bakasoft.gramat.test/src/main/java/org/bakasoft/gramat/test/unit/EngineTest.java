//package org.bakasoft.gramat.test.unit;
//
//import org.bakasoft.framboyan.Framboyan;
//import org.bakasoft.gramat.Engine;
//import org.bakasoft.gramat.Grammar;
//import org.bakasoft.gramat.errors.GramatException;
//
//public class EngineTest extends Framboyan {{
//
//	describe("Engine class", () -> {
//		it("should generate a Grammar compiling valid code", () -> {
//			Engine engine = new Engine();
//			
//			Grammar grammar = engine.compile("");
//			
//			expect(grammar).not.toBe(null);
//		});
//
//		it("should throw an error compiling an invalid code", () -> {
//			Engine engine = new Engine();
//			
//			expect(() -> {
//				engine.compile("=====");
//			}).toThrow(RuntimeException.class); // TODO: change to specific exception
//		});
//		
//		it("should register and then find a Grammar", () -> {
//			Engine engine = new Engine();
//			Grammar grammar0 = new Grammar(engine);
//			
//			engine.registerGrammar("test", grammar0);
//			
//			Grammar grammarF = engine.findGrammar("test");
//			
//			expect(grammar0 == grammarF).toBe(true);
//		});
//		
//		it("should throw an error when registering a duplicated", () -> {
//			Engine engine = new Engine();
//			Grammar grammar = new Grammar(engine);
//			
//			engine.registerGrammar("test", grammar);
//			
//			expect(() -> {
//				engine.registerGrammar("test", grammar);
//			}).toThrow(GramatException.class); // TODO: change to specific exception
//		});
//		
//		it("should throw an error finding a not registered Grammar", () -> {
//			Engine engine = new Engine();
//			
//			expect(() -> {
//				engine.findGrammar("test");
//			}).toThrow(GramatException.class); // TODO: change to specific exception
//		});
//	});
//	
//}}
