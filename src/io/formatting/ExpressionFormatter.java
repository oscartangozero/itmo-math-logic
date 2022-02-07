package io.formatting;

import model.expression.*;

public class ExpressionFormatter extends AbstractFormatter {
    public static String format(Expression target) throws FormatException {
        return format(ExpressionFormatter::formatExpression, target);
    }

    public static void formatExpression(StringBuilder builder, Expression target) throws FormatException {
        formatExpression(builder, target, 0);
    }

    /* Expression ::
     *      = Expression '&' Expression
     *      | Expression '|' Expression
     *      | Expression '->' Expression
     *      | '!' Expression
     *      | '(' Expression ')'
     *      | Variable
     * Note: '&' and '|' are left-associative, '->' is right-associative */
    protected static void formatExpression(StringBuilder builder, Expression target, int outerPriority) throws FormatException {
        if (target instanceof Variable) {
            formatVariable(builder, (Variable) target);
        }
        else if (target instanceof Operation) {
            Operation operation = (Operation) target;
            if (getPriority(operation) <= outerPriority) {
                formatBracketedOperation(builder, operation);
            } else {
                formatOperation(builder, (Operation) target);
            }
        } else {
            throw FormatException.invalidObject(target);
        }
    }

    private static void formatBracketedOperation(StringBuilder builder, Operation target) throws FormatException {
        builder.append('(');
        formatOperation(builder, target);
        builder.append(')');
    }

    private static void formatOperation(StringBuilder builder, Operation target) throws FormatException {
        if (target instanceof UnaryOperation) {
            formatUnaryOperation(builder, (UnaryOperation) target);
        } else if (target instanceof BinaryOperation) {
            formatBinaryOperation(builder, (BinaryOperation) target);
        } else {
            throw FormatException.invalidObject(target);
        }
    }

    private static void formatUnaryOperation(StringBuilder builder, UnaryOperation target) throws FormatException {
        builder.append(getSign(target));
        formatExpression(builder, target.getOperand(), getPriority(target) - 1);
    }

    private static void formatBinaryOperation(StringBuilder builder, BinaryOperation target) throws FormatException {
        int priority = getPriority(target);
        formatExpression(builder, target.getLeftOperand(), priority + (isLeftAssociative(target) ? -1 : 0));
        builder.append(getSign(target));
        formatExpression(builder, target.getRightOperand(), priority + (isRightAssociative(target) ? -1 : 0));
    }
}
