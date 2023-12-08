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
