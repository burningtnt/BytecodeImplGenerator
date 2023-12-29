package net.burningtnt.bcigenerator.visitors;

import org.objectweb.asm.*;

public final class BytecodeMethodVerifier extends MethodVisitor {
    public BytecodeMethodVerifier() {
        super(Opcodes.ASM9);
    }

    /* BCII:
       [0] NEW Lnet/burningtnt/bcigenerator/api/BytecodeImplError;
       [1] DUP
       [2] INVOKESPECIAL Lnet/burningtnt/bcigenerator/api/BytecodeImplError;<init>()V
       [3] ATHROW
       [4] MAXS 2 0
     */

    private int pc = 0;

    private void require(String command, int index) throws IllegalStateException {
        throw new IllegalStateException(String.format("The method implemented by Bytecode Implementation Generator must take operation \"%s\" at %d.", command, index));
    }

    private void no(String command) throws IllegalStateException {
        throw new IllegalStateException(String.format("The method implemented by Bytecode Implementation Generator should not contain any operation matched \"%s *\".", command));
    }

    private void no(int opcode) throws IllegalStateException {
        no('#' + Integer.toString(opcode));
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        no("FRAME");
    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
            case Opcodes.DUP: {
                if (this.pc != 1) {
                    require("DUP", 1);
                }
                break;
            }
            case Opcodes.ATHROW: {
                if (this.pc != 3) {
                    require("ATHROW", 3);
                }
                break;
            }
        }
        this.pc ++;
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        no(opcode);
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        no(opcode);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (this.pc != 0 || opcode != Opcodes.NEW) {
            require("NEW Lnet/burningtnt/bcigenerator/api/BytecodeImplError;", 0);
        }
        this.pc ++;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        no(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (this.pc != 2 || opcode != Opcodes.INVOKESPECIAL || !"net/burningtnt/bcigenerator/api/BytecodeImplError".equals(owner) || !"<init>".equals(name) || !"()V".equals(descriptor) || isInterface) {
            require("INVOKESPECIAL Lnet/burningtnt/bcigenerator/api/BytecodeImplError;<init>()V", 2);
        }
        this.pc ++;
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        no("INVOKEDYNAMIC");
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        no(opcode);
    }

    @Override
    public void visitLabel(Label label) {
    }

    @Override
    public void visitLdcInsn(Object value) {
        no("LDC");
    }

    @Override
    public void visitIincInsn(int varIndex, int increment) {
        no("IINC");
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        no("TABLESWITCH");
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        no("LOOKUPSWITCH");
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        no("MULTIANEWARRAY");
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return null;
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        no("TRYCATCH");
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        no("TRYCATCHANNOTATION");
        throw new AssertionError();
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        no("LOCALVARIABLEANNOTAION");
        throw new AssertionError();
    }

    @Override
    public void visitLineNumber(int line, Label start) {
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (this.pc != 4 || maxStack != 2) {
            require("MAXS 2 *", 4);
        }
        this.pc ++;
    }

    @Override
    public void visitEnd() {
    }
}
