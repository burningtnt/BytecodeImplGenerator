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
import net.burningtnt.bcigenerator.insn.CommandBuilder;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StructureHolder {
    private StructureHolder() {
    }

    private static final class SafeLabel {
        private final Label label;

        private boolean defined;

        public SafeLabel(boolean defined) {
            this.label = new Label();
            this.defined = defined;
        }
    }

    private static final Map<MethodVisitor, Map<String, SafeLabel>> labels = new HashMap<>();

    private static Label computeLabel(MethodVisitor mv, String s, boolean defined) {
        SafeLabel sl = labels.computeIfAbsent(mv, it -> new HashMap<>()).computeIfAbsent(s, it -> new SafeLabel(defined));
        sl.defined = sl.defined || defined;
        return sl.label;
    }

    public static void verify() {
        for (Map.Entry<MethodVisitor, Map<String, SafeLabel>> e1 : labels.entrySet()) {
            for (Map.Entry<String, SafeLabel> e2 : e1.getValue().entrySet()) {
                if (!e2.getValue().defined) {
                    throw new IllegalStateException(String.format("Label '%s' is used but not defined.", e2.getKey()));
                }
            }
        }
    }

    public static List<LiteralArgumentBuilder<Parser.InsnReference>> init() {
        return List.of(
                CommandBuilder.ofArgumentInsn("LABEL", StringArgumentType.string(), String.class, (mv, s) -> mv.visitLabel(computeLabel(mv, s, true))),
                CommandBuilder.literal("LOCALVARIABLE").then(
                        CommandBuilder.argument("name", StringArgumentType.string()).then(
                                CommandBuilder.argument("desc", JavaDescriptorArgumentType.single()).then(
                                        CommandBuilder.argument("begin", StringArgumentType.string()).then(
                                                CommandBuilder.argument("end", StringArgumentType.string()).then(
                                                        CommandBuilder.argument("index", IntegerArgumentType.integer()).executes(CommandBuilder.execute(context ->
                                                                mv -> mv.visitLocalVariable(
                                                                        context.getArgument("name", String.class),
                                                                        context.getArgument("desc", JavaDescriptor.class).getOwner(),
                                                                        null,
                                                                        computeLabel(mv, context.getArgument("begin", String.class), false),
                                                                        computeLabel(mv, context.getArgument("end", String.class), false),
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
