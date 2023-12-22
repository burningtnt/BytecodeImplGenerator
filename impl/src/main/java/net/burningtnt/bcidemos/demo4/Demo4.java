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
package net.burningtnt.bcidemos.demo4;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class Demo4 {
    private Demo4() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",

            "INVOKEDYNAMIC Lnet/burningtnt/bcidemos/demo4/Demo4;lambdaFunction()V Lnet/burningtnt/bcidemos/demo4/Demo4;bootstrap(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",

            "LABEL METHOD_TAIL",
            "RETURN",
            "MAXS 1 1"
    })
    private static void invokeDynamic() {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        invokeDynamic();
    }

    private static void hello() {
        System.out.println("Hello!");
    }

    public static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type) throws Throwable {
        return new ConstantCallSite(lookup.findStatic(Demo4.class, "hello", MethodType.methodType(void.class)));
    }
}
