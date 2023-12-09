/*
 * Copyright 2023 Burning_TNT
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.burningtnt.bcigenerator.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;

public final class JavaDescriptorArgumentType implements ArgumentType<JavaDescriptor> {
    private static final DynamicCommandExceptionType INVALID_DESC = new DynamicCommandExceptionType(chr -> new LiteralMessage("Invalid JVM descriptor: '" + chr + ";."));

    private final DescriptorType type;

    private JavaDescriptorArgumentType(DescriptorType type) {
        this.type = type;
    }

    public static JavaDescriptorArgumentType single() {
        return new JavaDescriptorArgumentType(DescriptorType.SINGLE);
    }
    public static JavaDescriptorArgumentType clazz() {
        return new JavaDescriptorArgumentType(DescriptorType.CLAZZ);
    }

    public static JavaDescriptorArgumentType field() {
        return new JavaDescriptorArgumentType(DescriptorType.FIELD);
    }

    public static JavaDescriptorArgumentType method() {
        return new JavaDescriptorArgumentType(DescriptorType.METHOD);
    }

    private static String readDesc(StringReader reader) throws CommandSyntaxException {
        StringBuilder desc = new StringBuilder();
        loop:
        while (true) {
            char current = reader.read();
            switch (current) {
                case '[': {
                    desc.append('[');
                    continue;
                }
                case 'V':
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z': {
                    desc.append(current);
                    break loop;
                }
                case 'L': {
                    desc.append(current);
                    desc.append(reader.readStringUntil(';'));
                    desc.append(';');
                    break loop;
                }
                default: {
                    throw INVALID_DESC.createWithContext(reader, String.valueOf(current));
                }
            }
        }
        return desc.toString();
    }

    @Override
    public JavaDescriptor parse(StringReader reader) throws CommandSyntaxException {
        if (this.type == DescriptorType.SINGLE) {
            return new JavaDescriptor(null, null, readDesc(reader));
        }

        if (this.type == DescriptorType.CLAZZ) {
            String clazz = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return new JavaDescriptor(clazz,null, null);
        }

        char first = reader.read();
        if (first != 'L') {
            throw INVALID_DESC.createWithContext(reader, String.valueOf(first));
        }
        String owner = reader.readStringUntil(';');
        String name = reader.readStringUntil(this.type == DescriptorType.FIELD ? ':' : '(');

        String desc;
        if (this.type == DescriptorType.FIELD) {
            desc = readDesc(reader);
        } else {
            StringBuilder builder = new StringBuilder(reader.getRemainingLength()).append('(');
            while (reader.peek() != ')') {
                builder.append(readDesc(reader));
            }
            builder.append(reader.read() /* Must be ')' */);
            builder.append(readDesc(reader));
            desc = builder.toString();
        }

        return new JavaDescriptor(owner, name, desc);
    }

    public enum DescriptorType {
        SINGLE("\"Lorg/objectweb/asm/Opcodes;", "I"),
        CLAZZ("org/objectweb/asm/Opcodes", "java/lang/System"),
        FIELD("Lorg/objectweb/asm/Opcodes;RETURN:I", "Ljava/lang/System;out:Ljava/io/PrintStream;"),
        METHOD("Ljava/io/PrintStream;println(Ljava/lang/String;)V", "Lcom/mojang/brigadier/arguments/StringArgumentType;<init>(Lcom/mojang/brigadier/arguments/StringArgumentType$StringType;)V");

        private final Collection<String> examples;

        DescriptorType(String... examples) {
            this.examples = Arrays.asList(examples);
        }

        public Collection<String> getExamples() {
            return this.examples;
        }
    }
}
