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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class CommandBuilder {
    private CommandBuilder() {
    }

    public static LiteralArgumentBuilder<Parser.InsnReference> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <A> RequiredArgumentBuilder<Parser.InsnReference, A> argument(String name, ArgumentType<A> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    public static Command<Parser.InsnReference> execute(Function<CommandContext<Parser.InsnReference>, IInsn> consumer) {
        return context -> {
            context.getSource().setInsn(consumer.apply(context));
            return Command.SINGLE_SUCCESS;
        };
    }

    @SafeVarargs
    public static CommandDispatcher<Parser.InsnReference> register(List<LiteralArgumentBuilder<Parser.InsnReference>>... items) {
        CommandDispatcher<Parser.InsnReference> dispatcher = new CommandDispatcher<>();
        for (List<LiteralArgumentBuilder<Parser.InsnReference>> holder : items) {
            for (LiteralArgumentBuilder<Parser.InsnReference> item : holder) {
                dispatcher.register(item);
            }
        }
        return dispatcher;
    }

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static LiteralArgumentBuilder<Parser.InsnReference> ofGeneralInsn(String op, int opcode) {
        return literal(op).executes(execute(context ->
                mv -> mv.visitInsn(opcode)
        ));
    }

    public static <A> LiteralArgumentBuilder<Parser.InsnReference> ofArgumentInsn(String op, ArgumentType<A> argumentType, Class<A> clazz, BiConsumer<MethodVisitor, A> consumer) {
        String argumentName = "CommandBuilderAnonymousArgument-" + Long.toHexString(RANDOM.nextLong());
        return literal(op).then(
                CommandBuilder.argument(argumentName, argumentType).executes(CommandBuilder.execute(context ->
                        mv -> consumer.accept(mv, context.getArgument(argumentName, clazz))
                ))
        );
    }

    public static LiteralArgumentBuilder<Parser.InsnReference> ofVarInsn(String op, int opcode) {
        return ofArgumentInsn(op, IntegerArgumentType.integer(), Integer.class, (mv, varIndex) -> mv.visitVarInsn(opcode, varIndex));
    }

    public static LiteralArgumentBuilder<Parser.InsnReference> ofFieldInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.field(), JavaDescriptor.class, (mv, desc) -> mv.visitFieldInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc()));
    }

    public static LiteralArgumentBuilder<Parser.InsnReference> ofMethodInsn(String op, int opcode) {
        return ofArgumentInsn(op, JavaDescriptorArgumentType.method(), JavaDescriptor.class, (mv, desc) -> mv.visitMethodInsn(opcode, desc.getOwner(), desc.getName(), desc.getDesc(), opcode == Opcodes.INVOKEINTERFACE));
    }
}
