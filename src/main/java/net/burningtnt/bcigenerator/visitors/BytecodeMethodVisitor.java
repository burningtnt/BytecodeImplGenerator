package net.burningtnt.bcigenerator.visitors;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.insn.IInsn;
import net.burningtnt.bcigenerator.insn.holders.StructureHolder;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public final class BytecodeMethodVisitor extends MethodVisitor {
    private static final String TARGET_DESC = "L" + BytecodeImpl.class.getName().replace('.', '/') + ";";

    private final MethodVisitor raw;

    public BytecodeMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
        this.raw = methodVisitor;
    }

    private final List<IInsn> insnList = new ArrayList<>();

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (TARGET_DESC.equals(descriptor)) {
            this.mv = null;
            return new BytecodeImplAnnotationVisitor(insnList);
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode() {
        if (this.mv == null) {
            for (IInsn insn : this.insnList) {
                insn.write(this.raw);
            }
            StructureHolder.verify();
        } else {
            super.visitCode();
        }
    }
}
