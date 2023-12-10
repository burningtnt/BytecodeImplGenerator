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
package net.burningtnt.bcigenerator.insn.holders;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class GeneralHolder {
    private GeneralHolder() {
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.literal("LDC").then(
                        CommandBuilder.ofArgumentInsn("STRING", StringArgumentType.string(), String.class, MethodVisitor::visitLdcInsn)
                ),

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
                CommandBuilder.ofGeneralInsn("SASTORE", Opcodes.SASTORE),

                CommandBuilder.ofGeneralInsn("POP", Opcodes.POP),
                CommandBuilder.ofGeneralInsn("POP2", Opcodes.POP2),
                CommandBuilder.ofGeneralInsn("DUP", Opcodes.DUP),
                CommandBuilder.ofGeneralInsn("DUP2", Opcodes.DUP2)
        );
    }
}
