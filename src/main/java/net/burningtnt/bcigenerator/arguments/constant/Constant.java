package net.burningtnt.bcigenerator.arguments.constant;

public final class Constant<T> {
    private final ConstantArgumentType.ConstantType type;

    private final T value;

    public Constant(ConstantArgumentType.ConstantType type, T value) {
        this.type = type;
        this.value = value;
    }

    public ConstantArgumentType.ConstantType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}
