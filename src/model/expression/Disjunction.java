package model.expression;

public class Disjunction extends BinaryOperation {
    public Disjunction(Expression... operands) {
        super(operands);
    }

    @Override
    boolean apply(boolean leftOperandValue, boolean rightOperandValue) {
        return leftOperandValue | rightOperandValue;
    }
}
