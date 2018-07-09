package org.bakasoft.gramat.test;

import org.bakasoft.framboyan.Framboyan;

public class Main {

	public static void main(String[] args) {
		boolean result = Framboyan.run();
		
		System.exit(result ? 0 : 1);
	}

}
