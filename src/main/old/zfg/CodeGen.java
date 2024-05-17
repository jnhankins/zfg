
package zfg;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import zfg.insts.I08Inst;
import zfg.insts.I16Inst;
import zfg.insts.I32Inst;
import zfg.insts.U08Inst;
import zfg.insts.U16Inst;
import zfg.insts.U32Inst;
import zfg.nodes.AssignExpr;
import zfg.nodes.BinaryExpr;
import zfg.nodes.CastExpr;
import zfg.nodes.CompareExpr;
import zfg.nodes.ConstantExpr;
import zfg.nodes.Expr;
import zfg.nodes.FunCallExpr;
import zfg.nodes.FunRef;
import zfg.nodes.NaryExpr;
import zfg.nodes.Node;
import zfg.nodes.Stmt;
import zfg.nodes.UnaryExpr;
import zfg.nodes.VarLoadExpr;
import zfg.nodes.VarRef;
import zfg.types.NomType;
import zfg.types.Type;

public final class CodeGen {
  private ClassWriter cv;
  private MethodVisitor mv;

  public CodeGen() {
    this.cv = new ClassWriter(ClassWriter.COMPUTE_MAXS & ClassWriter.COMPUTE_FRAMES);
    this.mv = null;
  }

  public void visit(final Node node) {
    switch (node) {
      case Expr n -> visitExpr(n);
      case Stmt n -> visitStmt(n);
      default -> throw new AssertionError();
    }
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Expressions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void visitExpr(final Expr expr) {
    switch (expr) {
      case ConstantExpr n -> visitConstantExpr(n);
      case VarLoadExpr  n -> visitVarLoadExpr(n);
      case FunCallExpr  n -> visitFunCallExpr(n);
      case CastExpr     n -> visitCastExpr(n);
      case UnaryExpr    n -> visitUnaryExpr(n);
      case BinaryExpr   n -> visitBinaryExpr(n);
      case NaryExpr     n -> visitNaryExpr(n);
      case CompareExpr  n -> visitCompareExpr(n);
      case AssignExpr   n -> visitAssignExpr(n);
    }
  }

  private void visitConstantExpr(final ConstantExpr expr) {
    switch (expr.val) {
      case insts.BitInst inst -> visitI(inst.value);
      case insts.U08Inst inst -> visitI(inst.value);
      case insts.U16Inst inst -> visitI(inst.value);
      case insts.U32Inst inst -> visitI(inst.value);
      case insts.U64Inst inst -> visitL(inst.value);
      case insts.I08Inst inst -> visitI(inst.value);
      case insts.I16Inst inst -> visitI(inst.value);
      case insts.I32Inst inst -> visitI(inst.value);
      case insts.I64Inst inst -> visitL(inst.value);
      case insts.F32Inst inst -> visitF(inst.value);
      case insts.F64Inst inst -> visitD(inst.value);
      // TODO arr, tup, rec, fun, etc.
    };
  }

  private void visitVarLoadExpr(final VarLoadExpr expr) {
    visitVarLoad(expr.var);
  }

  private void visitFunCallExpr(final FunCallExpr expr) {
    // Invoke a function using one of:
    // - invokestatic <indexbyte1> <indexbyte2>
    // - invokevirtual <indexbyte1> <indexbyte2>
    // - invokespecial <indexbyte1> <indexbyte2>
    // - invokeinterface <indexbyte1> <indexbyte2>
    // - invokedynamic <indexbyte1> <indexbyte2>
    for (int i = 0; i < expr.args.length; i++) visitExpr(expr.args[i]);
    final FunRef fun = expr.fun;
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, fun.owner, fun.name, fun.type.descriptor(), false);
  }

  private void visitCastExpr(final CastExpr expr) {
    visitExpr(expr.opd);
    final Type from = expr.opd.type();
    final Type into = expr.type;
    throw new UnsupportedOperationException("TODO"); // TODO
  }

