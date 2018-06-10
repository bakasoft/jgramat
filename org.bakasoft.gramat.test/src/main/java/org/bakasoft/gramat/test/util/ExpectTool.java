package org.bakasoft.gramat.test.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class ExpectTool {

	public static void matchMap(Map<?, ?> actualMap, Map<?, ?> expectedMap, boolean matchOrder) {
		match_map("", actualMap, expectedMap, matchOrder);
	}
	
	private static void match_map(String parentKey, Map<?, ?> actualMap, Map<?, ?> expectedMap, boolean matchOrder) {
		ArrayList<Entry<? ,?>> actualEntries = new ArrayList<>(actualMap.entrySet());
		ArrayList<Entry<? ,?>> expectedEntries = new ArrayList<>(expectedMap.entrySet());
		
		match(actualEntries, expectedEntries,
				(actual, expected) -> {
					return Objects.equals(actual.getKey(), expected.getKey());
				},
				(actual, expected) -> {
					match_entry(parentKey, actual, expected, matchOrder);
				},
				(missing -> {
					throw new AssertionError("Missing key: " + parentKey + "/" + missing.getKey());
				}),
				(extra -> {
					throw new AssertionError("Unexpected key: " + parentKey + "/" + extra.getKey());
				})
		);
		
		if (actualEntries.size() != expectedEntries.size()) {
			throw new AssertionError(String.format("Expected the key `%s` has %s entries instead of %s.", parentKey, expectedEntries.size(), actualEntries.size()));
		}
		
		if (matchOrder) {
			for (int i = 0; i < actualEntries.size(); i++) {
				if (!Objects.equals(actualEntries.get(i).getKey(), expectedEntries.get(i).getKey())) {
					throw new AssertionError(String.format("Expected the key `%s` at index %s.", expectedEntries.get(i).getKey(), i));
				}
			}
		}
	}
	
	private static void match_list(String parentKey, List<?> actualList, List<?> expectedList, boolean matchOrder) {
		int minLength = Math.min(actualList.size(), expectedList.size());
		
		for (int i = 0; i < minLength; i++) {
			Object actualValue = actualList.get(i);
			Object expectedValue = expectedList.get(i);
			
			match_values(parentKey + "[" + i + "]", actualValue, expectedValue, matchOrder);
		}
		
		if (actualList.size() != expectedList.size()) {
			throw new AssertionError(String.format("Expected the key `%s` has %s item(s) instead of %s.", parentKey, expectedList.size(), actualList.size()));
		}
	}
	

	private static void match_entry(String parentKey, Entry<?, ?> actual, Entry<?, ?> expected, boolean matchOrder) {
		String expectedKey = parentKey + "/" + expected.getKey();
		String actualKey = parentKey + "/" + actual.getKey();
		
		if (!Objects.equals(actual.getKey(), expected.getKey())) {
			throw new AssertionError(String.format("Expected the key `%s` instead of `%s`.", expectedKey, actualKey));
		}
		
		match_values(actualKey, actual.getValue(), expected.getValue(), matchOrder);
	}

	private static void match_values(String parentKey, Object actualValue, Object expectedValue, boolean matchOrder) {
		if (expectedValue instanceof Map) {
			if (actualValue instanceof Map) {
				match_map(parentKey, (Map<?, ?>)actualValue, (Map<?, ?>)expectedValue, matchOrder);
			}
			else {
				throw new AssertionError(String.format("Expected the key `%s` to be a map.", parentKey));
			}
		} 
		else if (expectedValue instanceof List) {
			if (actualValue instanceof List) {
				match_list(parentKey, (List<?>)actualValue, (List<?>)expectedValue, matchOrder);
			}
			else {
				throw new AssertionError(String.format("Expected the key `%s` to be a map.", parentKey));
			}
		} else if (!Objects.equals(actualValue, expectedValue)) {
			throw new AssertionError(String.format("Expected the key `%s` to be `%s` instead of `%s`.", parentKey, expectedValue, actualValue));
		}
	}

	private static <T> void match(List<T> actual, List<T> expected, BiPredicate<T, T> matcher, BiConsumer<T, T> matched, Consumer<T> missing, Consumer<T> extra) {
		for (T a : actual) {
			boolean isMissing = true;
			
			for (T e : expected) {
				if (matcher.test(a, e)) {
					matched.accept(a, e);
					isMissing = false;
					break;
				}
			}
			
			if (isMissing) {
				missing.accept(a);
			}
		}
		
		for (T e : expected) {
			boolean isExtra = true;
			
			for (T a : actual) {
				if (matcher.test(a, e)) {
					isExtra = false;
					break;
				}
			}
			
			if (isExtra) {
				extra.accept(e);
			}
		}
	}
	
}
