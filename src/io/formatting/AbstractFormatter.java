package io.formatting;

import model.expression.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFormatter {
    private static final Map<Class<? extends Operation>, CharSequence> OP_SIGNS = new HashMap<>();
    private static final Map<Class<? extends Operation>, Integer> OP_PRIORITIES = new HashMap<>();
    private static final Map<Class<? extends BinaryOperation>, BinaryAssociativity> BIN_OP_ASSOCIATIVITY = new HashMap<>();

    protected enum BinaryAssociativity {LEFT, RIGHT}

    static {
        OP_SIGNS.put(Implication.class, "->");
        OP_SIGNS.put(Disjunction.class, "|");
        OP_SIGNS.put(Conjunction.class, "&");
        OP_SIGNS.put(Negation.class, "!");

        OP_PRIORITIES.put(Implication.class, 1);
        OP_PRIORITIES.put(Disjunction.class, 2);
        OP_PRIORITIES.put(Conjunction.class, 3);
        OP_PRIORITIES.put(Negation.class, 4);

        BIN_OP_ASSOCIATIVITY.put(Implication.class, BinaryAssociativity.RIGHT);
        BIN_OP_ASSOCIATIVITY.put(Disjunction.class, BinaryAssociativity.LEFT);
        BIN_OP_ASSOCIATIVITY.put(Conjunction.class, BinaryAssociativity.LEFT);
    }

    protected static int getPriority(Operation operation) throws FormatException {
        Integer priority = OP_PRIORITIES.get(operation.getClass());
        if (priority == null) throw FormatException.invalidObject(operation);
        return priority;
    }

    protected static BinaryAssociativity getAssociativity(BinaryOperation binaryOperation) {
        return BIN_OP_ASSOCIATIVITY.get(binaryOperation.getClass());
    }

    protected static boolean isRightAssociative(BinaryOperation binaryOperation) {
        return getAssociativity(binaryOperation) == BinaryAssociativity.RIGHT;
    }

    protected static boolean isLeftAssociative(BinaryOperation binaryOperation) {
        return getAssociativity(binaryOperation) == BinaryAssociativity.LEFT;
    }

    /* Sign ::= '->' | '|' | '&' | '!' */
    protected static CharSequence getSign(Operation operation) {
        return OP_SIGNS.get(operation.getClass());
    }

    /* Variable ::= ('A'...'Z') {'A'...'Z'|'0'...'9'|'''}  */
    protected static boolean formatVariable(StringBuilder builder, Variable variable) {
        builder.append(variable.getName());
        return true;
    }

    @FunctionalInterface
    protected interface FormatFunction<T> {
        void apply(StringBuilder builder, T value) throws FormatException;
    }

    protected static <T> String format(FormatFunction<T> formatter, T value) throws FormatException {
        StringBuilder builder = new StringBuilder();
        formatter.apply(builder, value);
        return builder.toString();
    }
}
