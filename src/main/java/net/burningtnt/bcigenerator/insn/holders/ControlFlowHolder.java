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
import net.burningtnt.bcigenerator.arguments.*;
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

    private static final Map<MethodVisitor, Map<String, ManagedLabel>> labelCache = new HashMap<>();

    private static Label constructLabelInternal(MethodVisitor mv, String s, boolean defined) {
        ManagedLabel sl = labelCache.computeIfAbsent(mv, it -> new HashMap<>()).computeIfAbsent(s, it -> new ManagedLabel(defined));
        sl.defined = sl.defined || defined;
        return sl.label;
    }

    private static Label defineLabel(MethodVisitor mv, String s) {
        return constructLabelInternal(mv, s, true);
    }

    private static Label constructLabel(MethodVisitor mv, String s) {
        return constructLabelInternal(mv, s, false);
    }

    private static Label[] mapToLabels(MethodVisitor mv, List<?> list) {
        Label[] labels = new Label[list.size()];
        for (int i = 0; i < list.size(); i++) {
            labels[i] = constructLabel(mv, (String) list.get(i));
        }
        return labels;
    }

    public static void verify() {
        for (Map.Entry<MethodVisitor, Map<String, ManagedLabel>> e1 : labelCache.entrySet()) {
            for (Map.Entry<String, ManagedLabel> e2 : e1.getValue().entrySet()) {
                if (!e2.getValue().defined) {
                    throw new IllegalStateException(String.format("Label '%s' is used but not defined.", e2.getKey()));
                }
            }
        }
        labelCache.clear();
    }

    public static List<LiteralArgumentBuilder<List<IInsn>>> init() {
        return CommandBuilder.ofCommands(
                CommandBuilder.ofLabelInsn("IFEQ", (mv, s) -> mv.visitJumpInsn(Opcodes.IFEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IFNE", (mv, s) -> mv.visitJumpInsn(Opcodes.IFNE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IFLT", (mv, s) -> mv.visitJumpInsn(Opcodes.IFLT, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IFGE", (mv, s) -> mv.visitJumpInsn(Opcodes.IFGE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IFGT", (mv, s) -> mv.visitJumpInsn(Opcodes.IFGT, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IFLE", (mv, s) -> mv.visitJumpInsn(Opcodes.IFLE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPEQ", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPNE", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPNE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPLT", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPLT, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPGE", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPGE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPGT", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPGT, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ICMPLE", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ICMPLE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ACMPEQ", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ACMPEQ, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("IF_ACMPNE", (mv, s) -> mv.visitJumpInsn(Opcodes.IF_ACMPNE, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("GOTO", (mv, s) -> mv.visitJumpInsn(Opcodes.GOTO, constructLabel(mv, s))),
                CommandBuilder.ofLabelInsn("JSR", (mv, s) -> mv.visitJumpInsn(Opcodes.JSR, constructLabel(mv, s))),
                CommandBuilder.ofVarInsn("RET", Opcodes.RET),

                CommandBuilder.ofLiteralCommand("TABLESWITCH").then(
                        CommandBuilder.ofArgumentCommand("minValue", IntegerArgumentType.integer()).then(
                                CommandBuilder.ofArgumentCommand("branches", ListArgumentType.immutableList(LabelIDArgumentType.label())).then(
                                        CommandBuilder.ofArgumentCommand("defaultBranch", LabelIDArgumentType.label()).executes(CommandBuilder.execute(context ->
                                                mv -> {
                                                    int minValue = context.getArgument("minValue", Integer.class);
                                                    Label[] labels = mapToLabels(mv, context.getArgument("branches", List.class));
                                                    mv.visitTableSwitchInsn(
                                                            minValue,
                                                            minValue + labels.length - 1,
                                                            constructLabel(mv, context.getArgument("defaultBranch", String.class)),
                                                            labels
                                                    );
                                                }
                                        ))
                                )
                        )
                ),
                CommandBuilder.ofLiteralCommand("LOOKUPSWITCH").then(
                        CommandBuilder.ofArgumentCommand("branches", MapArgumentType.immutableMap(IntegerArgumentType.integer(), LabelIDArgumentType.label())).then(
                                CommandBuilder.ofArgumentCommand("defaultBranch", LabelIDArgumentType.label()).executes(CommandBuilder.execute(context ->
                                        mv -> {
                                            Map<?, ?> map = context.getArgument("branches", Map.class);
                                            Label[] labels = new Label[map.size()];
                                            int[] keys = new int[map.size()];
                                            int index = 0;
                                            for (Map.Entry<?, ?> entry : map.entrySet()) {
                                                keys[index] = (Integer) entry.getKey();
                                                labels[index] = constructLabel(mv, (String) entry.getValue());
                                                index++;
                                            }

                                            mv.visitLookupSwitchInsn(
                                                    constructLabel(mv, context.getArgument("defaultBranch", String.class)),
                                                    keys, labels
                                            );
                                        }
                                ))
                        )
                ),

                CommandBuilder.ofLiteralCommand("FRAME").then(
                        CommandBuilder.ofLiteralCommand("SAME").executes(CommandBuilder.execute(context ->
                                mv -> mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
                        ))
                ),

                CommandBuilder.ofArgumentInsn("LABEL", LabelIDArgumentType.label(), String.class, (mv, s) -> mv.visitLabel(defineLabel(mv, s))),
                CommandBuilder.ofLiteralCommand("LOCALVARIABLE").then(
                        CommandBuilder.ofArgumentCommand("name", StringArgumentType.string()).then(
                                CommandBuilder.ofArgumentCommand("desc", JavaDescriptorArgumentType.single()).then(
                                        CommandBuilder.ofArgumentCommand("begin", LabelIDArgumentType.label()).then(
                                                CommandBuilder.ofArgumentCommand("end", LabelIDArgumentType.label()).then(
                                                        CommandBuilder.ofArgumentCommand("index", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
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
                CommandBuilder.ofLiteralCommand("MAXS").then(
                        CommandBuilder.ofArgumentCommand("maxstack", IntegerArgumentType.integer()).then(
                                CommandBuilder.ofArgumentCommand("maxlocals", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
                                        mv -> mv.visitMaxs(context.getArgument("maxstack", Integer.class), context.getArgument("maxlocals", Integer.class))
                                ))
                        )
                )
        );
    }
}
