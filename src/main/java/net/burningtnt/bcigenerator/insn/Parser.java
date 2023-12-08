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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.burningtnt.bcigenerator.insn.holders.*;

public final class Parser {
    public static class InsnReference {
        private IInsn insn;

        private boolean exist = false;

        public InsnReference() {
        }

        public IInsn getInsn() {
            if (!this.exist) {
                throw new IllegalStateException("Insn has not been initialized.");
            }
            return this.insn;
        }

        public void setInsn(IInsn insn) {
            this.insn = insn;
            this.exist = true;
        }

        public void clean() {
            this.exist = false;
        }
    }

    private Parser() {
    }

    private static final CommandDispatcher<InsnReference> dispatcher = CommandBuilder.register(
            StructureHolder.init(),
            MiscHolder.init(),
            VariableHolder.init(),
            ReturnHolder.init(),
            FieldHolder.init(),
            MethodHolder.init()
    );

    public static IInsn parse(String code) throws CommandSyntaxException {
        InsnReference reference = new InsnReference();
        dispatcher.execute(code, reference);
        return reference.getInsn();
    }
}
