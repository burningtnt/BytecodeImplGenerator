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
