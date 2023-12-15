package net.burningtnt.bcigenerator.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.burningtnt.bcigenerator.utils.io.ExceptionalBiFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ListArgumentType<T> implements ArgumentType<List<T>> {
    private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_LIST = new SimpleCommandExceptionType(new LiteralMessage("Expected \"[ \" to start a list"));
    private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_LIST = new SimpleCommandExceptionType(new LiteralMessage("Expected \" ]\" to end a list"));
    private static final SimpleCommandExceptionType READER_EXPECTED_SEPARATE_OF_LIST = new SimpleCommandExceptionType(new LiteralMessage("Expected a space to separate a list"));

    private final Supplier<List<T>> listSupplier;

    private final ExceptionalBiFunction<List<T>, StringReader, List<T>, CommandSyntaxException> listFinisher;

    private final ArgumentType<T> elementType;

    private ListArgumentType(Supplier<List<T>> listSupplier, ExceptionalBiFunction<List<T>, StringReader, List<T>, CommandSyntaxException> listFinisher, ArgumentType<T> elementType) {
        this.listSupplier = listSupplier;
        this.listFinisher = listFinisher;
        this.elementType = elementType;
    }

    public static <T> ListArgumentType<T> custom(Supplier<List<T>> listSupplier, ExceptionalBiFunction<List<T>, StringReader, List<T>, CommandSyntaxException> listFinisher, ArgumentType<T> elementType) {
        return new ListArgumentType<>(listSupplier, listFinisher, elementType);
    }

    public static <T> ListArgumentType<T> immutableList(ArgumentType<T> elementType) {
        return new ListArgumentType<>(ArrayList::new, (list, reader) -> Collections.unmodifiableList(list), elementType);
    }

    private void expect(StringReader reader, char c, SimpleCommandExceptionType exceptionType) throws CommandSyntaxException {
        if (reader.canRead() && reader.peek() == c) {
            reader.skip();
        } else {
            throw exceptionType.createWithContext(reader);
        }
    }

    @Override
    public List<T> parse(StringReader reader) throws CommandSyntaxException {
        List<T> result = this.listSupplier.get();

        this.expect(reader, '[', READER_EXPECTED_START_OF_LIST);
        this.expect(reader, ' ', READER_EXPECTED_START_OF_LIST);
        while (reader.canRead()) {
            if (reader.peek() == ']') {
                reader.skip();
                return this.listFinisher.apply(result, reader);
            }

            result.add(this.elementType.parse(reader));
            this.expect(reader, ' ', READER_EXPECTED_SEPARATE_OF_LIST);
        }

        throw READER_EXPECTED_END_OF_LIST.createWithContext(reader);
    }
}
