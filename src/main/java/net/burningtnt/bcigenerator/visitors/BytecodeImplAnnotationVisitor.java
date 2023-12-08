package net.burningtnt.bcigenerator.visitors;

import net.burningtnt.bcigenerator.insn.IInsn;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public final class BytecodeImplAnnotationVisitor extends AnnotationVisitor {
    private final List<IInsn> insnList;

    public BytecodeImplAnnotationVisitor(List<IInsn> insnList) {
        super(Opcodes.ASM9, null);
        this.insnList = insnList;
    }

    @Override
    public void visit(String name, Object value) {
        throw new IllegalStateException("BytecodeImpl annotation shouldn't contain any data for AnnotationVisitor::visit.");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        throw new IllegalStateException("BytecodeImpl annotation shouldn't contain any data for AnnotationVisitor::visitAnnotation.");
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        throw new IllegalStateException("BytecodeImpl annotation shouldn't contain any data for AnnotationVisitor::visitEnum.");
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        if ("value".equals(name)) {
            return new BytecodeInsnAnnotationVistor(this.insnList);
        }
        throw new IllegalStateException("BytecodeImpl annotation should only contain \"value\" key for AnnotationVisitor::visitArray.");
    }
}
