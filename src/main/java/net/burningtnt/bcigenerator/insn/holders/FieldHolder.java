package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class FieldHolder {
    private FieldHolder() {
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
                CommandBuilder.ofFieldInsn("GETSTATIC", Opcodes.GETSTATIC),
                CommandBuilder.ofFieldInsn("PUTSTATIC", Opcodes.PUTSTATIC),
                CommandBuilder.ofFieldInsn("GETFIELD", Opcodes.GETFIELD),
                CommandBuilder.ofFieldInsn("PUTFIELD", Opcodes.PUTFIELD)
        );
    }
}
