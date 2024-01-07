package net.burningtnt.bcidemos;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Demo5 {
    private Demo5() {
    }

    @BytecodeImpl({
            "BIPUSH 8",
            "IRETURN",
            "MAXS 1 0"
    })
    private static boolean statement() {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        System.out.println(statement() ? "Hello World!" : "Nothing ...");
    }
}
