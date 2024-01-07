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
package net.burningtnt.bcidemos;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Demo0 {
    private Demo0() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",
            "GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;",
            "LDC (STRING \"Hello BCIG!\")",
            "INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V",
            "LABEL METHOD_TAIL",
            "RETURN",
            "MAXS 2 0"
    })
    private static void method() {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        method();
    }
}
