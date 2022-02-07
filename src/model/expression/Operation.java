package model.expression;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Operation extends Expression {
    private final Expression[] operands;

    protected Operation(Expression... operands) {
        for (Expression operand : operands) if (operand == null) throw new NullPointerException();
        this.operands = operands;
    }

    protected Expression getOperand(int i) {
        return operands[i];
    }

    protected int getArity() {
        return operands.length;
    }

    @Override
    public Expression expand(Map<String, Expression> variableSlots) {
        Expression[] expandedOperands = Arrays.stream(operands).map(o -> o.expand(variableSlots)).toArray(Expression[]::new);
        try {
            return this.getClass().getConstructor(Expression[].class).newInstance((Object) expandedOperands);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(
                    this.getClass().getName() + " must provide a constructor that takes `Expression...` as operands",e);
        }
    }

    @Override
    boolean tryMap(Expression expression, Map<String, Expression> variableSlots) {
        if (getClass() != expression.getClass()) return false;
        Operation target = (Operation) expression;
        for (int i = 0; i < operands.length; i++) {
            if (!operands[i].tryMap(target.operands[i], variableSlots)) return false;
        }
        return true;
    }

    @Override
    void collectVariables(Set<Variable> variables) {
        for (Expression operand : operands) operand.collectVariables(variables);
    }

    @Override
    String contentString() {
        return Arrays.stream(operands).map(Expression::toString).collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation that = (Operation) o;
        return Arrays.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) operands);
    }
}



















