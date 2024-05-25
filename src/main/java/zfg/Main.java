package zfg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

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

    Compiler.parse(sourcePath);
    // TODO
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
