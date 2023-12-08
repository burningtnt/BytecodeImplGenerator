package net.burningtnt.bcigenerator;

import net.burningtnt.bcigenerator.visitors.BytecodeImplClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class BytecodeImplGenerator {
    private BytecodeImplGenerator() {
    }

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            Path path = Path.of(arg);
            Files.write(path, process(Files.readAllBytes(path)));
        }
    }

    public static byte[] process(byte[] input) {
        ClassReader classReader = new ClassReader(input);
        ClassWriter classWriter = new ClassWriter(0) {
        };

        classReader.accept(new BytecodeImplClassVisitor(classWriter), 0);

        return classWriter.toByteArray();
    }
}
