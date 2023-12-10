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

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.burningtnt.bcigenerator.arguments.JavaDescriptor;
import net.burningtnt.bcigenerator.arguments.JavaDescriptorArgumentType;
import net.burningtnt.bcigenerator.arguments.LabelIDArgumentType;
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ControlFlowHolder {
    private ControlFlowHolder() {
    }

    private static final class ManagedLabel {
        private final Label label;

        private boolean defined;

        public ManagedLabel(boolean defined) {
            this.label = new Label();
            this.defined = defined;
        }
    }

    private static final Map<MethodVisitor, Map<String, ManagedLabel>> labels = new HashMap<>();

    private static Label constructLabelInternal(MethodVisitor mv, String s, boolean defined) {
        ManagedLabel sl = labels.computeIfAbsent(mv, it -> new HashMap<>()).computeIfAbsent(s, it -> new ManagedLabel(defined));
        sl.defined = sl.defined || defined;
        return sl.label;
    }

    private static Label defineLabel(MethodVisitor mv, String s) {
        return constructLabelInternal(mv, s, true);
    }

    private static Label constructLabel(MethodVisitor mv, String s) {
        return constructLabelInternal(mv, s, false);
    }

    public static void verify() {
        for (Map.Entry<MethodVisitor, Map<String, ManagedLabel>> e1 : labels.entrySet()) {
            for (Map.Entry<String, ManagedLabel> e2 : e1.getValue().entrySet()) {
                if (!e2.getValue().defined) {
                    throw new IllegalStateException(String.format("Label '%s' is used but not defined.", e2.getKey()));
                }
            }
        }
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.ofLabel("IFEQ" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IFNE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFNE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IFLT" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFLT, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IFGE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFGE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IFGT" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFGT, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IFLE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IFLE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPEQ" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPNE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPNE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPLT" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPLT, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPGE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPGE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPGT" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPGT, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ICMPLE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPLE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ACMPEQ" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ACMPEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabel("IF_ACMPNE" , (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ACMPNE, constructLabel(mv, s))),
                CommandBuilder.ofLabel("GOTO" , (mv, s) -> mv.visitJumpInsn(Opcodes.GOTO, constructLabel(mv, s))),
                CommandBuilder.ofLabel("JSR" , (mv, s) -> mv.visitJumpInsn(Opcodes.JSR, constructLabel(mv, s))),
                CommandBuilder.ofVarInsn("RET", Opcodes.RET),

                CommandBuilder.ofArgumentInsn("LABEL", LabelIDArgumentType.label(), String.class, (mv, s) -> mv.visitLabel(defineLabel(mv, s))),
                CommandBuilder.literal("LOCALVARIABLE").then(
                        CommandBuilder.argument("name", StringArgumentType.string()).then(
                                CommandBuilder.argument("desc", JavaDescriptorArgumentType.single()).then(
                                        CommandBuilder.argument("begin", LabelIDArgumentType.label()).then(
                                                CommandBuilder.argument("end", LabelIDArgumentType.label()).then(
                                                        CommandBuilder.argument("index", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
                                                                mv -> mv.visitLocalVariable(
                                                                        context.getArgument("name", String.class),
                                                                        context.getArgument("desc", JavaDescriptor.class).getDesc(),
                                                                        null,
                                                                        constructLabel(mv, context.getArgument("begin", String.class)),
                                                                        constructLabel(mv, context.getArgument("end", String.class)),
                                                                        context.getArgument("index", Integer.class)
                                                                )
                                                        ))
                                                )
                                        )
                                )
                        )
                ),
                CommandBuilder.literal("MAXS").then(
                        CommandBuilder.argument("maxstack", IntegerArgumentType.integer()).then(
                                CommandBuilder.argument("maxlocals", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
                                        mv -> mv.visitMaxs(context.getArgument("maxstack", Integer.class), context.getArgument("maxlocals", Integer.class))
                                ))
                        )
                )
        );
    }
}
