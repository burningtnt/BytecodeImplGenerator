# Projects
All the source codes can be found in the ./impl/src/main/java/net/burningtnt/bcidemos directory.

## #0: Hello World
In this project, we will make a program that prints "Hello BCIG!" on the console.

In the beginning, let's make the structure.

```java
package net.burningtnt.bcidemos.demo0;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

public final class Demo0 {
    private Demo0() {
    }

    @BytecodeImpl({
            // BCIIs
    })
    private static void method() {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        method();
    }
}
```

In general, our BCIIs should be coded in following steps:
1. Load the static field of `java.lang.System#out` into the JVM runtime stack.
2. Load a String type constant into the JVM runtime stack.
3. Invoke the virtual method of `java.io.PrintStream::println(java.lang.String)`, which consumes the upper objects.
4. Return our bytecode implemented method.

So, these are the BCIIs.
```BCII
GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;
LDC (STRING "Hello BCIG!")
INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V
RETURN
```

In order to make this method legal in VM Class Format, we should insert these BCIIs.
```diff
+LABEL METHOD_HEAD
GETSTATIC Ljava/lang/System;out:Ljava/io/PrintStream;
LDC (STRING "Hello BCIG!")
INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V
+LABEL METHOD_TAIL
RETURN
+MAXS 2 0 
```
- `LABEL METHOD_HEAD`: The start label of this method.
- `LABEL METHOD_TAIL`: The tail label of this method.
- `MAXS 2 0`: Declare the max stack number and max local number.

What many people can't understand is how the max stacks and max locals are calculated.
Here, let's imagine how JVM run our bytecodes. When JVM run till line `INVOKEVIRTUAL Ljava/io/PrintStream;println(Ljava/lang/String;)V`, 
there are two element in the JVM runtime stack: `Ljava/io/PrintStream` and `Ljava/lang/String`. Both of the two element required 1 stack space.
So the max stack number is 2.

In this method, no local variable is required. So the max local number is 0.

## #1: Java Version Compatibility
In this project, we will create a method that invokes `java.nio.file.Path::of(java.lang.String, java.lang.String...)` with bytecode.

In the beginning, let's make the structure.

```java
package net.burningtnt.bcidemos.demo0;

import net.burningtnt.bcigenerator.api.BytecodeImpl;
import net.burningtnt.bcigenerator.api.BytecodeImplError;

import java.nio.file.Path;

public final class Demo1 {
    private Demo1() {
    }

    @BytecodeImpl({
            // BCIIs
    })
    private static Path getPath(String first, String... further) {
        throw new BytecodeImplError();
    }

    public static void main(String[] args) {
        System.out.println(getPath("First", "Second", "Third").toString());
    }
}
```

Here we declared a method called `getPath` with two parameters and a return value.

In general, our BCIIs should be coded in following steps:
1. Load the first parameter into the JVM runtime stack.
2. Load the second parameter into the JVM runtime stack.
3. Invoke the static method of `java.nio.file.Path::of(java.lang.String, java.lang.String...)`, which consumes the upper objects and return a `java.nio.file.Path` object.
4. Return our bytecode implemented method with an object.

So, these are the BCIIs.
```BCII
ALOAD 0
ALOAD 1
INVOKEINTERFACESTATIC Ljava/nio/file/Path;of(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
ARETURN
```
Note that here we invoked a static method in an interface, so we need to use the `INVOKEINTERFACESTATIC` operator.

In order to make this method legal in VM Class Format, we should insert these BCIIs.
```diff
+LABEL METHOD_HEAD
ALOAD 0
ALOAD 1
INVOKEINTERFACESTATIC Ljava/nio/file/Path;of(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
+LABEL METHOD_TAIL
ARETURN
+LOCALVARIABLE first Ljava/lang/String; METHOD_HEAD METHOD_TAIL 0
+LOCALVARIABLE further [Ljava/lang/String; METHOD_HEAD METHOD_TAIL 1
+MAXS 2 2
```

Here, we have two local variable. So, we need to declare them in the BCII.
LOCALVARIABLE operator should be after codes, and MAXS operator should always in the last.

Notice: The Label ID in operators must be defined by using `LABEL XXX` before or after. Which means, You can use `GOTO XXX`
before this Label ID is really defined.