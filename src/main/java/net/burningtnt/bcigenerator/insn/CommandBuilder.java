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
package net.burningtnt.bcigenerator.insn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.burningtnt.bcigenerator.arguments.LabelIDArgumentType;
import net.burningtnt.bcigenerator.arguments.desc.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.desc.JavaDescriptorArgumentType;
import org.objectweb.asm.MethodVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class CommandBuilder {
    private CommandBuilder() {
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofLiteralCommand(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <A> RequiredArgumentBuilder<List<IInsn>, A> ofArgumentCommand(String name, ArgumentType<A> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    public static Command<List<IInsn>> execute(Function<CommandContext<List<IInsn>>, IInsn> consumer) {
        return context -> {
            context.getSource().add(consumer.apply(context));
            return Command.SINGLE_SUCCESS;
        };
    }

    @SafeVarargs
    public static List<LiteralArgumentBuilder<List<IInsn>>> ofCommands(LiteralArgumentBuilder<List<IInsn>>... items) {
        return Collections.unmodifiableList(Arrays.asList(items));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofGeneralInsn(String op, int opcode) {
        return ofLiteralCommand(op).executes(execute(context ->
                mv -> mv.visitInsn(opcode)
        ));
    }

    private static final ThreadLocalRandom ARGUMENT_NAME_RANDOM = ThreadLocalRandom.current();
    private static long anonymousArgumentGlobalIndex = 0;

    private static String generateArgName() {
        return String.format("net.burningtnt.bcigenerator.insn.CommandBuilder/%016x@%016x", ARGUMENT_NAME_RANDOM.nextLong(), anonymousArgumentGlobalIndex++);
    }

    public static <A> LiteralArgumentBuilder<List<IInsn>> ofArgumentInsn(String op, ArgumentType<A> argumentType, Class<A> clazz, BiConsumer<MethodVisitor, A> consumer) {
        return ofLiteralCommand(op).then(ofArgumentInsn(argumentType, clazz, consumer));
    }

    public static <A> RequiredArgumentBuilder<List<IInsn>, A> ofArgumentInsn(ArgumentType<A> argumentType, Class<A> clazz, BiConsumer<MethodVisitor, A> consumer) {
        String argumentName = generateArgName();
        return ofArgumentCommand(argumentName, argumentType).executes(execute(context ->
                mv -> consumer.accept(mv, context.getArgument(argumentName, clazz))
        ));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofLabelInsn(String op, BiConsumer<MethodVisitor, String> consumer) {
        return ofArgumentInsn(op, LabelIDArgumentType.label(), String.class, consumer);
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofIntInsn(String op, int opcode) {
        return ofIntInsn(op, opcode, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofIntInsn(String op, int opcode, int min, int max) {
        return ofArgumentInsn(op, IntegerArgumentType.integer(min, max), Integer.class, (mv, integer) -> mv.visitIntInsn(opcode, integer));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofVarInsn(String op, int opcode) {
        return ofArgumentInsn(op, IntegerArgumentType.integer(), Integer.class, (mv, varIndex) -> mv.visitVarInsn(opcode, varIndex));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofFieldInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.field(), JavaDescriptor.class, (mv, desc) -> mv.visitFieldInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc()));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofMethodInsn(String op, int opcode, boolean isInterface) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.method(), JavaDescriptor.class, (mv, desc) -> mv.visitMethodInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc(), isInterface));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofClassTypeInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.desc(), JavaDescriptor.class, (mv, desc) -> mv.visitTypeInsn(opcode, desc.getDesc()));
    }
}
