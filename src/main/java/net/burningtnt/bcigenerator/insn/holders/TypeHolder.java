package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.arguments.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.JavaDescriptorArgumentType;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class TypeHolder {
    private TypeHolder() {
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.ofTypeInsn("NEW", Opcodes.NEW),
                CommandBuilder.ofLiteralCommand("NEWARRAY").then(
                        CommandBuilder.ofIntInsn("BOOLEAN", Opcodes.T_BOOLEAN)
                ).then(
                        CommandBuilder.ofIntInsn("CHAR", Opcodes.T_CHAR)
                ).then(
                        CommandBuilder.ofIntInsn("FLOAT", Opcodes.T_FLOAT)
                ).then(
                        CommandBuilder.ofIntInsn("DOUBLE", Opcodes.T_DOUBLE)
                ).then(
                        CommandBuilder.ofIntInsn("BYTE", Opcodes.T_BYTE)
                ).then(
                        CommandBuilder.ofIntInsn("SHORT", Opcodes.T_SHORT)
                ).then(
                        CommandBuilder.ofIntInsn("INT", Opcodes.T_INT)
                ).then(
                        CommandBuilder.ofIntInsn("LONG", Opcodes.T_LONG)
                ),
                CommandBuilder.ofTypeInsn("ANEWARRAY", Opcodes.ANEWARRAY),
                CommandBuilder.ofIntInsn("ARRAYLENGTH", Opcodes.ARRAYLENGTH),
                CommandBuilder.ofIntInsn("ATHROW", Opcodes.ATHROW),
                CommandBuilder.ofTypeInsn("CHECKCAST", Opcodes.CHECKCAST),
                CommandBuilder.ofTypeInsn("INSTANCEOF", Opcodes.INSTANCEOF),
                CommandBuilder.ofGeneralInsn("MONITORENTER", Opcodes.MONITORENTER),
                CommandBuilder.ofGeneralInsn("MONITOREXIT", Opcodes.MONITOREXIT),
                CommandBuilder.ofLiteralCommand("MULTIANEWARRAY").then(
                        CommandBuilder.ofArgumentCommand("desc", JavaDescriptorArgumentType.clazz()).then(
                                CommandBuilder.ofArgumentCommand("dimensions", IntegerArgumentType.integer(0)).executes(CommandBuilder.execute(context ->
                                        mv -> mv.visitMultiANewArrayInsn(
                                                context.getArgument("desc", JavaDescriptor.class).getOwner(),
                                                context.getArgument("dimensions", Integer.class)
                                        )
                                ))
                        )
                )
        );
    }
}
