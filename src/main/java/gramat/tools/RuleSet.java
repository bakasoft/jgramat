package gramat.tools;

import gramat.expressions.Rule;

import java.util.*;

public class RuleSet extends AbstractSet<Rule> {

    private final ArrayList<Rule> rules;

    public RuleSet() {
        rules = new ArrayList<>();
    }

    public boolean containsName(String name) {
        return get(name) != null;
    }

    public Rule get(String name) {
        for (var rule : rules) {
            if (Objects.equals(rule.name, name)) {
                return rule;
            }
        }

        return null;
    }

    @Override
    public boolean add(Rule rule) {
        if (containsName(rule.name)) {
            return false;
        }
        return rules.add(rule);
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @Override
    public int size() {
        return rules.size();
    }

}
