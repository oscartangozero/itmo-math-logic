package model.expression;

public class Conjunction extends BinaryOperation {
    public Conjunction(Expression... operands) {
        super(operands);
    }

    @Override
    boolean apply(boolean leftOperandValue, boolean rightOperandValue) {
        return leftOperandValue & rightOperandValue;
    }
}
