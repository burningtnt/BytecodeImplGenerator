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
package net.burningtnt.bcigenerator.visitors;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.insn.IInsn;
import net.burningtnt.bcigenerator.insn.holders.ControlFlowHolder;
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
            ControlFlowHolder.verify();
        } else {
            super.visitCode();
        }
    }
}
