package model.expression;

public class Implication extends BinaryOperation {
    public Implication(Expression... operands) {
        super(operands);
    }

    @Override
    boolean apply(boolean leftOperandValue, boolean rightOperandValue) {
        return !leftOperandValue | rightOperandValue;
    }
}
