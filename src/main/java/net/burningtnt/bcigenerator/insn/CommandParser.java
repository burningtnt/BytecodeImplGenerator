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
package net.burningtnt.bcigenerator.insn;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.burningtnt.bcigenerator.insn.holders.*;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public final class CommandParser {
    private CommandParser() {
    }

    private static final CommandDispatcher<List<IInsn>> dispatcher = initCommandDispatcher(
            GeneralHolder.init(),
            ControlFlowHolder.init(),
            MiscHolder.init(),
            ReturnHolder.init(),
            FieldHolder.init(),
            MethodHolder.init(),
            TypeHolder.init()
    );

    public static void parse(String code, List<IInsn> context) throws CommandSyntaxException {
        dispatcher.execute(code, context);
    }

    public static void write(MethodVisitor mv, List<IInsn> context) {
        for (IInsn insn : context) {
            insn.write(mv);
        }
        ControlFlowHolder.verify(mv);
    }

    @SafeVarargs
    private static CommandDispatcher<List<IInsn>> initCommandDispatcher(List<LiteralArgumentBuilder<List<IInsn>>>... items) {
        CommandDispatcher<List<IInsn>> dispatcher = new CommandDispatcher<>();
        for (List<LiteralArgumentBuilder<List<IInsn>>> holder : items) {
            for (LiteralArgumentBuilder<List<IInsn>> item : holder) {
                dispatcher.register(item);
            }
        }
        return dispatcher;
    }
}