  private void visitUnaryExpr(final UnaryExpr expr) {
    switch (expr.opr) {
      case UnaryExpr.Opr.INC -> {
        visitExpr(expr.opd);
        switch (expr.type().kind()) {
          case U08 -> { visitI(1); mv.visitInsn(Opcodes.IADD); visit_i2u08(); }
          case U16 -> { visitI(1); mv.visitInsn(Opcodes.IADD); visit_i2u16(); }
          case U32 -> { visitI(1); mv.visitInsn(Opcodes.IADD); }
          case U64 -> { visitI(1); mv.visitInsn(Opcodes.LADD); }
          case I08 -> { visitI(1); mv.visitInsn(Opcodes.IADD); visit_i2i08(); }
          case I16 -> { visitI(1); mv.visitInsn(Opcodes.IADD); visit_i2i16(); }
          case I32 -> { visitI(1); mv.visitInsn(Opcodes.IADD); }
          case I64 -> { visitL(1); mv.visitInsn(Opcodes.LADD); }
          case F32 -> { visitF(1); mv.visitInsn(Opcodes.FADD); }
          case F64 -> { visitD(1); mv.visitInsn(Opcodes.DADD); }
          default -> throw new AssertionError();
        }
      }
      case UnaryExpr.Opr.DEC -> {
        visitExpr(expr.opd);
        switch (expr.type().kind()) {
          case U08 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); visit_i2u08(); }
          case U16 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); visit_i2u16(); }
          case U32 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); }
          case U64 -> { visitL(1); mv.visitInsn(Opcodes.LSUB); }
          case I08 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); visit_i2i08(); }
          case I16 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); visit_i2i16(); }
          case I32 -> { visitI(1); mv.visitInsn(Opcodes.ISUB); }
          case I64 -> { visitL(1); mv.visitInsn(Opcodes.LSUB); }
          case F32 -> { visitF(1); mv.visitInsn(Opcodes.FSUB); }
          case F64 -> { visitD(1); mv.visitInsn(Opcodes.DSUB); }
          default -> throw new AssertionError();
        }
      }
      case UnaryExpr.Opr.NEG -> {
        visitExpr(expr.opd);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.INEG); visit_i2u08(); }
          case U16 -> { mv.visitInsn(Opcodes.INEG); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.INEG); }
          case U64 -> { mv.visitInsn(Opcodes.LNEG); }
          case I08 -> { mv.visitInsn(Opcodes.INEG); visit_i2i08(); }
          case I16 -> { mv.visitInsn(Opcodes.INEG); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.INEG); }
          case I64 -> { mv.visitInsn(Opcodes.LNEG); }
          case F32 -> { mv.visitInsn(Opcodes.FNEG); }
          default -> throw new AssertionError();
        }
      }
      case UnaryExpr.Opr.NOT -> {
        visitExpr(expr.opd);
        switch (expr.type().kind()) {
          case U08 -> { visitI(0x000000FF); mv.visitInsn(Opcodes.IXOR); }
          case U16 -> { visitI(0x0000FFFF); mv.visitInsn(Opcodes.IXOR); }
          case U32 -> { visitI(0xFFFFFFFF); mv.visitInsn(Opcodes.IXOR); }
          case U64 -> { visitL(0xFFFFFFFFFFFFFFFFL); mv.visitInsn(Opcodes.LXOR); }
          case I08 -> { visitI(0x000000FF); mv.visitInsn(Opcodes.IXOR); }
          case I16 -> { visitI(0x0000FFFF); mv.visitInsn(Opcodes.IXOR); }
          case I32 -> { visitI(0xFFFFFFFF); mv.visitInsn(Opcodes.IXOR); }
          case I64 -> { visitL(0xFFFFFFFFFFFFFFFFL); mv.visitInsn(Opcodes.LXOR); }
          default -> throw new AssertionError();
        }
      }
      case UnaryExpr.Opr.LNT -> {
        visitExpr(expr.opd);
        switch (expr.type().kind()) {
          case BIT -> { visitI(0x00000001); mv.visitInsn(Opcodes.IXOR); }
          default -> throw new AssertionError();
        }
      }
    }
  }

  private void visitBinaryExpr(final BinaryExpr expr) {
    switch (expr.opr) {
      case BinaryExpr.Opr.ADD -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.IADD); visit_i2u08(); }
          case U16 -> { mv.visitInsn(Opcodes.IADD); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.IADD); }
          case U64 -> { mv.visitInsn(Opcodes.LADD); }
          case I08 -> { mv.visitInsn(Opcodes.IADD); visit_i2i08(); }
          case I16 -> { mv.visitInsn(Opcodes.IADD); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.IADD); }
          case I64 -> { mv.visitInsn(Opcodes.LADD); }
          case F32 -> { mv.visitInsn(Opcodes.FADD); }
          case F64 -> { mv.visitInsn(Opcodes.DADD); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.SUB -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.ISUB); visit_i2u08(); }
          case U16 -> { mv.visitInsn(Opcodes.ISUB); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.ISUB); }
          case U64 -> { mv.visitInsn(Opcodes.LSUB); }
          case I08 -> { mv.visitInsn(Opcodes.ISUB); visit_i2i08(); }
          case I16 -> { mv.visitInsn(Opcodes.ISUB); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.ISUB); }
          case I64 -> { mv.visitInsn(Opcodes.LSUB); }
          case F32 -> { mv.visitInsn(Opcodes.FSUB); }
          case F64 -> { mv.visitInsn(Opcodes.DSUB); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.MUL -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.IMUL); visit_i2u08(); }
          case U16 -> { mv.visitInsn(Opcodes.IMUL); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.IMUL); }
          case U64 -> { mv.visitInsn(Opcodes.LMUL); }
          case I08 -> { mv.visitInsn(Opcodes.IMUL); visit_i2i08(); }
          case I16 -> { mv.visitInsn(Opcodes.IMUL); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.IMUL); }
          case I64 -> { mv.visitInsn(Opcodes.LMUL); }
          case F32 -> { mv.visitInsn(Opcodes.FMUL); }
          case F64 -> { mv.visitInsn(Opcodes.DMUL); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.DIV -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.IDIV); }
          case U16 -> { mv.visitInsn(Opcodes.IDIV); }
          case U32 -> { visit_divideUnsigned_i(); }
          case U64 -> { visit_divideUnsigned_l(); }
          case I08 -> { mv.visitInsn(Opcodes.IDIV); }
          case I16 -> { mv.visitInsn(Opcodes.IDIV); }
          case I32 -> { mv.visitInsn(Opcodes.IDIV); }
          case I64 -> { mv.visitInsn(Opcodes.LDIV); }
          case F32 -> { mv.visitInsn(Opcodes.FDIV); }
          case F64 -> { mv.visitInsn(Opcodes.DDIV); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.REM -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { mv.visitInsn(Opcodes.IREM); }
          case U16 -> { mv.visitInsn(Opcodes.IREM); }
          case U32 -> { visit_remainderUnsigned_i(); }
          case U64 -> { visit_remainderUnsigned_l(); }
          case I08 -> { mv.visitInsn(Opcodes.IREM); }
          case I16 -> { mv.visitInsn(Opcodes.IREM); }
          case I32 -> { mv.visitInsn(Opcodes.IREM); }
          case I64 -> { mv.visitInsn(Opcodes.LREM); }
          case F32 -> { mv.visitInsn(Opcodes.FREM); }
          case F64 -> { mv.visitInsn(Opcodes.DREM); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.MOD -> {
        switch (expr.type().kind()) {
          case U08 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); mv.visitInsn(Opcodes.IREM); }
          case U16 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); mv.visitInsn(Opcodes.IREM); }
          case U32 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_remainderUnsigned_i(); }
          case U64 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_remainderUnsigned_l(); }
          case I08 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_floorMod_i(); }
          case I16 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_floorMod_i(); }
          case I32 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_floorMod_i(); }
          case I64 -> { visitExpr(expr.lhs); visitExpr(expr.rhs); visit_floorMod_l(); }
          case F32 -> { // a mod b = a - b * floor(a / b)
            visit(expr.lhs);               // Stack -> ..., a
            visit(expr.rhs);               // Stack -> ..., a, b
            mv.visitInsn(Opcodes.DUP2_X2); // Stack -> ..., a, b, a, b
            mv.visitInsn(Opcodes.FDIV);    // Stack -> ..., a, b, a/b
            mv.visitInsn(Opcodes.F2D);     // Stack -> ..., a, b, (double)(a/b)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
            mv.visitInsn(Opcodes.D2F);     // Stack -> ..., a, b, floor(a/b)
            mv.visitInsn(Opcodes.FMUL);    // Stack -> ..., a, b*floor(a/b)
            mv.visitInsn(Opcodes.FSUB);    // Stack -> ..., a-b*floor(a/b)
          }
          case F64 -> { // a mod b = a - b * floor(a / b)
            visit(expr.lhs);               // Stack -> ..., a
            mv.visitInsn(Opcodes.DUP2);    // Stack -> ..., a, a
            visit(expr.rhs);               // Stack -> ..., a, a, b
            mv.visitInsn(Opcodes.DUP2_X2); // Stack -> ..., a, b, a, b
            mv.visitInsn(Opcodes.DDIV);    // Stack -> ..., a, b, a/b
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
            mv.visitInsn(Opcodes.DMUL);    // Stack -> ..., a, b*floor(a/b)
            mv.visitInsn(Opcodes.DSUB);    // Stack -> ..., a-b*floor(a/b)
          }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.SHL -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { visitI(0b0111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHL); visit_i2u08(); }
          case U16 -> { visitI(0b1111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHL); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.ISHL); }
          case U64 -> { mv.visitInsn(Opcodes.LSHL); }
          case I08 -> { visitI(0b0111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHL); visit_i2i08(); }
          case I16 -> { visitI(0b1111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHL); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.ISHL); }
          case I64 -> { mv.visitInsn(Opcodes.LSHL); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.SHR -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case U08 -> { visitI(0b0111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHR); }
          case U16 -> { visitI(0b1111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHR); }
          case U32 -> { mv.visitInsn(Opcodes.ISHR); }
          case U64 -> { mv.visitInsn(Opcodes.LSHR); }
          case I08 -> { visitI(0b0111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHR); }
          case I16 -> { visitI(0b1111); mv.visitInsn(Opcodes.IAND); mv.visitInsn(Opcodes.ISHR); }
          case I32 -> { mv.visitInsn(Opcodes.ISHR); }
          case I64 -> { mv.visitInsn(Opcodes.LSHR); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExpr.Opr.TWC -> {
        visitExpr(expr.lhs);
        visitExpr(expr.rhs);
        switch (expr.type().kind()) {
          case BIT -> { mv.visitInsn(Opcodes.ISUB); }
          case U08 -> { visit_compare_i(); }
          case U16 -> { visit_compare_i(); }
          case U32 -> { visit_compareUnsigned_i(); }
          case U64 -> { visit_compareUnsigned_l(); }
          case I08 -> { visit_compare_i(); }
          case I16 -> { visit_compare_i(); }
          case I32 -> { visit_compare_i(); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPL); } // NaN <=> 3.1 => -1
          case F64 -> { mv.visitInsn(Opcodes.DCMPL); } // NaN <=> NaN => -1
          default -> throw new AssertionError();
        }
      }
    }
  }

  private void visitNaryExpr(final NaryExpr expr) {
    switch (expr.opr) {
      case NaryExpr.Opr.AND -> {
        final int opcode = switch (expr.type().kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> Opcodes.IAND;
          case U64, I64 -> Opcodes.LAND;
          default -> throw new AssertionError();
        };
        visitExpr(expr.opds[0]);
        for (int i = 1; i < expr.opds.length; i++) {
          visitExpr(expr.opds[i]);
          mv.visitInsn(opcode);
        }
      }
      case NaryExpr.Opr.IOR -> {
        final int opcode = switch (expr.type().kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> Opcodes.IOR;
          case U64, I64 -> Opcodes.LOR;
          default -> throw new AssertionError();
        };
        visitExpr(expr.opds[0]);
        for (int i = 1; i < expr.opds.length; i++) {
          visitExpr(expr.opds[i]);
          mv.visitInsn(opcode);
        }
      }
      case NaryExpr.Opr.XOR -> {
        final int opcode = switch (expr.type().kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> Opcodes.IXOR;
          case U64, I64 -> Opcodes.LXOR;
          default -> throw new AssertionError();
        };
        visitExpr(expr.opds[0]);
        for (int i = 1; i < expr.opds.length; i++) {
          visitExpr(expr.opds[i]);
          mv.visitInsn(opcode);
        }
      }
      case NaryExpr.Opr.LCJ -> { // a && b && c ...
        final Label val0 = new Label();
        final Label done = new Label();
        for (int i = 0; i < expr.opds.length; i++) {
          // TODO: optimize case where expr is CompareExpr and operands are type I
          // We canse use Opcodes.if_ICMP** instead of Opcodes.IFEQ
          visitExpr(expr.opds[i]);
          mv.visitJumpInsn(Opcodes.IFEQ, val0);
        }
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitJumpInsn(Opcodes.GOTO, done);
        mv.visitLabel(val0);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitLabel(done);
      }
      case NaryExpr.Opr.LDJ -> { // a || b || c ...
        final Label val1 = new Label();
        final Label done = new Label();
        for (int i = 0; i < expr.opds.length; i++) {
          // TODO: optimize case where expr is CompareExpr and operands are type I
          visitExpr(expr.opds[i]);
          mv.visitJumpInsn(Opcodes.IFNE, val1);
        }
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, done);
        mv.visitLabel(val1);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitLabel(done);
      }
    }
  }

  private Type commonType(final Type a, final Type b) {
    // TODO
    return null;
  }
  private void visitCastIfNeeded(final Type into, final Type from) {
    if (!into.equals(from)) visitCast(into, from);
  }

  private void visitCompareExpr(final CompareExpr expr) {
    // "a < b < c < d"
    // a >= b ? goto :zero
    // b >= c ? goto :zero
    // c >= d ? goto :zero
    // iconst_1
    // goto :done
    // :zero
    // iconst_0
    // :done


    final Label zero = new Label();
    final Label done = new Label();

    final int cmpCount = expr.oprs.length;
    for (int i = 0; i < cmpCount; i++) {
      final CompareExpr.Opr opr = expr.oprs[i];
      final Expr lhs = expr.opds[i];
      final Expr rhs = expr.opds[i + 1];
      final Type cmp = commonType(lhs.type(), rhs.type());
      visitExpr(lhs);
      visitCastIfNeeded(cmp, lhs.type());
      visitExpr(rhs);
      visitCastIfNeeded(cmp, rhs.type());
      switch (cmp.kind()) {
        case BIT, U08, U16, U32, I08, I16, I32:
          switch (opr) {
            case CompareExpr.Opr.EQL -> mv.visitJumpInsn(Opcodes.IF_ICMPNE, zero);
            case CompareExpr.Opr.NEQ -> mv.visitJumpInsn(Opcodes.IF_ICMPEQ, zero);
            case CompareExpr.Opr.LTN -> mv.visitJumpInsn(Opcodes.IF_ICMPGE, zero);
            case CompareExpr.Opr.LEQ -> mv.visitJumpInsn(Opcodes.IF_ICMPGT, zero);
            case CompareExpr.Opr.GTN -> mv.visitJumpInsn(Opcodes.IF_ICMPLE, zero);
            case CompareExpr.Opr.GEQ -> mv.visitJumpInsn(Opcodes.IF_ICMPLT, zero);
          }
          continue;
        case U64, I64:
          mv.visitInsn(Opcodes.LCMP);
          break;
        case F32:
          mv.visitInsn(Opcodes.FCMPL);
          break;
        case F64:
          mv.visitInsn(Opcodes.DCMPL);
          break;
        case ARR, TUP, REC, FUN:
          switch (opr) {
            case CompareExpr.Opr.EQL -> mv.visitJumpInsn(Opcodes.IF_ACMPNE, zero);
            case CompareExpr.Opr.NEQ -> mv.visitJumpInsn(Opcodes.IF_ACMPEQ, zero);
            default -> throw new AssertionError();
          }
          break;
        case NOM, UNK, ERR: throw new AssertionError()
      }
      switch (opr) {
        case CompareExpr.Opr.EQL -> mv.visitJumpInsn(Opcodes.IFNE, zero);
        case CompareExpr.Opr.NEQ -> mv.visitJumpInsn(Opcodes.IFEQ, zero);
        case CompareExpr.Opr.LTN -> mv.visitJumpInsn(Opcodes.IFGE, zero);
        case CompareExpr.Opr.LEQ -> mv.visitJumpInsn(Opcodes.IFGT, zero);
        case CompareExpr.Opr.GTN -> mv.visitJumpInsn(Opcodes.IFLE, zero);
        case CompareExpr.Opr.GEQ -> mv.visitJumpInsn(Opcodes.IFLT, zero);
      }
    }

    mv.visitInsn(Opcodes.ICONST_1);
    mv.visitJumpInsn(Opcodes.GOTO, done);
    mv.visitLabel(zero);
    mv.visitInsn(Opcodes.ICONST_0);
    mv.visitLabel(done);
  }

  private void visitAssignExpr(final AssignExpr expr) {
    switch (expr.mode) {
      case AssignExpr.Mode.SET_GET -> {
        if (visitLocalIncInt(expr)) {
          visitVarLoad(expr.lhs);
        } else {
          visitExpr(expr.rhs);
          switch (expr.type().kind()) {
            case BIT, U08, U16, U32, I08, I16, I32 -> mv.visitInsn(Opcodes.DUP);
            case U64, I64                          -> mv.visitInsn(Opcodes.DUP2);
            case F32                               -> mv.visitInsn(Opcodes.DUP);
            case F64                               -> mv.visitInsn(Opcodes.DUP2);
            case ARR, TUP, REC, FUN                -> mv.visitInsn(Opcodes.DUP);
            case NOM                               -> throw new AssertionError();
            case UNK, ERR                          -> throw new UnsupportedOperationException();
          }
          visitVarStore(expr.lhs);
        }
      }
      case AssignExpr.Mode.GET_SET -> {
        visitVarLoad(expr.lhs);
        if (visitLocalIncInt(expr)) {
          return;
        } else {
          visitExpr(expr.rhs);
          visitVarStore(expr.lhs);
        }
      }
    }
  }

  private void visitVarLoad(final VarRef var) {
    switch (var.site) {
      case VarRef.Site.Static -> {
        mv.visitFieldInsn(Opcodes.GETSTATIC, var.owner, var.name, var.type.descriptor());
      }
      case VarRef.Site.Member -> {
        mv.visitFieldInsn(Opcodes.GETFIELD, var.owner, var.name, var.type.descriptor());
      }
      case VarRef.Site.Local -> {
        Type type = var.type;
        while (type instanceof NomType nom) type = nom.aliasedType;
        switch (type.kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> mv.visitVarInsn(Opcodes.ILOAD, var.index);
          case U64, I64 ->                          mv.visitVarInsn(Opcodes.LLOAD, var.index);
          case F32 ->                               mv.visitVarInsn(Opcodes.FLOAD, var.index);
          case F64 ->                               mv.visitVarInsn(Opcodes.DLOAD, var.index);
          case ARR, TUP, REC, FUN ->                mv.visitVarInsn(Opcodes.ALOAD, var.index);
          case NOM -> throw new AssertionError();
          case UNK, ERR -> throw new UnsupportedOperationException();
        }
      }
    }
  }

  private void visitVarStore(final VarRef var) {
    switch (var.site) {
      case VarRef.Site.Static -> {
        mv.visitFieldInsn(Opcodes.PUTSTATIC, var.owner, var.name, var.type.descriptor());
      }
      case VarRef.Site.Member -> {
        mv.visitFieldInsn(Opcodes.PUTFIELD, var.owner, var.name, var.type.descriptor());
      }
      case VarRef.Site.Local -> {
        Type type = var.type;
        while (type instanceof NomType nom) type = nom.aliasedType;
        switch (type.kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> mv.visitVarInsn(Opcodes.ISTORE, var.index);
          case U64, I64 ->                          mv.visitVarInsn(Opcodes.LSTORE, var.index);
          case F32 ->                               mv.visitVarInsn(Opcodes.FSTORE, var.index);
          case F64 ->                               mv.visitVarInsn(Opcodes.DSTORE, var.index);
          case ARR, TUP, REC, FUN ->                mv.visitVarInsn(Opcodes.ASTORE, var.index);
          case NOM -> throw new AssertionError();
          case UNK, ERR -> throw new UnsupportedOperationException();
        }
      }
    }
  }

  private boolean visitLocalIncInt(final AssignExpr expr) {
    // Must be a local variable
    if (expr.lhs.site != VarRef.Site.Local) return false;
    // Must have integer type
    switch (expr.lhs.type.kind()) {
      case BIT, U08, U16, U32, I08, I16, I32, U64, I64: break;
      default: return false;
    };
    // Check if it's a UnaryExpr
    if (expr.rhs instanceof UnaryExpr unary) {
      // Must be a self-assignment
      if (!(unary.opd instanceof VarLoadExpr var && var.var == expr.lhs)) return false;
      // Must be INC or DEC
      switch (unary.opr) {
        case UnaryExpr.Opr.INC: mv.visitIincInsn(expr.lhs.index, +1); return true;
        case UnaryExpr.Opr.DEC: mv.visitIincInsn(expr.lhs.index, -1); return true;
        default: return false;
      }
    }
    // Check if it's a UnaryExpr
    if (expr.rhs instanceof BinaryExpr binary) {
      // Must be a self-assignment
      if (!(binary.lhs instanceof VarLoadExpr var && var.var == expr.lhs)) return false;
      // Must have a constant rhs
      if (binary.rhs instanceof ConstantExpr c) {
        // Check the amount
        final int amount;
        switch (c.val) {
          case U08Inst inst: amount = inst.value; break;
          case U16Inst inst: amount = inst.value; break;
          case U32Inst inst: amount = inst.value; break;
          case I08Inst inst: amount = inst.value; break;
          case I16Inst inst: amount = inst.value; break;
          case I32Inst inst: amount = inst.value; break;
          default: throw new AssertionError();
        }
        // Must be an amount between -128 and 127
        if (amount < -128 || amount > 127) return false;
        // Must be ADD or SUB
        switch (binary.opr) {
          case BinaryExpr.Opr.ADD: mv.visitIincInsn(expr.lhs.index, +amount); return true;
          case BinaryExpr.Opr.SUB: mv.visitIincInsn(expr.lhs.index, -amount); return true;
          default: return false;
        }
      }
      return false;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////



  // private void visitFunctionDecl(final FunctionDeclNode node) {
  //   assert mv == null;
  //   // Create a new method visitor
  //   mv = cv.visitMethod(
  //     Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL,
  //     node.name,
  //     node.type.descriptor(),
  //     null,
  //     null
  //   );
  //   // Visit the function body
  //   for (int i = 0; i < node.body.length; i++) {
  //     visit(node.body[i]);
  //   }
  //   // Finish the method
  //   mv.visitInsn(Opcodes.RETURN);
  //   mv.visitEnd();
  //   // Reset the method visitor
  //   mv = null;
  // }

  // private void visitReturnStmt(final ReturnStmtNode node) {
  //   if (node.expr != null) visit(node.expr);
  //   mv.visitInsn(Opcodes.RETURN);
  // }

  // private void visitVariableExpr(final VariableExprNode node) {
  //   // Loads a varaible onto the stack using one of:
  //   // - a static class-variable
  //   //   - getstatic <indexbyte1> <indexbyte2>
  //   // - a member class-variable
  //   //   - getfield <indexbyte1> <indexbyte2>
  //   // - a function local variable
  //   //   - iload <indexbyte> | wide iload <indexbyte1> <indexbyte2>
  //   //   - lload <indexbyte> | wide lload <indexbyte1> <indexbyte2>
  //   //   - fload <indexbyte> | wide fload <indexbyte1> <indexbyte2>
  //   //   - dload <indexbyte> | wide dload <indexbyte1> <indexbyte2>
  //   //   - aload <indexbyte> | wide aload <indexbyte1> <indexbyte2>
  //   switch (node.site) {
  //     case VariableExprNode.Site.Static -> {
  //       mv.visitFieldInsn(Opcodes.GETSTATIC, node.owner, node.name, node.type.descriptor());
  //     }
  //     case VariableExprNode.Site.Member -> {
  //       mv.visitFieldInsn(Opcodes.GETFIELD, node.owner, node.name, node.type.descriptor());
  //     }
  //     case VariableExprNode.Site.Local -> {
  //       Type type = node.type;
  //       while (type instanceof NomType nom) type = nom.aliasedType;
  //       switch (node.type.kind()) {
  //         case
  //         case BIT, U08, U16, U32, I08, I16, I32 -> mv.visitVarInsn(Opcodes.ILOAD, node.index);
  //         case U64, I64 ->                          mv.visitVarInsn(Opcodes.LLOAD, node.index);
  //         case F32 ->                               mv.visitVarInsn(Opcodes.FLOAD, node.index);
  //         case F64 ->                               mv.visitVarInsn(Opcodes.DLOAD, node.index);
  //         case ARR, TUP, REC, FUN ->                mv.visitVarInsn(Opcodes.ALOAD, node.index);
  //         case NOM -> throw new AssertionError();
  //         case UNK, ERR -> throw new UnsupportedOperationException();
  //       }
  //     }
  //   }
  // }

  // private void visitBinaryExpr(final BinaryExprNode node) {
  //   mv.visitMethodInsn(0, null, null, null, false);
  //   final Kind kind = node.type.kind();
  //   final Node lhs = node.lhs;
  //   final Node rhs = node.rhs;
  //   final BinaryExprNode.Op op = node.op;
  //   switch (op) {
  //     }

  //     case BinaryExprNode.Op.Eql -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Neq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Ltn -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Leq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Gtn -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Geq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Lcj -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     case BinaryExprNode.Op.Ldj -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
  //     default -> throw new AssertionError();
  //   }
  // }

  // //////////////////////////////////////////////////////////////////////////////////////////////////
  // // Conditional
  // //////////////////////////////////////////////////////////////////////////////////////////////////

  // // Note -> Assumes stack -> ..., lhs, rhs
  // private void visitConditionalOp(
  //   final BinaryExprNode.Op op,
  //   final Kind kind,
  //   final Node       lhs,
  //   final Node       rhs,
  //   final Runnable   visitThen,
  //   final Runnable   visitElse // <-- Optional
  // ) {
  //   assert op != null && kind != null && lhs != null && rhs != null && visitThen != null;

  //   final Label doneLabel = new Label();
  //   final Label elseLabel = visitElse == null ? doneLabel : new Label();

  //   switch (op) {
  //     case BinaryExprNode.Op.Eql -> { // if a == b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case U32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case U64 -> { mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); } // NaN == 3.1 => (NaN ->-1) != 0 ? 0 : 1 => 0
  //         case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); } // NaN == NaN => (NaN ->-1) != 0 ? 0 : 1 => 0
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Neq -> { // if a != b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case U32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case U64 -> { mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); } // NaN != 3.1 => (NaN ->-1) == 0 ? 0 : 1 => 1
  //         case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); } // NaN != NaN => (NaN ->-1) == 0 ? 0 : 1 => 1
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Ltn -> { // if a < b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
  //         case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); } // NaN < 3.1 => (NaN ->+1) >= 0 ? 0 : 1 => 0
  //         case F64 -> { mv.visitInsn(Opcodes.DCMPG); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); } // NaN < NaN => (NaN ->+1) >= 0 ? 0 : 1 => 0
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Leq -> { // if a <= b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
  //         case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); } // NaN <= 3.1 => (NaN ->+1) > 0 ? 0 : 1 => 0
  //         case F64 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); } // NaN <= NaN => (NaN ->+1) > 0 ? 0 : 1 => 0
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Gtn -> { // if a > b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
  //         case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); } // NaN > 3.1 => (NaN ->-1) <= 0 ? 0 : 1 => 0
  //         case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); } // NaN > NaN => (NaN ->-1) <= 0 ? 0 : 1 => 0
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Geq -> { // if a >= b then <true> else <false>;
  //       visit(lhs);
  //       visit(rhs);
  //       switch (kind) {
  //         case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
  //         case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
  //         case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
  //         case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
  //         case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); } // NaN >= 3.1 => (NaN ->-1) < 0 ? 0 : 1 => 0
  //         case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); } // NaN >= NaN => (NaN ->-1) < 0 ? 0 : 1 => 0
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Lcj -> { // if a && b then <true> else <false>;
  //       switch(kind) {
  //         case BIT -> {
  //           visit(lhs); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
  //           visit(rhs); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
  //         }
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     case BinaryExprNode.Op.Ldj -> { // if a || b then <true> else <false>;
  //       switch(kind) {
  //         case BIT -> {
  //           final Label thenLabel = new Label();
  //           visit(lhs); mv.visitJumpInsn(Opcodes.IFEQ, thenLabel);
  //           visit(rhs); mv.visitJumpInsn(Opcodes.IFEQ, thenLabel);
  //           mv.visitJumpInsn(Opcodes.GOTO, elseLabel);
  //           mv.visitLabel(thenLabel);
  //         }
  //         default -> throw new AssertionError();
  //       }
  //     }
  //     default -> throw new AssertionError();
  //   }

  //   // <then>
  //   visitThen.run();
  //   if (visitElse != null) {
  //     // GOTO done
  //     // else ->
  //     // <else>
  //     mv.visitJumpInsn(Opcodes.GOTO, doneLabel);
  //     mv.visitLabel(elseLabel);
  //     visitElse.run();
  //   }
  //   // done ->
  //   mv.visitLabel(doneLabel);
  // }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // JVM Type Conversions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void visit_i2bit() { mv.visitInsn(Opcodes.ICONST_1); mv.visitInsn(Opcodes.IAND); }
  private void visit_i2u08() { mv.visitIntInsn(Opcodes.SIPUSH, 0xFF); mv.visitInsn(Opcodes.IAND); }
  private void visit_i2u16() { mv.visitInsn(Opcodes.I2C); }
  private void visit_i2i08() { mv.visitInsn(Opcodes.I2B); }
  private void visit_i2i16() { mv.visitInsn(Opcodes.I2S); }

  private void visit_l2i() { mv.visitInsn(Opcodes.L2I); }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // JVM Stack Constants
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void visitI(final int i32) {
    if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); return; }
    if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); return; }
    if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); return; }
    mv.visitLdcInsn(i32);
  }

  private void visitL(final long i64) {
    if (i64 == 0x0000000000000000L) { mv.visitInsn(Opcodes.LCONST_0); return; }
    if (i64 == 0x0000000000000001L) { mv.visitInsn(Opcodes.LCONST_1); return; }
    if (0xFFFFFFFFFFFFFFFFL <= i64 && i64 <= 0x0000000000000005L) { mv.visitInsn(Opcodes.ICONST_0 + (int)i64); mv.visitInsn(Opcodes.I2L); return; }
    if (0xFFFFFFFFFFFFFF80L <= i64 && i64 <= 0x000000000000007FL) { mv.visitIntInsn(Opcodes.BIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
    if (0xFFFFFFFFFFFF8000L <= i64 && i64 <= 0x0000000000007FFFL) { mv.visitIntInsn(Opcodes.SIPUSH, (int)i64); mv.visitInsn(Opcodes.I2L); return; }
    mv.visitLdcInsn(i64);
  }

  private void visitF(final float f32) {
    if (f32 == 0.0f) { mv.visitInsn(Opcodes.FCONST_0); return; }
    if (f32 == 1.0f) { mv.visitInsn(Opcodes.FCONST_1); return; }
    if (f32 == 2.0f) { mv.visitInsn(Opcodes.FCONST_2); return; }
    final int i32 = (int) f32;
    if (f32 == (float) i32) {
      if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); mv.visitInsn(Opcodes.I2F); return; }
      if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); mv.visitInsn(Opcodes.I2F); return; }
      if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); mv.visitInsn(Opcodes.I2F); return; }
    }
    mv.visitLdcInsn(f32);
  }

  private void visitD(final double f64) {
    if (f64 == 0.0) { mv.visitInsn(Opcodes.DCONST_0); return; }
    if (f64 == 1.0) { mv.visitInsn(Opcodes.DCONST_1); return; }
    final int i32 = (int) f64;
    if (f64 == (double) i32) {
      if (0xFFFFFFFF <= i32 && i32 <= 0x00000005) { mv.visitInsn(Opcodes.ICONST_0 + i32); mv.visitInsn(Opcodes.I2D); return; }
      if (0xFFFFFF80 <= i32 && i32 <= 0x0000007F) { mv.visitIntInsn(Opcodes.BIPUSH, i32); mv.visitInsn(Opcodes.I2D); return; }
      if (0xFFFF8000 <= i32 && i32 <= 0x00007FFF) { mv.visitIntInsn(Opcodes.SIPUSH, i32); mv.visitInsn(Opcodes.I2D); return; }
    }
    mv.visitLdcInsn(f64);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // JVM Invoke Static Methods
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Intrinsic Candiates
  // See -> https ->//chriswhocodes.com/hotspot_intrinsics_openjdk21.html
  private void visit_compareUnsigned_i()   { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "compareUnsigned", "(II)I", false); }
  private void visit_divideUnsigned_i()    { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "divideUnsigned", "(II)I", false); }
  private void visit_remainderUnsigned_i() { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "remainderUnsigned", "(II)I", false); }
  private void visit_compareUnsigned_l()   { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "compareUnsigned", "(JJ)I", false); }
  private void visit_divideUnsigned_l()    { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "divideUnsigned", "(JJ)I", false); }
  private void visit_remainderUnsigned_l() { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "remainderUnsigned", "(JJ)I", false); }

  // Non-Intrinsic
  private void visit_compare_i()           { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "compare", "(II)I", false); }
  private void visit_floorMod_i()          { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floorMod", "(II)I", false); }
  private void visit_floorMod_l()          { mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floorMod", "(JJ)J", false); }

}
