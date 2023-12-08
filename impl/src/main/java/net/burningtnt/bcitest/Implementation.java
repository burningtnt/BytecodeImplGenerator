package net.burningtnt.bcitest;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Implementation {
    private Implementation() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",
            "GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;",
            "LDC \"Hello World!\"",
            "INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V",
            "RETURN",
            "LABEL METHOD_TAIL",
            "MAXS 2 0"
    })
    private static void sayHelloWorld() {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        sayHelloWorld();
    }
}
