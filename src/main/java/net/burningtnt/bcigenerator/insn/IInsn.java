package net.burningtnt.bcigenerator.insn;

import org.objectweb.asm.MethodVisitor;

public interface IInsn {
    void write(MethodVisitor mv);
}
