package io.formatting;

import model.expression.*;

public class ExpressionPrefixFormatter extends AbstractFormatter {
    public static String format(Expression target) throws FormatException {
        return format(ExpressionPrefixFormatter::formatNode, target);
    }

    /* Expression ::= Node
     * Node ::
     *      = '(' Sign ',' Node ',' Node ')'
     *      | '(!' Node ')'
     *      |  Variable                 */
    private static void formatNode(StringBuilder builder, Expression target) throws FormatException {
        if (target instanceof Variable) formatVariable(builder, (Variable) target);
        else {
            builder.append('(');
            if (target instanceof Negation) {
                Negation negation = (Negation) target;
                builder.append(getSign(negation));
                formatNode(builder, negation.getOperand());
            } else if (target instanceof BinaryOperation) {
                BinaryOperation binaryOperation = (BinaryOperation) target;
                builder.append(getSign(binaryOperation));
                builder.append(',');
                formatNode(builder, binaryOperation.getLeftOperand());
                builder.append(',');
                formatNode(builder, binaryOperation.getRightOperand());
            } else {
                throw FormatException.invalidObject(target);
            }
            builder.append(')');
        }
    }
}
