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

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.arguments.constant.ConstantArgumentType;
import net.burningtnt.bcigenerator.arguments.desc.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.desc.JavaDescriptorArgumentType;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

public final class GeneralHolder {
    private GeneralHolder() {
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.ofGeneralInsn("NOP", Opcodes.NOP),
                CommandBuilder.ofGeneralInsn("ACONST_NULL", Opcodes.ACONST_NULL),
                CommandBuilder.ofGeneralInsn("ICONST_M1", Opcodes.ICONST_M1),
                CommandBuilder.ofGeneralInsn("ICONST_0", Opcodes.ICONST_0),
                CommandBuilder.ofGeneralInsn("ICONST_1", Opcodes.ICONST_1),
                CommandBuilder.ofGeneralInsn("ICONST_2", Opcodes.ICONST_2),
                CommandBuilder.ofGeneralInsn("ICONST_3", Opcodes.ICONST_3),
                CommandBuilder.ofGeneralInsn("ICONST_4", Opcodes.ICONST_4),
                CommandBuilder.ofGeneralInsn("ICONST_5", Opcodes.ICONST_5),
                CommandBuilder.ofGeneralInsn("LCONST_0", Opcodes.LCONST_0),
                CommandBuilder.ofGeneralInsn("LCONST_1", Opcodes.LCONST_1),
                CommandBuilder.ofGeneralInsn("FCONST_0", Opcodes.FCONST_0),
                CommandBuilder.ofGeneralInsn("FCONST_1", Opcodes.FCONST_1),
                CommandBuilder.ofGeneralInsn("FCONST_2", Opcodes.FCONST_2),
                CommandBuilder.ofGeneralInsn("DCONST_0", Opcodes.DCONST_0),
                CommandBuilder.ofGeneralInsn("DCONST_1", Opcodes.DCONST_1),
                CommandBuilder.ofIntInsn("BIPUSH", Opcodes.BIPUSH, Byte.MIN_VALUE, Byte.MAX_VALUE),
                CommandBuilder.ofIntInsn("SIPUSH", Opcodes.SIPUSH, Short.MIN_VALUE, Short.MAX_VALUE),
                CommandBuilder.ofLiteralCommand("LDC").then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.INT, IntegerArgumentType.integer()), Integer.class, MethodVisitor::visitLdcInsn)
                ).then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.FLOAT, FloatArgumentType.floatArg()), Float.class, MethodVisitor::visitLdcInsn)
                ).then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.LONG, LongArgumentType.longArg()), Long.class, MethodVisitor::visitLdcInsn)
                ).then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.DOUBLE, DoubleArgumentType.doubleArg()), Double.class, MethodVisitor::visitLdcInsn)
                ).then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.STRING, StringArgumentType.string()), String.class, MethodVisitor::visitLdcInsn)
                ).then(
                        CommandBuilder.ofArgumentInsn(ConstantArgumentType.of(ConstantArgumentType.ConstantType.TYPE, JavaDescriptorArgumentType.desc()), JavaDescriptor.class, (mv, desc) -> mv.visitLdcInsn(Type.getType(desc.getDesc())))
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
                CommandBuilder.ofGeneralInsn("DUP2", Opcodes.DUP2),
                CommandBuilder.ofGeneralInsn("DUP2_X1", Opcodes.DUP2_X1),
                CommandBuilder.ofGeneralInsn("DUP2_X2", Opcodes.DUP2_X2),
                CommandBuilder.ofGeneralInsn("SWAP", Opcodes.SWAP),

                CommandBuilder.ofGeneralInsn("SWAP", Opcodes.SWAP),
                CommandBuilder.ofGeneralInsn("IADD", Opcodes.IADD),
                CommandBuilder.ofGeneralInsn("LADD", Opcodes.LADD),
                CommandBuilder.ofGeneralInsn("FADD", Opcodes.FADD),
                CommandBuilder.ofGeneralInsn("DADD", Opcodes.DADD),
                CommandBuilder.ofGeneralInsn("ISUB", Opcodes.ISUB),
                CommandBuilder.ofGeneralInsn("LSUB", Opcodes.LSUB),
                CommandBuilder.ofGeneralInsn("FSUB", Opcodes.FSUB),
                CommandBuilder.ofGeneralInsn("DSUB", Opcodes.DSUB),
                CommandBuilder.ofGeneralInsn("IMUL", Opcodes.IMUL),
                CommandBuilder.ofGeneralInsn("LMUL", Opcodes.LMUL),
                CommandBuilder.ofGeneralInsn("FMUL", Opcodes.FMUL),
                CommandBuilder.ofGeneralInsn("DMUL", Opcodes.DMUL),
                CommandBuilder.ofGeneralInsn("IDIV", Opcodes.IDIV),
                CommandBuilder.ofGeneralInsn("LDIV", Opcodes.LDIV),
                CommandBuilder.ofGeneralInsn("FDIV", Opcodes.FDIV),
                CommandBuilder.ofGeneralInsn("DDIV", Opcodes.DDIV),
                CommandBuilder.ofGeneralInsn("IREM", Opcodes.IREM),
                CommandBuilder.ofGeneralInsn("LREM", Opcodes.LREM),
                CommandBuilder.ofGeneralInsn("FREM", Opcodes.FREM),
                CommandBuilder.ofGeneralInsn("DREM", Opcodes.DREM),
                CommandBuilder.ofGeneralInsn("INEG", Opcodes.INEG),
                CommandBuilder.ofGeneralInsn("LNEG", Opcodes.LNEG),
                CommandBuilder.ofGeneralInsn("FNEG", Opcodes.FNEG),
                CommandBuilder.ofGeneralInsn("DNEG", Opcodes.DNEG),
                CommandBuilder.ofGeneralInsn("ISHL", Opcodes.ISHL),
                CommandBuilder.ofGeneralInsn("LSHL", Opcodes.LSHL),
                CommandBuilder.ofGeneralInsn("ISHR", Opcodes.ISHR),
                CommandBuilder.ofGeneralInsn("LSHR", Opcodes.LSHR),
                CommandBuilder.ofGeneralInsn("IUSHR", Opcodes.IUSHR),
                CommandBuilder.ofGeneralInsn("LUSHR", Opcodes.LUSHR),
                CommandBuilder.ofGeneralInsn("IAND", Opcodes.IAND),
                CommandBuilder.ofGeneralInsn("LAND", Opcodes.LAND),
                CommandBuilder.ofGeneralInsn("IOR", Opcodes.IOR),
                CommandBuilder.ofGeneralInsn("LOR", Opcodes.LOR),
                CommandBuilder.ofGeneralInsn("IXOR", Opcodes.IXOR),
                CommandBuilder.ofGeneralInsn("LXOR", Opcodes.LXOR),
                CommandBuilder.ofLiteralCommand("IINC").then(
                        CommandBuilder.ofArgumentCommand("varIndex", IntegerArgumentType.integer()).then(
                                CommandBuilder.ofArgumentCommand("increment", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
                                        mv -> mv.visitIincInsn(context.getArgument("varIndex", Integer.class), context.getArgument("increment", Integer.class))
                                ))
                        )
                ),

                CommandBuilder.ofGeneralInsn("I2L", Opcodes.I2L),
                CommandBuilder.ofGeneralInsn("I2F", Opcodes.I2F),
                CommandBuilder.ofGeneralInsn("I2D", Opcodes.I2D),
                CommandBuilder.ofGeneralInsn("L2I", Opcodes.L2I),
                CommandBuilder.ofGeneralInsn("L2F", Opcodes.L2F),
                CommandBuilder.ofGeneralInsn("L2D", Opcodes.L2D),
                CommandBuilder.ofGeneralInsn("F2I", Opcodes.F2I),
                CommandBuilder.ofGeneralInsn("F2L", Opcodes.F2L),
                CommandBuilder.ofGeneralInsn("F2D", Opcodes.F2D),
                CommandBuilder.ofGeneralInsn("D2I", Opcodes.D2I),
                CommandBuilder.ofGeneralInsn("D2L", Opcodes.D2L),
                CommandBuilder.ofGeneralInsn("D2F", Opcodes.D2F),
                CommandBuilder.ofGeneralInsn("I2B", Opcodes.I2B),
                CommandBuilder.ofGeneralInsn("I2C", Opcodes.I2C),
                CommandBuilder.ofGeneralInsn("I2S", Opcodes.I2S),

                CommandBuilder.ofGeneralInsn("LCMP", Opcodes.LCMP),
                CommandBuilder.ofGeneralInsn("FCMPL", Opcodes.FCMPL),
                CommandBuilder.ofGeneralInsn("FCMPG", Opcodes.FCMPG),
                CommandBuilder.ofGeneralInsn("DCMPL", Opcodes.DCMPL),
                CommandBuilder.ofGeneralInsn("DCMPG", Opcodes.DCMPG)
        );
    }
}
