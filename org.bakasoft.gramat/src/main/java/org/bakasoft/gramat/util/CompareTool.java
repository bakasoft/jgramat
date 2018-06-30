package org.bakasoft.gramat.util;

import java.util.List;

public class CompareTool {

	private static int hash_mix(Object[] items, boolean deep) {
		if (items == null || items.length == 0) {
			return 0;
		}

		int hash = items.length;
		
		for (Object item : items) {
			if (item != null) {
				if (deep) {
					if (item instanceof Object[]) {
					    hash = hash ^ hash_mix((Object[])item, true);
					    continue;
					} else if (item instanceof List) {
						hash = hash ^ hash_mix(((List<?>)item).toArray(), true);
					    continue;
					}
				}

				hash = hash ^ item.hashCode();
				continue;
			}
		}
		
		return hash;
	}
	
	public static int hashMix(Object... items) {
		return hash_mix(items, false);
	}
	
	public static int deepHashMix(Object... items) {
		return hash_mix(items, true);
	}
	
	public static <T> T checkType(Object obj, Class<T> type) {
		if (type != null && obj != null && type.isInstance(obj)) {
			return type.cast(obj);
		}
		
		return null;
	}
	
}
