package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class ReturnHolder {
    private ReturnHolder() {
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
                CommandBuilder.ofGeneralInsn("IRETURN", Opcodes.IRETURN),
                CommandBuilder.ofGeneralInsn("LRETURN", Opcodes.LRETURN),
                CommandBuilder.ofGeneralInsn("FRETURN", Opcodes.FRETURN),
                CommandBuilder.ofGeneralInsn("DRETURN", Opcodes.DRETURN),
                CommandBuilder.ofGeneralInsn("ARETURN", Opcodes.ARETURN),
                CommandBuilder.ofGeneralInsn("RETURN", Opcodes.RETURN)
        );
    }
}
