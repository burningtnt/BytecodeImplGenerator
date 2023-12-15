package net.burningtnt.bcigenerator.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.burningtnt.bcigenerator.utils.io.ExceptionalBiFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class MapArgumentType<K, V> implements ArgumentType<Map<K, V>> {
    private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_MAP = new SimpleCommandExceptionType(new LiteralMessage("Expected \"{ \" to start a map"));
    private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_MAP = new SimpleCommandExceptionType(new LiteralMessage("Expected \" }\" to end a map"));
    private static final SimpleCommandExceptionType READER_EXPECTED_ITEM_SEPARATE_OF_MAP = new SimpleCommandExceptionType(new LiteralMessage("Expected \"| \" to separate the items in a map"));
    private static final SimpleCommandExceptionType READER_EXPECTED_KEY_VALUE_SEPARATE_OF_MAP = new SimpleCommandExceptionType(new LiteralMessage("Expected a space to separate the key-value pair in a map"));

    private final Supplier<Map<K, V>> mapSupplier;

    private final ExceptionalBiFunction<Map<K, V>, StringReader, Map<K, V>, CommandSyntaxException> mapFinisher;

    private final ArgumentType<K> keyType;

    private final ArgumentType<V> valueType;

    private MapArgumentType(Supplier<Map<K, V>> mapSupplier, ExceptionalBiFunction<Map<K, V>, StringReader, Map<K, V>, CommandSyntaxException> mapFinisher, ArgumentType<K> keyType, ArgumentType<V> valueType) {
        this.mapSupplier = mapSupplier;
        this.mapFinisher = mapFinisher;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public static <K, V> MapArgumentType<K ,V> custom(Supplier<Map<K, V>> mapSupplier, ExceptionalBiFunction<Map<K, V>, StringReader, Map<K, V>, CommandSyntaxException> mapFinisher, ArgumentType<K> keyType, ArgumentType<V> valueType) {
        return new MapArgumentType<>(mapSupplier, mapFinisher, keyType, valueType);
    }

    public static <K, V> MapArgumentType<K, V> immutableMap(ArgumentType<K> keyType, ArgumentType<V> valueType) {
        return new MapArgumentType<>(HashMap::new, (map, reader) -> Collections.unmodifiableMap(map), keyType, valueType);
    }

    private void expect(StringReader reader, char c, SimpleCommandExceptionType exceptionType) throws CommandSyntaxException {
        if (reader.canRead() && reader.peek() == c) {
            reader.skip();
        } else {
            throw exceptionType.createWithContext(reader);
        }
    }

    @Override
    public Map<K, V> parse(StringReader reader) throws CommandSyntaxException {
        Map<K, V> result = this.mapSupplier.get();

        this.expect(reader, '{', READER_EXPECTED_START_OF_MAP);
        this.expect(reader, ' ', READER_EXPECTED_START_OF_MAP);
        while (reader.canRead()) {
            if (reader.peek() == '}') {
                reader.skip();
                return this.mapFinisher.apply(result, reader);
            }

            if (!result.isEmpty()) {
                this.expect(reader, '|', READER_EXPECTED_ITEM_SEPARATE_OF_MAP);
                this.expect(reader, ' ', READER_EXPECTED_ITEM_SEPARATE_OF_MAP);
            }

            K key = this.keyType.parse(reader);
            this.expect(reader, ' ', READER_EXPECTED_KEY_VALUE_SEPARATE_OF_MAP);
            V value = this.valueType.parse(reader);
            this.expect(reader, ' ', READER_EXPECTED_KEY_VALUE_SEPARATE_OF_MAP);

            result.put(key, value);
        }

        throw READER_EXPECTED_END_OF_MAP.createWithContext(reader);
    }
}
