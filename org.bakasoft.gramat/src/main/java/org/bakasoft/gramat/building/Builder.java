package org.bakasoft.gramat.building;

import java.util.ArrayList;
import java.util.List;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.propertyExpressions.NullProperty;
import org.bakasoft.gramat.propertyExpressions.NumberProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;
import org.bakasoft.gramat.regularExpressions.AnyChar;
import org.bakasoft.gramat.regularExpressions.CharPredicateExpression;
import org.bakasoft.gramat.regularExpressions.Complement;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;
import org.bakasoft.gramat.regularExpressions.FuzzyString;
import org.bakasoft.gramat.regularExpressions.KleenePlus;
import org.bakasoft.gramat.regularExpressions.KleeneStar;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.StrictString;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;

public class Builder {

	public static Expression build(ExpressionBuilder item, GrammarBuilder grammarBuilder) {
		ExpressionBuilder collapsed = collapse(item);
		
		if (collapsed instanceof ComplementBuilder) {
			return buildComplement((ComplementBuilder)collapsed, grammarBuilder);	
		}
		else if (collapsed instanceof AnyBuilder) {
			return buildAnyChar(grammarBuilder);
		}
		else if (collapsed instanceof ConjunctionSequenceBuilder) {
			return buildConjunction((ConjunctionSequenceBuilder) collapsed, grammarBuilder);
		}
		else if (collapsed instanceof DisjunctionSequenceBuilder) {
			return buildDisjunction((DisjunctionSequenceBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof PredicateBuilder) {
			return buildPredicate((PredicateBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof PropertyBuilder) {
			return buildProperty((PropertyBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof ReferencedRuleBuilder) {
			return buildReference((ReferencedRuleBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof RepetitionBuilder) {
			return buildRepetition((RepetitionBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof StringLiteralBuilder) {
			return buildLiteral((StringLiteralBuilder)collapsed, grammarBuilder);
		}
		else if (collapsed instanceof WildCharBuilder) {
			return buildWildChar((WildCharBuilder)collapsed, grammarBuilder);
		}
		
		throw new RuntimeException("not supported builder:" + collapsed);
	}

	// COLLAPSE FUNCTIONS
	
	private static ExpressionBuilder collapse(ExpressionBuilder item) {
		if (item instanceof ComplementBuilder) {
			return collapseComplement((ComplementBuilder)item);
		}
		else if (item instanceof ConjunctionSequenceBuilder) {
			return collapseConjunction((ConjunctionSequenceBuilder)item);
		}
		else if (item instanceof DisjunctionSequenceBuilder) {
			return collapseDisjunction((DisjunctionSequenceBuilder)item);
		}
		
		return item;
	}
	
	private static ExpressionBuilder collapseConjunction(ConjunctionSequenceBuilder item) {
		if (item.getChildren().size() == 1) {
			return collapse(item.getChildren().get(0));
		}
		
		return item;
	}
	
	private static ExpressionBuilder collapseDisjunction(DisjunctionSequenceBuilder item) {
		if (item.getChildren().size() == 1) {
			return collapse(item.getChildren().get(0));
		}
		
		return item;
	}

	private static ExpressionBuilder collapseComplement(ComplementBuilder item) {
		ExpressionBuilder e = collapse(item.expression);
		
		if (e instanceof ComplementBuilder) {
			ComplementBuilder subitem = (ComplementBuilder)e;
			
			return collapse(subitem.expression);	
		}
		
		return item;
	}
	
	// BUILD FUNCTIONS

	private static Expression buildAnyChar(GrammarBuilder grammarBuilder) {
		return new CharPredicateExpression("any", c -> true); // TODO add class to implement this
	}

	private static Expression buildComplement(ComplementBuilder complement, GrammarBuilder grammarBuilder) {
		Expression e = build(complement.getExpression(), grammarBuilder);
		
		return new Complement(e);
	}

	private static Expression buildConjunction(ConjunctionSequenceBuilder sequence, GrammarBuilder grammarBuilder) {
		Expression[] expressions = sequence.getChildren().stream()
				.map(item -> build(item, grammarBuilder))
				.toArray(Expression[]::new);
		
		if (expressions.length == 0) {
			throw new RuntimeException(); // TODO empty expression error
		} else if (expressions.length == 1) {
			return expressions[0];
		}
		
		// TODO: optimize joining literals
		
		return new ConjunctionSequence(expressions);
	}

	private static Expression buildDisjunction(DisjunctionSequenceBuilder seq, GrammarBuilder grammarBuilder) {
		ArrayList<Expression> expressions = new ArrayList<>();
		
		buildDisjunctionOptimized(seq, grammarBuilder, expressions);
		
		if (expressions.isEmpty()) {
			throw new RuntimeException(); // TODO empty expression error
		} else if (expressions.size() == 1) {
			return expressions.get(0);
		}
		
		return new DisjunctionSequence(expressions.toArray(new Expression[expressions.size()]));
	}
	
	private static void buildDisjunctionOptimized(DisjunctionSequenceBuilder seq, GrammarBuilder grammarBuilder, List<Expression> expressions) {
		for (ExpressionBuilder child : seq.getChildren()) {
			if (child instanceof DisjunctionSequenceBuilder) {
				DisjunctionSequenceBuilder subseq = (DisjunctionSequenceBuilder)child;
				
				buildDisjunctionOptimized(subseq, grammarBuilder, expressions);
			} else {
				Expression expr = build(child, grammarBuilder);
				
				if (!expressions.contains(expr)) {
					expressions.add(expr);		
				}
			}
		}
	}

	private static Expression buildPredicate(PredicateBuilder predicate, GrammarBuilder grammarBuilder) {
		return new CharPredicateExpression(predicate.getName(), predicate.getPredicate());
	}

	private static Expression buildProperty(PropertyBuilder property, GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = property.getExpression();
		Expression e = build(content, grammarBuilder);
		
		switch(property.getType()) {
		case FALSE: return new FalseProperty(property.getName(), property.isArray(), e);
		case NULL: return new NullProperty(property.getName(), property.isArray(), e);
		case NUMBER: return new NumberProperty(property.getName(), property.isArray(), e);
		case OBJECT: return new ObjectProperty(property.getName(), property.isArray(), e);
		case STRING: return new StringProperty(property.getName(), property.isArray(), e);
		case TRUE: return new TrueProperty(property.getName(), property.isArray(), e);
		default: 
			throw new RuntimeException();
		}
	}
	
	private static Expression buildReference(ReferencedRuleBuilder reference, GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = reference.getReferencedExpression(grammarBuilder);
		
		return build(expression, grammarBuilder);
	}
	
	private static Expression buildRepetition(RepetitionBuilder repetition, GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = collapse(repetition.getExpression());
		Integer minimum = repetition.getMinimum();
		Integer maximum = repetition.getMaximum();
		Expression e;
		boolean kleene;
		
		if (content instanceof WildCharBuilder) {
			ConjunctionSequenceBuilder seq = new ConjunctionSequenceBuilder();
			
			ExpressionBuilder nextExpr = content.getNextExpression();

			while(nextExpr != null && !nextExpr.hasWildChar(grammarBuilder)) {
				seq.addExpression(nextExpr.clone(false));
				
				nextExpr = nextExpr.getNextExpression();
			}

			e = build(seq, grammarBuilder);
			kleene = true;
		} else {
			// TODO add a flag to build to avoid collapse twice
			e = build(content, grammarBuilder);
			kleene = false;
		}
		
		if (minimum == null || minimum == 0) {
			if (maximum == null) {
				return kleene ? new KleeneStar(e) : new ZeroOrMore(e);
			}
			else if (maximum == 1) {
				return new ZeroOrOne(e);	
			}
		} else if (minimum == 1) {
			if (maximum == null) {
				return kleene ? new KleenePlus(e) : new OneOrMore(e);
			}
			else if (maximum == 1) {
				return e; // 🤦🏽
			}
		}
		
		throw new RuntimeException(); // TODO implement max-min and exact repetitions
	}
	
	private static Expression buildLiteral(StringLiteralBuilder literal, GrammarBuilder grammarBuilder) {
		if (literal.isFuzzy()) {
			return new FuzzyString(literal.getText());
		}

		// TODO optimize immutable expressions by returning the same instance instead of a new one
		return new StrictString(literal.getText());
	}
	
	private static Expression buildWildChar(WildCharBuilder wildChar, GrammarBuilder grammarBuilder) {
		return new AnyChar();
	}
	
}
