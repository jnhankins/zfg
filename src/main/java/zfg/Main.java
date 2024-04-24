package zfg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import zfg.old.ast.Parser;

public class Main {
  public static void main(final String[] args) {
    System.out.println("main: " + Arrays.toString(args));

    final Path pwd = Paths.get(System.getProperty("user.dir"));
    System.out.println("pwd: " + pwd);

    for (final String arg : args) {
      if (!arg.endsWith(".zfg")) {
        System.err.println("Invalid file suffix: " + arg);
        continue;
      }
      final Path source = pwd.resolve(arg);
      final Path target = pwd.resolve(arg.substring(0, arg.length() - 4) + ".class");
      compile(source, target);
    }
  }

  public static void compile(final Path sourcePath, final Path targetPath) {
    System.out.println("compile: " + sourcePath + " -> " + targetPath);

    // TODO
    Parser.parse(sourcePath);

    // TODO Generate JVM bytecode from ZFG AST
    final byte[] bytecode = generateBytecode();
    writeFile(targetPath, bytecode);
  }

  final static byte[] generateBytecode() {
    final ClassWriter cw = new ClassWriter(0);

    // public class Main {
    cw.visit(Opcodes.V21, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);

    // public static void main(String[] args) {
    final MethodVisitor mv = cw.visitMethod(
        Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);

    // System.out.println("Hello, world!");
    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn("Hello, world!");
    mv.visitMethodInsn(
        Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

    // return;
    mv.visitInsn(Opcodes.RETURN);

    // }
    mv.visitMaxs(0, 0);
    mv.visitEnd();

    // }
    cw.visitEnd();

    return cw.toByteArray();
  }

  final static CharStream readFile(final Path path) {
    try {
      return CharStreams.fromPath(path);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  final static void writeFile(final Path path, final byte[] content) {
    try {
      Files.write(path, content);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
