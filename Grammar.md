# Structure
A bytecode implemented method should be something like this:

```java
@BytecodeImpl({
        "LABEL METHOD_HEAD",
        "GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;",
        "LDC (STRING \"Hello BCIG!\")",
        "INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V",
        "RETURN",
        "LABEL METHOD_TAIL",
        "MAXS 2 0"
})
private static void sayHelloBCIG() {
    throw new BytecodeImplError();
}
```

The method must be annotated by `@BytecodeImpl`, and must throw a newly constructed `BytecodeImplError`
as BytecodeImplGenerator may check this in the future versions.

The string array in `@BytecodeImpl` is called BCII, which stands for Bytecode Implementation Instructions.
This will be introduced later.

The method should be declared correctly, just like BCIG doesn't exist. Examples: if you like to declare a method
with parameter(s) and a return value, you can code like this.
```java
@BytecodeImpl({
        "LABEL METHOD_HEAD",
        "GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;",
        "ALOAD 0",
        "INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V",
        "LDC (STRING \"Return String\")",
        "LABEL RELEASE_PARAMETER",
        "ARETURN",
        "LOCALVARIABLE text [java/lang/String; METHOD_HEAD RELEASE_PARAMETER 0",
        "MAXS 2 1"
})
private static String sayString(String text) {
    throw new BytecodeImplError();
}
```

# BCII: Bytecode Implementation Instruction
A BCII should be a string, which stands for Bytecode Implementation Instruction.

For a BCII, it should consist of an operator, a space (if parameters is required by this operator) and parameter(s).
Each parameter must be separated by a space as well.
These are legal BCII.

- `ALOAD 0`: `ALOAD` is the operator. `0` is the parameter.
- `INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V`: `INVOKEVIRTUAL` is the operaotr. `Ljava/io/PrintStream;println(Ljava/lang/String;)V` is the parameter.
- `LOCALVARIABLE text [java/lang/String; METHOD_HEAD RELEASE_PARAMETER 0`:
  `LOCALVARIABLE` is the operator. `text`, `[java/lang/String;` and `METHOD_HEAD RELEASE_PARAMETER 0`

Each BCII describes a bytecode, or a part of the method info.

## Bytecode
For BCIIs that describes a bytecode, they are cataloged base on the way that ASM declares them.

## visitInsn
The operator is directly the name declared in `Opcodes`.

The parameter is not needed.

Examples: `DUP`, `ARETURN`, `MONITORENTER`

## visitIntInsn
The operator is directly the name declared in `Opcodes`.

The parameter should be the name declared in `Opcodes` without "T_" prefix, or directly the value, which depends on the operator.

Examples: `NEWARRAY BOOLEAN`, `BIPUSH 10` and `SIPUSH 800`

## visitVarInsn
The operator is directly the name declared in `Opcodes`.

The parameter should be an int value which stands for the index of a local variable.

Examples: `ILOAD 0`, `ALOAD 8`

## visitTypeInsn
TODO

## visitFieldInsn
The operator is directly the name declared in `Opcodes`.

The parameter should be a VM field method descriptor.

Examples: `GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;`, `GETFIELD Lorg/objectweb/asm/MethodVisitor;api:I`

## visitMethodInsn
The operator is directly the name declared in `Opcodes`, with a new operator `INVOKEINTERFACESTATIC`, which stands for invoking a static mehod in an interface.

The parameter should be a VM method descriptor.

Examples: `INVOKESTATIC Ljava/lang/System;getProperty(Ljava/lang/String;)Ljava/lang/String;`, `INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V` and `INVOKEINTERFACE Ljava/lang/Runnable;run()V`

## visitInvokeDynamicInsn
TODO

## visitJumpInsn
The operator is directly the name declared in `Opcodes`.

The parameter should be a Label ID.

Examples: `GOTO TARGET_LABEL`, `IFNULL targetLabel`

## visitLabel
The operator should always be `LABEL`.

The parameter should be a Label ID.

Examples: `LABEL METHOD_BEGIN`, `LABEL targetLabel2`

## visitLdcInsn
This depends on the constant type.

### String
The operator should always be `LDCString`.

The first parameter should always be `STRING`.
The next parameter should be a string placed in an apostrophe.

Examples: `LDC (STRING \"Hello World!\")` which stands for push `Hello World!` into the JVM runtime stack, is a legal BCII.

### TODO

## visitLdcInsn
TODO

## visitTableSwitchInsn
TODO

## visitLookupSwitchInsn
TODO

## visitMultiANewArrayInsn
TODO

## visitTryCatchBlock
TODO

## visitLineNumber
TODO

# Method Info

## LocalVariable
The operator should always be `LOCALVARIABLE`.

The parameter should be the name, the VM descriptor of the type, the start Label ID, the end Label ID and the index of this local variable.

Examples: `LOCALVARIABLE more [C START_LABEL END_LABEL 0`

## Maxs
The operator should always be `MAXS`.

The parameter should be the max stack number and the max local number.

Examples: MAXS 2 1