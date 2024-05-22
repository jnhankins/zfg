// package zfg;

// import java.util.Arrays;
// import java.util.Objects;

// public final class Path {
//   public final String[] modulePath;
//   public final String symbolName;
//   public final Segment[] fieldPath;
//   public Path(final String[] modulePath, final String symbolName, final Segment[] fieldPath) {
//     assert modulePath != null;
//     assert Arrays.stream(modulePath).allMatch(Names::isUpperCamelCase);
//     assert symbolName != null;
//     assert Names.isLowerSnakeCase(symbolName) || Names.isUpperCamelCase(symbolName);
//     assert fieldPath != null;
//     assert fieldPath.length > 0;
//     assert Arrays.stream(fieldPath).noneMatch(Objects::isNull);
//     this.modulePath = modulePath;
//     this.symbolName = symbolName;
//     this.fieldPath = fieldPath;
//   }
//   public String qualifiedName() {
//     if (modulePath == null || modulePath.length == 0) return symbolName;
//     final StringBuilder sb = new StringBuilder();
//     for (int i = 0; i < modulePath.length; i++) {
//       sb.append(modulePath[i]);
//       sb.append("::");
//     }
//     sb.append(symbolName);
//     return sb.toString();
//   }
//   public static sealed interface Segment {
//     public static final class Named implements Segment {
//       public final String named;
//       public Named(final String named) {
//         assert named != null;
//         assert Names.isLowerSnakeCase(named) || Names.isUpperCamelCase(named);
//         this.named = named;
//       }
//     }
//     public static final class Index implements Segment {
//       public final Ast2.Expr index;
//       public Index(final Ast2.Expr index) {
//         assert index != null;
//         this.index = index;
//       }
//     }
//   }
// }
