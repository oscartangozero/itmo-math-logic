package model.proof;

import model.expression.Expression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AxiomVariableSlots implements Map<String, Expression> {
    private static final char NAME_BASE = 'A';
    private final Expression[] slots;

    public AxiomVariableSlots(int size) {
        this.slots = new Expression[size];
    }

    public AxiomVariableSlots(Expression... slots) {
        this.slots = slots;
    }

    private int getIndex(Object str) {
        return ((String) str).charAt(0) - NAME_BASE;
    }

    @Override
    public Expression get(Object key) {
        return slots[getIndex(key)];
    }

    @Override
    public int size() {
        return slots.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) return false;
        if (!(((String) key).length() == 1)) return false;
        int index = getIndex(key);
        return 0 <= index && index < size();
    }

    @Override
    public boolean containsValue(Object value) {
        return Arrays.asList(slots).contains(value);
    }

    @Override
    public Expression put(String key, Expression value) {
        int index = getIndex(key);
        Expression formerValue = slots[index];
        slots[index] = value;
        return formerValue;
    }

    @Override
    public Expression remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Expression> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Expression> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<String, Expression>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
