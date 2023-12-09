package net.burningtnt.bcigenerator.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class LabelIDArgumentType implements ArgumentType<String> {
    private LabelIDArgumentType() {
    }

    public static LabelIDArgumentType label() {
        return new LabelIDArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(reader);
        }

        int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.read();
        }
        return reader.getString().substring(start, reader.getCursor());
    }
}
