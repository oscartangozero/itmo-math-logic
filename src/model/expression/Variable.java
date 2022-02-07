package model.expression;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        if (name == null) throw new NullPointerException();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean evaluate(VariableValues variableValues) {
        return variableValues.getValue(getName());
    }

    @Override
    public Expression expand(Map<String, Expression> variableSlots) {
        Expression expanded = variableSlots.get(getName());
        if (expanded == null) throw new IllegalArgumentException("Unable to expand " + this);
        return expanded;
    }

    @Override
    public boolean tryMap(Expression expression, Map<String, Expression> variableSlots) {
        Expression alreadyMapped = variableSlots.get(getName());
        if (alreadyMapped == null) {
            variableSlots.put(getName(), expression);
            return true;
        }
        return expression.equals(alreadyMapped);
    }

    @Override
    void collectVariables(Set<Variable> variables) {
        variables.add(this);
    }

    @Override
    String contentString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return getName().equals(variable.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
