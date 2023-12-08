package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class VariableHolder {
    private VariableHolder() {
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
                CommandBuilder.ofArgumentInsn("LDC", StringArgumentType.string(), String.class, MethodVisitor::visitLdcInsn),

                CommandBuilder.ofVarInsn("ILOAD", Opcodes.ILOAD),
                CommandBuilder.ofVarInsn("LLOAD", Opcodes.LLOAD),
                CommandBuilder.ofVarInsn("FLOAD", Opcodes.FLOAD),
                CommandBuilder.ofVarInsn("DLOAD", Opcodes.DLOAD),
                CommandBuilder.ofVarInsn("ALOAD", Opcodes.ALOAD),
                CommandBuilder.ofGeneralInsn("IALOAD", Opcodes.IALOAD),
                CommandBuilder.ofGeneralInsn("LALOAD", Opcodes.LALOAD),
                CommandBuilder.ofGeneralInsn("FALOAD", Opcodes.FALOAD),
                CommandBuilder.ofGeneralInsn("DALOAD", Opcodes.DALOAD),
                CommandBuilder.ofGeneralInsn("AALOAD", Opcodes.AALOAD),
                CommandBuilder.ofGeneralInsn("BALOAD", Opcodes.BALOAD),
                CommandBuilder.ofGeneralInsn("CALOAD", Opcodes.CALOAD),
                CommandBuilder.ofGeneralInsn("SALOAD", Opcodes.SALOAD),

                CommandBuilder.ofVarInsn("ISTORE", Opcodes.ISTORE),
                CommandBuilder.ofVarInsn("LSTORE", Opcodes.LSTORE),
                CommandBuilder.ofVarInsn("FSTORE", Opcodes.FSTORE),
                CommandBuilder.ofVarInsn("DSTORE", Opcodes.DSTORE),
                CommandBuilder.ofVarInsn("ASTORE", Opcodes.ASTORE),

                CommandBuilder.ofGeneralInsn("IASTORE", Opcodes.IASTORE),
                CommandBuilder.ofGeneralInsn("LASTORE", Opcodes.LASTORE),
                CommandBuilder.ofGeneralInsn("FASTORE", Opcodes.FASTORE),
                CommandBuilder.ofGeneralInsn("DASTORE", Opcodes.DASTORE),
                CommandBuilder.ofGeneralInsn("AASTORE", Opcodes.AASTORE),
                CommandBuilder.ofGeneralInsn("BASTORE", Opcodes.BASTORE),
                CommandBuilder.ofGeneralInsn("CASTORE", Opcodes.CASTORE),
                CommandBuilder.ofGeneralInsn("SASTORE", Opcodes.SASTORE)
        );
    }
}
