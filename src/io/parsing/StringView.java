package io.parsing;

public class StringView implements CharSequence {
    private final String buffer;
    private int start;
    private int end;

    public StringView(String buffer) {
        this(buffer, 0, buffer.length());
    }

    public StringView(StringView other) {
        this(other.buffer, other.start, other.end);
    }

    private StringView(String buffer, int start, int end) {
        this.buffer = buffer;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    public boolean isEmpty() {
        return start == end;
    }

    @Override
    public char charAt(int index) {
        return buffer.charAt(start + index);
    }

    public char getChar() {
        return buffer.charAt(start);
    }

    // Assumes that 0 <= shift <=end
    public void shiftStartPosition(int shift) {
        start += shift;
    }

    public boolean startsWith(char prefix) {
        return !isEmpty() && getChar() == prefix;
    }

    public boolean startsWith(String prefix) {
        return buffer.regionMatches(start, prefix, 0, prefix.length());
    }

    public int indexOf(String pattern) {
        int bufferIndex = buffer.indexOf(pattern, start);
        if (bufferIndex + pattern.length() > end) return -1;
        return bufferIndex;
    }

    public StringView subView(int start, int end) {
        if (0 <= start && start <= end && end <= length()) {
            return new StringView(buffer, start, end);
        }
        throw new IndexOutOfBoundsException();
    }

    public String subString(int start, int end) {
        if (0 <= start && start <= end && end <= length()) {
            return buffer.substring(this.start + start, this.start + end);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return subView(start, end);
    }

    public StringView removePrefix(int length) {
        StringView prefix = subView(0, length);
        shiftStartPosition(length);
        return prefix;
    }

    public String removePrefixString(int length) {
        String prefix = subString(0, length);
        shiftStartPosition(length);
        return prefix;
    }

    @Override
    public String toString() {
        return "StringView(" + buffer.subSequence(start, end) + ")";
    }
}






















