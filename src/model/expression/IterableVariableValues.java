package model.expression;

import java.util.*;

public class IterableVariableValues implements VariableValues {
    private final String[] names;
    private final boolean[] values;

    public IterableVariableValues(Set<String> variableNames) {
        this.names = variableNames.toArray(new String[0]);
        Arrays.sort(this.names);
        this.values = new boolean[this.names.length];
    }

    @Override
    public boolean getValue(String variableName) {
        int index = Arrays.binarySearch(this.names, variableName);
        return values[index];
    }

    public boolean increment() {
        for (int i = 0; i < values.length; i++) {
            if (!values[i]) {
                values[i] = true;
                return true;
            }
            values[i] = false;
        }
        return false;
    }
}
