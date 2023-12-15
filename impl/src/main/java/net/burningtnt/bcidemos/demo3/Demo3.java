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
package net.burningtnt.bcidemos.demo3;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Demo3 {
    private Demo3() {
    }

    @BytecodeImpl({
            "LABEL METHOD_HEAD",
            "ILOAD 0",
            "LOOKUPSWITCH { 0 SWITCH_0 | 5 SWITCH_5 | 8 SWITCH_8 } SWITCH_DEFAULT",

            "LABEL SWITCH_0",
            "FRAME SAME",
            "SIPUSH 200",
            "IRETURN",

            "LABEL SWITCH_5",
            "FRAME SAME",
            "SIPUSH 205",
            "IRETURN",

            "LABEL SWITCH_8",
            "FRAME SAME",
            "SIPUSH 208",
            "IRETURN",

            "LABEL SWITCH_DEFAULT",
            "FRAME SAME",
            "SIPUSH 404",
            "IRETURN",

            "LABEL END_SWITCH",
            "LOCALVARIABLE value I METHOD_HEAD END_SWITCH 0",
            "MAXS 1 1"
    })
    private static int doLookupSwitch(int value) {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        System.out.println(doLookupSwitch(0));
        System.out.println(doLookupSwitch(5));
        System.out.println(doLookupSwitch(8));
        System.out.println(doLookupSwitch(16));
    }
}
