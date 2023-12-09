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
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.burningtnt.bcigenerator.arguments.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.JavaDescriptorArgumentType;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class CommandBuilder {
    private CommandBuilder() {
    }

    public static LiteralArgumentBuilder<List<IInsn>> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <A> RequiredArgumentBuilder<List<IInsn>, A> argument(String name, ArgumentType<A> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    public static Command<List<IInsn>> execute(Function<CommandContext<List<IInsn>>, IInsn> consumer) {
        return context -> {
            context.getSource().add(consumer.apply(context));
            return Command.SINGLE_SUCCESS;
        };
    }

    @SafeVarargs
    public static CommandDispatcher<List<IInsn>> register(List<LiteralArgumentBuilder<List<IInsn>>>... items) {
        CommandDispatcher<List<IInsn>> dispatcher = new CommandDispatcher<>();
        for (List<LiteralArgumentBuilder<List<IInsn>>> holder : items) {
            for (LiteralArgumentBuilder<List<IInsn>> item : holder) {
                dispatcher.register(item);
            }
        }
        return dispatcher;
    }

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static LiteralArgumentBuilder<List<IInsn>> ofGeneralInsn(String op, int opcode) {
        return literal(op).executes(execute(context ->
                mv -> mv.visitInsn(opcode)
        ));
    }

    @SafeVarargs
    public static List<LiteralArgumentBuilder<List<IInsn>>> ofCommands(LiteralArgumentBuilder<List<IInsn>>... items) {
        return Collections.unmodifiableList(Arrays.asList(items));
    }

    public static <A> LiteralArgumentBuilder<List<IInsn>> ofArgumentInsn(String op, ArgumentType<A> argumentType, Class<A> clazz, BiConsumer<MethodVisitor, A> consumer) {
        String argumentName = "CommandBuilderAnonymousArgument-" + Long.toHexString(RANDOM.nextLong());
        return literal(op).then(
                CommandBuilder.argument(argumentName, argumentType).executes(CommandBuilder.execute(context ->
                        mv -> consumer.accept(mv, context.getArgument(argumentName, clazz))
                ))
        );
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofVarInsn(String op, int opcode) {
        return ofArgumentInsn(op, IntegerArgumentType.integer(), Integer.class, (mv, varIndex) -> mv.visitVarInsn(opcode, varIndex));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofFieldInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.field(), JavaDescriptor.class, (mv, desc) -> mv.visitFieldInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc()));
    }

    public static LiteralArgumentBuilder<List<IInsn>> ofMethodInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.method(), JavaDescriptor.class, (mv, desc) -> mv.visitMethodInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc(), opcode == Opcodes.INVOKEINTERFACE));
    }
}
