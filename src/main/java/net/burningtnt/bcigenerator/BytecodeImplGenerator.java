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
package net.burningtnt.bcigenerator;

import net.burningtnt.bcigenerator.visitors.BytecodeImplClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public final class BytecodeImplGenerator {
    private BytecodeImplGenerator() {
    }

    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            Path path = Paths.get(arg);
            if (Files.isDirectory(path)) {
                try (Stream<Path> stream = Files.walk(path)) {
                    Iterator<Path> iterator = stream.iterator();
                    while (iterator.hasNext()) {
                        Path current = iterator.next();
                        if (Files.isRegularFile(current)) {
                            process(current);
                        }
                    }
                }
            } else if (Files.isRegularFile(path)) {
                process(path);
            } else {
                throw new IllegalStateException("Unknown path type: " + path + '.');
            }
        }
    }

    public static void process(Path path) throws IOException {
        Files.write(path, process(Files.readAllBytes(path)));
    }

    public static byte[] process(byte[] input) {
        ClassReader classReader = new ClassReader(input);
        ClassWriter classWriter = new ClassWriter(0) {
        };

        classReader.accept(new BytecodeImplClassVisitor(classWriter), 0);

        return classWriter.toByteArray();
    }
}
