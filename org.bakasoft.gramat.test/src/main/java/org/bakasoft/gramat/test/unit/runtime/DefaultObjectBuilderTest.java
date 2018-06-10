package org.bakasoft.gramat.test.unit.runtime;

import java.util.Map;

import org.bakasoft.framboyan.Framboyan;
import org.bakasoft.gramat.util.DefaultObjectBuilder;
import org.bakasoft.gramat.util.ObjectBuilder;

public class DefaultObjectBuilderTest extends Framboyan {{
	
	describe("ObjectBuilder class", () -> {
		
		it("should create a plain object", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
			
			builder.setAttribute("string", "Test", false);
			builder.setAttribute("number", 3.5, false);
			builder.setAttribute("boolean", true, false);
			builder.setAttribute("null", null, false);
			
			Map<String, Object> map = builder.build();
			
			expect(map.get("string")).toBe("Test");
			expect(map.get("number")).toBe(3.5);
			expect(map.get("boolean")).toBe(true);
			expect(map.get("null")).toBe(null);
		});
		
		it("should support array properties", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
			
			builder.setAttribute("list", "Test", true);
			builder.setAttribute("list", 3.5, true);
			builder.setAttribute("list", true, true);
			builder.setAttribute("list", null, true);
			
			Map<String, Object> map = builder.build();
			
			expect(map.get("list").toString()).toBe("[Test, 3.5, true, null]");
		});

		it("should create an object with nested objects", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
			
			builder.setAttribute("name", "G-Man", false);
			builder.setAttribute("artist", "Sonny Rollins", false);
			builder.setAttribute("year", 1987, false);
			builder.setAttribute("genre", "Jazz", false);
			
			builder.openElement();
			builder.setAttribute("year", "1986", false);
			builder.setAttribute("month", "August", false);
			builder.setAttribute("day", "16", false);
			builder.closeElement("recorded", false);
			
			builder.openElement();
			builder.setAttribute("track", 1, false);
			builder.setAttribute("name", "G-Man - Live", false);
			builder.setAttribute("length", "15:20", false);
			builder.closeElement("songs", true);
			
			builder.openElement();
			builder.setAttribute("track", 2, false);
			builder.setAttribute("name", "Kim - Live", false);
			builder.setAttribute("length", "5:57", false);
			builder.closeElement("songs", true);
			
			builder.openElement();
			builder.setAttribute("track", 3, false);
			builder.setAttribute("name", "Don't Stop The Carnival - Live", false);
			builder.setAttribute("length", "11:18", false);
			builder.closeElement("songs", true);
			
			builder.openElement();
			builder.setAttribute("track", 4, false);
			builder.setAttribute("name", "Tenor Madness - Live", false);
			builder.setAttribute("length", "12:02", false);
			builder.closeElement("songs", true);
			
			Map<String, Object> map = builder.build();

			expect(map.toString()).toBe(
					"{name=G-Man, artist=Sonny Rollins, year=1987, genre=Jazz, "
					+ "recorded={year=1986, month=August, day=16}, "
					+ "songs=["
					+ "{track=1, name=G-Man - Live, length=15:20}, "
					+ "{track=2, name=Kim - Live, length=5:57}, "
					+ "{track=3, name=Don't Stop The Carnival - Live, length=11:18}, "
					+ "{track=4, name=Tenor Madness - Live, length=12:02}"
					+ "]}");
		});
		
		it("should commit/rollback changes in an plain property", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
			
			builder.transaction(() -> {
				builder.setAttribute("name", "Benny Golson", false);
				return true;
			});
			
			builder.transaction(() -> {
				builder.setAttribute("born", "January 25, 1929", false);
				return false;
			});
			
			builder.transaction(() -> {
				builder.setAttribute("born", "1929-01-25", false);
				return true;
			});
			
			Map<String, Object> map = builder.build();
			
			expect(map.toString()).toBe("{name=Benny Golson, born=1929-01-25}");
		});
		
		it("should commit/rollback changes in an array property", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
				
			builder.setAttribute("genres", "Jazz", true);
			
			builder.transaction(() -> {
				builder.setAttribute("genres", "Pop", true);
				return false;
			});
			
			builder.transaction(() -> {
				builder.setAttribute("genres", "Bebop", true);
				builder.setAttribute("genres", "Hard Bop", true);
				return true;
			});
			
			Map<String, Object> map = builder.build();
			
			expect(map.toString()).toBe("{genres=[Jazz, Bebop, Hard Bop]}");
		});
		
		it("should commit/rollback changes in an element property", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
			
			builder.openElement();
			builder.setAttribute("name", "Whisper Not", false);
			builder.setAttribute("year", 1956, false);
			builder.closeElement("songs", true);
			
			builder.transaction(() -> {
				builder.openElement();
				builder.setAttribute("name", "Take Five", false);
				builder.setAttribute("year", 1959, false);
				builder.closeElement("songs", true);
				return false;
			});
			
			builder.transaction(() -> {
				builder.openElement();
				builder.setAttribute("name", "Killer Joe", false);
				builder.setAttribute("year", 1960, false);
				builder.closeElement("songs", true);
				return true;
			});
			
			Map<String, Object> map = builder.build();
			
			expect(map.toString()).toBe("{songs=[{name=Whisper Not, year=1956}, {name=Killer Joe, year=1960}]}");
		});
		
		it("should commit/rollback changes in nested transactions", () -> {
			ObjectBuilder builder = new DefaultObjectBuilder();
				
			builder.setAttribute("name", "Dexter Gordon", false);
			
			builder.transaction(() -> {
				builder.setAttribute("alias", "Long Tall Dexter", false);
				return false;
			});	
			
			builder.transaction(() -> {			
				builder.setAttribute("birthday", "1923-02-27", false);
				
				builder.transaction(() -> {
					builder.setAttribute("labels", "Blue Note", true);
					builder.setAttribute("labels", "Savoy", true);
					
					builder.transaction(() -> {
						builder.setAttribute("labels", "Mercury", true);
						return false;
					});	
					
					builder.setAttribute("labels", "Columbia", true);
					return true;
				});
				
				builder.transaction(() -> {
					builder.openElement();
					builder.setAttribute("city", "Los Angeles", false);
					builder.setAttribute("state", "California", false);
					builder.setAttribute("country", "US", false);
					builder.closeElement("birthplace", false);
					
					builder.transaction(() -> {
						builder.setAttribute("website", "http://www.dextergordon.com/", false);
						return true;
					});	
					
					return false;
				});	
				
				return true;
			});
			
			Map<String, Object> map = builder.build();

			expect(map.toString()).toBe("{name=Dexter Gordon, birthday=1923-02-27, labels=[Blue Note, Savoy, Columbia]}");
		});	
	});		
}}
