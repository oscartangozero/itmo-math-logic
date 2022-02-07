package model.expression;

public class Negation extends UnaryOperation {
    public Negation(Expression... operands) {
        super(operands);
    }

    @Override
    boolean apply(boolean operandValue) {
        return !operandValue;
    }
}
