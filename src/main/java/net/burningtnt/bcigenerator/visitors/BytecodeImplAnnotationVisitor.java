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
