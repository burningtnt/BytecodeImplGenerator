package net.burningtnt.bcigenerator.arguments.constant;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class ConstantArgumentType<T> implements ArgumentType<T> {
    private final ConstantType type;

    private final ArgumentType<T> delegate;

    private ConstantArgumentType(ConstantType type, ArgumentType<T> delegate) {
        this.type = type;
        this.delegate = delegate;
    }

    public static <T> ConstantArgumentType<T> of(ConstantType type, ArgumentType<T> delegate) {
        return new ConstantArgumentType<>(type, delegate);
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        reader.expect('(');
        String type = this.type.name();
        if (!reader.getString().regionMatches(reader.getCursor(), type, 0, type.length())) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(reader);
        }
        reader.setCursor(reader.getCursor() + type.length());
        reader.expect(' ');
        T value = this.delegate.parse(reader);
        reader.expect(')');
        return value;
    }

    public enum ConstantType {
        INT, FLOAT, LONG, DOUBLE, STRING, TYPE;
    }
}
