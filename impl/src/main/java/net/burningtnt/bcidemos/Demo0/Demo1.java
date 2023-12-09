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
package net.burningtnt.bcidemos.Demo0;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

import java.nio.file.Path;

public final class Demo1 {
    private Demo1() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",
            "ALOAD 0",
            "ALOAD 1",
            "INVOKEINTERFACESTATIC Ljava/nio/file/Path;of(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;",
            "LABEL METHOD_TAIL",
            "ARETURN",
            "LOCALVARIABLE first Ljava/lang/String; METHOD_HEAD METHOD_TAIL 0",
            "LOCALVARIABLE further [Ljava/lang/String; METHOD_HEAD METHOD_TAIL 1",
            "MAXS 2 2"
    })
    private static Path getPath(String first, String... further) {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        System.out.println(getPath("First", "Second", "Third").toString());
    }
}
