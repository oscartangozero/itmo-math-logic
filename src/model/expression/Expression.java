package model.expression;

import java.util.*;

public abstract class Expression {
    public abstract boolean evaluate(VariableValues variableValues);

    /* Expression scheme manipulation */

    public abstract Expression expand(Map<String, Expression> variableSlots);

    abstract boolean tryMap(Expression expression, Map<String, Expression> variableSlots);

    public Map<String, Expression> map(Expression expression) {
        Map<String, Expression> variableSlots = new HashMap<>();
        return (tryMap(expression, variableSlots)) ? variableSlots : null;
    }

    public boolean mapsTo(Expression expr) {
        return map(expr) != null;
    }

    /* Expression tree traversal */

    abstract void collectVariables(Set<Variable> variables);

    public Set<Variable> getVariables() {
        Set<Variable> variables = new LinkedHashSet<>();
        collectVariables(variables);
        return variables;
    }

    /* String debug representation */

    abstract String contentString();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + contentString() + ')';
    }
}
