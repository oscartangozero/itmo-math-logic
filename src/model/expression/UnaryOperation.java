package model.expression;

public abstract class UnaryOperation extends Operation {
    public UnaryOperation(Expression... operands) {
        super(operands);
    }

    public Expression getOperand() {
        return getOperand(0);
    }

    abstract boolean apply(boolean operandValue);

    @Override
    public boolean evaluate(VariableValues variableValues) {
        return apply(getOperand().evaluate(variableValues));
    }
}
