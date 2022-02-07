package model.expression;

public abstract class BinaryOperation extends Operation {
    public BinaryOperation(Expression... operands) {
        super(operands);
    }

    public Expression getLeftOperand() {
        return getOperand(0);
    }

    public Expression getRightOperand() {
        return getOperand(1);
    }

    abstract boolean apply(boolean leftOperandValue, boolean rightOperandValue);

    @Override
    public boolean evaluate(VariableValues variableValues) {
        return apply(getLeftOperand().evaluate(variableValues), getRightOperand().evaluate(variableValues));
    }
}
