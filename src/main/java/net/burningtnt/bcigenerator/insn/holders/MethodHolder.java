package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class MethodHolder {
    private MethodHolder() {
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
                CommandBuilder.ofMethodInsn("INVOKEVIRTUAL", Opcodes.INVOKEVIRTUAL),
                CommandBuilder.ofMethodInsn("INVOKESPECIAL", Opcodes.INVOKESPECIAL),
                CommandBuilder.ofMethodInsn("INVOKESTATIC", Opcodes.INVOKESTATIC),
                CommandBuilder.ofMethodInsn("INVOKEINTERFACE", Opcodes.INVOKEINTERFACE)
        );
    }
}
