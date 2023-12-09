package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.arguments.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.JavaDescriptorArgumentType;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class TypeHolder {
    private TypeHolder() {
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.ofArgumentInsn("NEW", JavaDescriptorArgumentType.clazz(), JavaDescriptor.class, (mv, desc) -> mv.visitTypeInsn(Opcodes.NEW, desc.getOwner()))
        );
    }
}
