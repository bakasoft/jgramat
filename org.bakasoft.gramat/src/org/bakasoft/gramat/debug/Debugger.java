package org.bakasoft.gramat.debug;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.compiling.Decompiler;
import org.bakasoft.gramat.util.Tape;

public interface Debugger {

	void processStarted(Context context);

	void expressionEntered(Expression expression);

	void tapePositionMoved(Tape tape, int previous, int current);
	
	void tapeCharacterPeeked(Tape tape, char c);
	
	void expressionExited(Expression expression, boolean result);	
	
	void objectAttributeSet(String name, Object value, boolean array);
	
	void objectElementSet(String name, boolean array);
	
	void objectTransactionBegun();
	
	void objectTransactionCompleted(boolean result);
	
	void processCompleted(Context context);
	

	static String inspect(Expression expr, int sampleLength) {
		String code = Decompiler.decompileExpression(expr);
		
//		if (code.length() > sampleLength) {
//			return code.substring(sampleLength).trim() + "...";
//		} 
		
		return code;
	}
	
	static String inspect(Tape tape, int sampleLength) {
		StringBuilder textLine = new StringBuilder();
		int position = tape.getPosition();
		int startPosition = Math.max(0, position - sampleLength / 2);
		int endPosition = Math.min(tape.getLength() - 1, startPosition + sampleLength);
		
		textLine.append('[');
		
		for (int i = startPosition; i <= endPosition; i++) {
			char c = tape.charAt(i);
			
			if (Character.isWhitespace(c) || Character.isISOControl(c)) {
				c = ' ';
			}
			
			if (i == position) {
				textLine.append(" >");
				textLine.append(c);
				textLine.append("< ");
			} else {
				textLine.append(c);
			}	
		}
		
		textLine.append(']');
		
		if (position >= tape.getLength()) {
			textLine.append("><");
		}
		
		return textLine.toString();
	}
	
}
