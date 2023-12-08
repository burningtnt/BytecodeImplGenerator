package net.burningtnt.bcigenerator.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class BytecodeImplClassVisitor extends ClassVisitor {
    public BytecodeImplClassVisitor(ClassWriter delegate) {
        super(Opcodes.ASM9, delegate);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new BytecodeMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
    }
}
