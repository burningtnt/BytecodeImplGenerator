package net.burningtnt.bcigenerator.visitors;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.burningtnt.bcigenerator.insn.IInsn;
import net.burningtnt.bcigenerator.insn.Parser;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class BytecodeInsnAnnotationVistor extends AnnotationVisitor {
    private final List<IInsn> insnList;

    public BytecodeInsnAnnotationVistor(List<IInsn> insnList) {
        super(Opcodes.ASM9);
        this.insnList = insnList;
    }

    @Override
    public void visit(String name, Object value) {
        if ((name == null || "value".equals(name)) && value instanceof String) {
            try {
                this.insnList.add(Parser.parse((String) value));
            } catch (CommandSyntaxException e) {
                throw new IllegalStateException(String.format("Illegal command: %s.", value), e);
            }
        } else {
            throw new IllegalStateException("BytecodeInsn annotation shouldn't contain any data except \"value\" with argument type String[] for AnnotationVisitor::visit.");
        }
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        throw new IllegalStateException("BytecodeInsn annotation shouldn't contain any data for AnnotationVisitor::visitArray.");
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        throw new IllegalStateException("BytecodeInsn annotation shouldn't contain any data for AnnotationVisitor::visitEnum.");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        throw new IllegalStateException("BytecodeInsn annotation shouldn't contain any data for AnnotationVisitor::visitAnnotation.");
    }
}
