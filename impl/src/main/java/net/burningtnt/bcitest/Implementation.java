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
package net.burningtnt.bcitest;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Implementation {
    private Implementation() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",
            "NEW java/lang/StringBuilder",
            "DUP",
            "INVOKESPECIAL Ljava/lang/StringBuilder;<init>()V",
            "ALOAD 0",
            "INVOKEVIRTUAL Ljava/lang/StringBuilder;append([C)Ljava/lang/StringBuilder;",
            "LABEL RELEASE_PARAMETER",
            "ARETURN",
            "LOCALVARIABLE more [C METHOD_HEAD RELEASE_PARAMETER 0",
            "MAXS 2 1"
    })
    private static StringBuilder doAppend(char[] more) {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        System.out.println(doAppend("[TEXT]".toCharArray()).toString());
    }
}
