package zfg;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import zfg.nodes.BinaryExprNode;
import zfg.nodes.ConstantExprNode;
import zfg.nodes.FunctionDeclNode;
import zfg.nodes.Node;
import zfg.nodes.ReturnStmtNode;
import zfg.nodes.VariableExprNode;
import zfg.types.Kind;
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
      case FunctionDeclNode n -> visitFunctionDecl(n);
      case ReturnStmtNode   n -> visitReturnStmt(n);
      case ConstantExprNode n -> visitConstantExpr(n);
      case VariableExprNode n -> visitVariableExpr(n);
      case BinaryExprNode   n -> visitBinaryExpr(n);
      default -> throw new AssertionError();
    }
  }

  private void visitConstantExpr(final ConstantExprNode node) {
    switch (node.value) {
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
      default -> throw new AssertionError();
    };
  }

  private void visitFunctionDecl(final FunctionDeclNode node) {
    assert mv == null;
    // Create a new method visitor
    mv = cv.visitMethod(
      Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL,
      node.name,
      node.type.descriptor(),
      null,
      null
    );
    // Visit the function body
    for (int i = 0; i < node.body.length; i++) {
      visit(node.body[i]);
    }
    // Finish the method
    mv.visitInsn(Opcodes.RETURN);
    mv.visitEnd();
    // Reset the method visitor
    mv = null;
  }

  private void visitReturnStmt(final ReturnStmtNode node) {
    if (node.expr != null) visit(node.expr);
    mv.visitInsn(Opcodes.RETURN);
  }

  private void visitVariableExpr(final VariableExprNode node) {
    // Loads a varaible onto the stack using one of:
    // - a static class-variable
    //   - getstatic <indexbyte1> <indexbyte2>
    // - a member class-variable
    //   - getfield <indexbyte1> <indexbyte2>
    // - a function local variable
    //   - iload <indexbyte> | wide iload <indexbyte1> <indexbyte2>
    //   - lload <indexbyte> | wide lload <indexbyte1> <indexbyte2>
    //   - fload <indexbyte> | wide fload <indexbyte1> <indexbyte2>
    //   - dload <indexbyte> | wide dload <indexbyte1> <indexbyte2>
    //   - aload <indexbyte> | wide aload <indexbyte1> <indexbyte2>
    switch (node.site) {
      case VariableExprNode.Site.Static -> {
        mv.visitFieldInsn(Opcodes.GETSTATIC, node.owner, node.name, node.type.descriptor());
      }
      case VariableExprNode.Site.Member -> {
        mv.visitFieldInsn(Opcodes.GETFIELD, node.owner, node.name, node.type.descriptor());
      }
      case VariableExprNode.Site.Local -> {
        Type type = node.type;
        while (type instanceof NomType nom) type = nom.aliasedType;
        switch (node.type.kind()) {
          case BIT, U08, U16, U32, I08, I16, I32 -> mv.visitVarInsn(Opcodes.ILOAD, node.index);
          case U64, I64 ->                          mv.visitVarInsn(Opcodes.LLOAD, node.index);
          case F32 ->                               mv.visitVarInsn(Opcodes.FLOAD, node.index);
          case F64 ->                               mv.visitVarInsn(Opcodes.DLOAD, node.index);
          case ARR, TUP, REC, FUN ->                mv.visitVarInsn(Opcodes.ALOAD, node.index);
          case NOM -> throw new AssertionError();
          case UNK, ERR -> throw new UnsupportedOperationException();
        }
      }
    }
  }

  private void visitBinaryExpr(final BinaryExprNode node) {
    mv.visitMethodInsn(0, null, null, null, false);
    final Kind kind = node.type.kind();
    final Node lhs = node.lhs;
    final Node rhs = node.rhs;
    final BinaryExprNode.Op op = node.op;
    switch (op) {
      case BinaryExprNode.Op.ADD -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
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
      case BinaryExprNode.Op.SUB -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
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
      case BinaryExprNode.Op.MUL -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
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
      case BinaryExprNode.Op.DIV -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case U08 -> mv.visitInsn(Opcodes.IDIV);
          case U16 -> mv.visitInsn(Opcodes.IDIV);
          case U32 -> visit_divideUnsigned_i();
          case U64 -> visit_divideUnsigned_l();
          case I08 -> mv.visitInsn(Opcodes.IDIV);
          case I16 -> mv.visitInsn(Opcodes.IDIV);
          case I32 -> mv.visitInsn(Opcodes.IDIV);
          case I64 -> mv.visitInsn(Opcodes.LDIV);
          case F32 -> mv.visitInsn(Opcodes.FDIV);
          case F64 -> mv.visitInsn(Opcodes.DDIV);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.REM -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case U08 -> mv.visitInsn(Opcodes.IREM);
          case U16 -> mv.visitInsn(Opcodes.IREM);
          case U32 -> visit_remainderUnsigned_i();
          case U64 -> visit_remainderUnsigned_l();
          case I08 -> mv.visitInsn(Opcodes.IREM);
          case I16 -> mv.visitInsn(Opcodes.IREM);
          case I32 -> mv.visitInsn(Opcodes.IREM);
          case I64 -> mv.visitInsn(Opcodes.LREM);
          case F32 -> mv.visitInsn(Opcodes.FREM);
          case F64 -> mv.visitInsn(Opcodes.DREM);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.MOD -> {
        // TODO revisit this
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case U08 -> mv.visitInsn(Opcodes.IREM);
          case U16 -> mv.visitInsn(Opcodes.IREM);
          case U32 -> visit_remainderUnsigned_i();
          case U64 -> visit_remainderUnsigned_l();
          case I08 -> visit_floorMod_i();
          case I16 -> visit_floorMod_i();
          case I32 -> visit_floorMod_i();
          case I64 -> visit_floorMod_l();
          case F32 -> { // a mod b = a - b * floor(a / b)
            visit(lhs);                    // Stack -> ..., a
            visit(rhs);                    // Stack -> ..., a, b
            mv.visitInsn(Opcodes.DUP2_X2); // Stack -> ..., a, b, a, b
            mv.visitInsn(Opcodes.FDIV);    // Stack -> ..., a, b, (a / b)
            mv.visitInsn(Opcodes.F2D);     // Stack -> ..., a, b, (a / b)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
            mv.visitInsn(Opcodes.D2F);     // Stack -> ..., a, b, floor(a / b)
            mv.visitInsn(Opcodes.I2F);     // Stack -> ..., a, b, floor(a / b)
            mv.visitInsn(Opcodes.FMUL);    // Stack -> ..., a, (b * floor(a / b))
            mv.visitInsn(Opcodes.FSUB);    // Stack -> ..., (a - b * floor(a / b))
          }
          case F64 -> { // a mod b = a - b * floor(a / b)
            visit(lhs);                    // Stack -> ..., a
            mv.visitInsn(Opcodes.DUP2);    // Stack -> ..., a, a
            visit(rhs);                    // Stack -> ..., a, a, b
            mv.visitInsn(Opcodes.DUP2_X2); // Stack -> ..., a, b, a, b
            mv.visitInsn(Opcodes.FDIV);    // Stack -> ..., a, b, (a / b)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "floor", "(D)D", false);
            mv.visitInsn(Opcodes.DMUL);    // Stack -> ..., a, (b * floor(a / b))
            mv.visitInsn(Opcodes.DSUB);    // Stack -> ..., (a - b * floor(a / b))
          }
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.AND -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> mv.visitInsn(Opcodes.IAND);
          case U08 -> mv.visitInsn(Opcodes.IAND);
          case U16 -> mv.visitInsn(Opcodes.IAND);
          case U32 -> mv.visitInsn(Opcodes.IAND);
          case U64 -> mv.visitInsn(Opcodes.LAND);
          case I08 -> mv.visitInsn(Opcodes.IAND);
          case I16 -> mv.visitInsn(Opcodes.IAND);
          case I32 -> mv.visitInsn(Opcodes.IAND);
          case I64 -> mv.visitInsn(Opcodes.LAND);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.IOR -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> mv.visitInsn(Opcodes.IOR);
          case U08 -> mv.visitInsn(Opcodes.IOR);
          case U16 -> mv.visitInsn(Opcodes.IOR);
          case U32 -> mv.visitInsn(Opcodes.IOR);
          case U64 -> mv.visitInsn(Opcodes.LOR);
          case I08 -> mv.visitInsn(Opcodes.IOR);
          case I16 -> mv.visitInsn(Opcodes.IOR);
          case I32 -> mv.visitInsn(Opcodes.IOR);
          case I64 -> mv.visitInsn(Opcodes.LOR);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Xor -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> mv.visitInsn(Opcodes.IXOR);
          case U08 -> mv.visitInsn(Opcodes.IXOR);
          case U16 -> mv.visitInsn(Opcodes.IXOR);
          case U32 -> mv.visitInsn(Opcodes.IXOR);
          case U64 -> mv.visitInsn(Opcodes.LXOR);
          case I08 -> mv.visitInsn(Opcodes.IXOR);
          case I16 -> mv.visitInsn(Opcodes.IXOR);
          case I32 -> mv.visitInsn(Opcodes.IXOR);
          case I64 -> mv.visitInsn(Opcodes.LXOR);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Shl -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case U08 -> { mv.visitInsn(Opcodes.ISHL); visit_i2u08(); }
          case U16 -> { mv.visitInsn(Opcodes.ISHL); visit_i2u16(); }
          case U32 -> { mv.visitInsn(Opcodes.ISHL); }
          case U64 -> { mv.visitInsn(Opcodes.LSHL); }
          case I08 -> { mv.visitInsn(Opcodes.ISHL); visit_i2i08(); }
          case I16 -> { mv.visitInsn(Opcodes.ISHL); visit_i2i16(); }
          case I32 -> { mv.visitInsn(Opcodes.ISHL); }
          case I64 -> { mv.visitInsn(Opcodes.LSHL); }
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Shr -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case U08 -> mv.visitInsn(Opcodes.ISHR);
          case U16 -> mv.visitInsn(Opcodes.ISHR);
          case U32 -> mv.visitInsn(Opcodes.ISHR);
          case U64 -> mv.visitInsn(Opcodes.LSHR);
          case I08 -> mv.visitInsn(Opcodes.ISHR);
          case I16 -> mv.visitInsn(Opcodes.ISHR);
          case I32 -> mv.visitInsn(Opcodes.ISHR);
          case I64 -> mv.visitInsn(Opcodes.LSHR);
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Cmp -> {
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> mv.visitInsn(Opcodes.ISUB);
          case U08 -> visit_compare_i();
          case U16 -> visit_compare_i();
          case U32 -> visit_compareUnsigned_i();
          case U64 -> visit_compareUnsigned_l();
          case I08 -> visit_compare_i();
          case I16 -> visit_compare_i();
          case I32 -> visit_compare_i();
          case I64 -> mv.visitInsn(Opcodes.LCMP );
          case F32 -> mv.visitInsn(Opcodes.FCMPL); // NaN <=> 3.1 => -1
          case F64 -> mv.visitInsn(Opcodes.DCMPL); // NaN <=> NaN => -1
          default -> throw new AssertionError();
        }
      }

      case BinaryExprNode.Op.Eql -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Neq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Ltn -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Leq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Gtn -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Geq -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Lcj -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      case BinaryExprNode.Op.Ldj -> visitConditionalOp(op, kind, lhs, rhs, this::visitI1, this::visitI0);
      default -> throw new AssertionError();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Conditional
  //////////////////////////////////////////////////////////////////////////////////////////////////

  // Note -> Assumes stack -> ..., lhs, rhs
  private void visitConditionalOp(
    final BinaryExprNode.Op op,
    final Kind kind,
    final Node       lhs,
    final Node       rhs,
    final Runnable   visitThen,
    final Runnable   visitElse // <-- Optional
  ) {
    assert op != null && kind != null && lhs != null && rhs != null && visitThen != null;

    final Label doneLabel = new Label();
    final Label elseLabel = visitElse == null ? doneLabel : new Label();

    switch (op) {
      case BinaryExprNode.Op.Eql -> { // if a == b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case U32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case U64 -> { mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPNE, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); } // NaN == 3.1 => (NaN ->-1) != 0 ? 0 : 1 => 0
          case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFNE, elseLabel); } // NaN == NaN => (NaN ->-1) != 0 ? 0 : 1 => 0
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Neq -> { // if a != b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case U32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case U64 -> { mv.visitInsn(Opcodes.LCMP); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPEQ, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); } // NaN != 3.1 => (NaN ->-1) == 0 ? 0 : 1 => 1
          case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel); } // NaN != NaN => (NaN ->-1) == 0 ? 0 : 1 => 1
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Ltn -> { // if a < b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
          case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGE, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); } // NaN < 3.1 => (NaN ->+1) >= 0 ? 0 : 1 => 0
          case F64 -> { mv.visitInsn(Opcodes.DCMPG); mv.visitJumpInsn(Opcodes.IFGE, elseLabel); } // NaN < NaN => (NaN ->+1) >= 0 ? 0 : 1 => 0
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Leq -> { // if a <= b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
          case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPGT, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); } // NaN <= 3.1 => (NaN ->+1) > 0 ? 0 : 1 => 0
          case F64 -> { mv.visitInsn(Opcodes.FCMPG); mv.visitJumpInsn(Opcodes.IFGT, elseLabel); } // NaN <= NaN => (NaN ->+1) > 0 ? 0 : 1 => 0
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Gtn -> { // if a > b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
          case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLE, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); } // NaN > 3.1 => (NaN ->-1) <= 0 ? 0 : 1 => 0
          case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFLE, elseLabel); } // NaN > NaN => (NaN ->-1) <= 0 ? 0 : 1 => 0
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Geq -> { // if a >= b then <true> else <false>;
        visit(lhs);
        visit(rhs);
        switch (kind) {
          case BIT -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case U08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case U16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case U32 -> { visit_compareUnsigned_i(); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
          case U64 -> { visit_compareUnsigned_l(); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
          case I08 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case I16 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case I32 -> { mv.visitJumpInsn(Opcodes.IF_ICMPLT, elseLabel); }
          case I64 -> { mv.visitInsn(Opcodes.LCMP ); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); }
          case F32 -> { mv.visitInsn(Opcodes.FCMPL); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); } // NaN >= 3.1 => (NaN ->-1) < 0 ? 0 : 1 => 0
          case F64 -> { mv.visitInsn(Opcodes.DCMPL); mv.visitJumpInsn(Opcodes.IFLT, elseLabel); } // NaN >= NaN => (NaN ->-1) < 0 ? 0 : 1 => 0
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Lcj -> { // if a && b then <true> else <false>;
        switch(kind) {
          case BIT -> {
            visit(lhs); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
            visit(rhs); mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
          }
          default -> throw new AssertionError();
        }
      }
      case BinaryExprNode.Op.Ldj -> { // if a || b then <true> else <false>;
        switch(kind) {
          case BIT -> {
            final Label thenLabel = new Label();
            visit(lhs); mv.visitJumpInsn(Opcodes.IFEQ, thenLabel);
            visit(rhs); mv.visitJumpInsn(Opcodes.IFEQ, thenLabel);
            mv.visitJumpInsn(Opcodes.GOTO, elseLabel);
            mv.visitLabel(thenLabel);
          }
          default -> throw new AssertionError();
        }
      }
      default -> throw new AssertionError();
    }

    // <then>
    visitThen.run();
    if (visitElse != null) {
      // GOTO done
      // else ->
      // <else>
      mv.visitJumpInsn(Opcodes.GOTO, doneLabel);
      mv.visitLabel(elseLabel);
      visitElse.run();
    }
    // done ->
    mv.visitLabel(doneLabel);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // JVM Type Conversions
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void visit_i2u08() { mv.visitIntInsn(Opcodes.SIPUSH, 0xFF); mv.visitInsn(Opcodes.IAND); }
  private void visit_i2u16() { mv.visitInsn(Opcodes.I2C); }
  private void visit_i2i08() { mv.visitInsn(Opcodes.I2B); }
  private void visit_i2i16() { mv.visitInsn(Opcodes.I2S); }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // JVM Stack Constants
  //////////////////////////////////////////////////////////////////////////////////////////////////

  private void visitI0() {
    mv.visitInsn(Opcodes.ICONST_0);
  }

  private void visitI1() {
    mv.visitInsn(Opcodes.ICONST_1);
  }

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
