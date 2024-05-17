## 2.11.2. Load and Store Instructions

### Load a local variable onto the operand stack
- `iload <indexbyte>` `... → ..., value{I}`, `iload_0`, `iload_1`, `iload_2`, `iload_3`
  - **Load int from local variable**
- `lload <indexbyte>}` `... → ..., value{L}`, `lload_0`, `lload_1`, `lload_2`, `lload_3`
  - **Load long from local variable**
- `fload <indexbyte>` `... → ..., value{F}`, `fload_0`, `fload_1`, `fload_2`, `fload_3`
  - **Load float from local variable**
- `dload <indexbyte>` `... → ..., value{D}`, `dload_0`, `dload_1`, `dload_2`, `dload_3`
  - **Load double from local variable**
- `aload <indexbyte>` `... → ..., value{A}`, `aload_0`, `aload_1`, `aload_2`, `aload_3`
  - **Load reference from local variable**: An aload_<n> instruction cannot be used to load a value of type returnAddress from a local variable onto the operand stack. This asymmetry with the corresponding astore_<n> instruction (§astore_<n>) is intentional.

### Store a value from the operand stack into a local variable
- `istore <indexbyte>` `..., value{I} → ...`, `istore_0`, `istore_1`, `istore_2`, `istore_3`
  - **Store int into local variable**
- `lstore <indexbyte>` `..., value{L} → ...`, `lstore_0`, `lstore_1`, `lstore_2`, `lstore_3`
  - **Store long into local variable**
- `fstore <indexbyte>` `..., value{L} → ...`, `fstore_0`, `fstore_1`, `fstore_2`, `fstore_3`
  - **Store floag into local variable**
- `fstore <indexbyte>` `..., value{L} → ...`, `dstore_0`, `dstore_1`, `dstore_2`, `dstore_3`
  - **Store double into local variable**
- `astore <indexbyte>` `..., value{A} → ...`, `astore_0`, `astore_1`, `astore_2`, `astore_3`
  - **Store reference into local variable**

### Load a constant on to the operand stack
- `bipush <byte>` `... → ..., byte{I}`
  - **Push byte**: The immediate byte is sign-extended to an int value. That value is pushed onto the operand stack.
- `sipush <byte1> <byte2>` `... → ..., ((<byte1> << 8) | <byte2>){I}`
  - **Push short**: The immediate unsigned byte1 and byte2 values are assembled into an intermediate short, where the value of the short is (byte1 << 8) | byte2. The intermediate value is then sign-extended to an int value. That value is pushed onto the operand stack.
- `iconst_<i>`
  - `iconst_m1` `... → ..., -1{I}`
  - `iconst_0` `... → ..., 0{I}`
  - `iconst_1` `... → ..., 1{I}`
  - `iconst_2` `... → ..., 2{I}`
  - `iconst_3` `... → ..., 3{I}`
  - `iconst_4` `... → ..., 4{I}`
  - `iconst_5` `... → ..., 5{I}`
  - **Push int constant**: Push the int constant `<i>` (-1, 0, 1, 2, 3, 4 or 5) onto the operand stack. Each of this family of instructions is equivalent to bipush `<i>` for the respective value of `<i>`, except that the operand `<i>` is implicit.
- `lconst_<l>`
  - `lconst_0` `... → ..., 0{L}`
  - `lconst_1` `... → ..., 1{L}`
  - **Push long constant**: Push the long constant `<l>` (0 or 1) onto the operand stack.
- `fconst_<f>`
  - `fconst_0` `... → ..., 0.0{L}`
  - `fconst_1` `... → ..., 1.0{L}`
  - `fconst_2` `... → ..., 1.0{L}`
  - **Push float**: Push the float constant `<f>` (0.0, 1.0, or 2.0) onto the operand stack.
- `dconst_<d>`
  - `dconst_0` `... → ..., 0.0{D}`
  - `dconst_1` `... → ..., 1.0{D}`
  - **Push double**: Push the double constant `<d>` (0.0 or 1.0) onto the operand stack.
- `aconst_null` `... → ..., null{A}`
  - **Push null**:  Push the `null` object reference onto the operand stack. The Java Virtual Machine does not mandate a concrete value for `null`.
- `ldc <indexbyte>` `... → ..., const{1}`
  - **Push item from run-time constant pool**: The index is an unsigned byte that must be a valid index into the run-time constant pool of the current class (§2.5.5). The run-time constant pool entry at index must be loadable (§5.1), and not any of the following:
    - A numeric constant of type long or double.
    - A symbolic reference to a dynamically-computed constant whose field descriptor is J (denoting long) or D (denoting double).
  - If the run-time constant pool entry is a numeric constant of type int or float, then the value of that numeric constant is pushed onto the operand stack as an int or float, respectively.
  - Otherwise, if the run-time constant pool entry is a string constant, that is, a reference to an instance of class String, then value, a reference to that instance, is pushed onto the operand stack.
  - Otherwise, if the run-time constant pool entry is a symbolic reference to a class or interface, then the named class or interface is resolved (§5.4.3.1) and value, a reference to the Class object representing that class or interface, is pushed onto the operand stack.
  - Otherwise, the run-time constant pool entry is a symbolic reference to a method type, a method handle, or a dynamically-computed constant. The symbolic reference is resolved (§5.4.3.5, §5.4.3.6) and value, the result of resolution, is pushed onto the operand stack.
  - During resolution of a symbolic reference, any of the exceptions pertaining to resolution of that kind of symbolic reference can be thrown.
- `ldc_w <indexbyte1> <indexbyte2>` `... → ..., const{1}`
  - **Push item from run-time constant pool (wide index)**: The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index into the run-time constant pool of the current class (§2.5.5), where the value of the index is calculated as `(indexbyte1 << 8) | indexbyte2`.
  - The ldc_w instruction is identical to the ldc instruction (§ldc) except for its wider run-time constant pool index.
- `ldc2_w <indexbyte1> <indexbyte2>` `... → ..., const{2}`
  - The unsigned indexbyte1 and indexbyte2 are assembled into an unsigned 16-bit index into the run-time constant pool of the current class (§2.5.5), where the value of the index is calculated as `(indexbyte1 << 8) | indexbyte2`. The index must be a valid index into the run-time constant pool of the current class. The run-time constant pool entry at the index must be loadable (§5.1), and in particular one of the following:
    - A numeric constant of type long or double.
    - A symbolic reference to a dynamically-computed constant whose field descriptor is J (denoting long) or D (denoting double).
  -  If the run-time constant pool entry is a numeric constant of type long or double, then the value of that numeric constant is pushed onto the operand stack as a long or double, respectively.
  - Otherwise, the run-time constant pool entry is a symbolic reference to a dynamically-computed constant. The symbolic reference is resolved (§5.4.3.6) and value, the result of resolution, is pushed onto the operand stack.
  - During resolution of a symbolic reference to a dynamically-computed constant, any of the exceptions pertaining to dynamically-computed constant resolution can be thrown.
  - Only a wide-index version of the ldc2_w instruction exists; there is no ldc2 instruction that pushes a long or double with a single-byte index.

### Gain access to more local variables using a wider index, or to a larger immediate operand
- `wide`
  - `wide <opcode> <indexbyte1> <indexbyte2>` where <opcode> is one of iload, fload, aload, lload, dload, istore, fstore, astore, lstore, dstore, or ret.
  - `wide iinc <indexbyte1> <indexbyte2> <constbyte1> <constbyte2>`
  - **Extend local variable index by additional bytes**: The wide instruction modifies the behavior of another instruction. It takes one of two formats, depending on the instruction being modified. The first form of the wide instruction modifies one of the instructions iload, fload, aload, lload, dload, istore, fstore, astore, lstore, dstore, or ret (§iload, §fload, §aload, §lload, §dload, §istore, §fstore, §astore, §lstore, §dstore, §ret). The second form applies only to the iinc instruction (§iinc).
  - In either case, the wide opcode itself is followed in the compiled code by the opcode of the instruction wide modifies. In either form, two unsigned bytes indexbyte1 and indexbyte2 follow the modified opcode and are assembled into a 16-bit unsigned index to a local variable in the current frame (§2.6), where the value of the index is `(indexbyte1 << 8) | indexbyte2`. The calculated index must be an index into the local variable array of the current frame. Where the wide instruction modifies an lload, dload, lstore, or dstore instruction, the index following the calculated index `(index + 1)` must also be an index into the local variable array. In the second form, two immediate unsigned bytes constbyte1 and constbyte2 follow indexbyte1 and indexbyte2 in the code stream. Those bytes are also assembled into a signed 16-bit constant, where the constant is `(constbyte1 << 8)` | constbyte2.
  - The widened bytecode operates as normal, except for the use of the wider index and, in the case of the second form, the larger increment range.
Notes
  - Although we say that wide "modifies the behavior of another instruction," the wide instruction effectively treats the bytes constituting the modified instruction as operands, denaturing the embedded instruction in the process. In the case of a modified iinc instruction, one of the logical operands of the iinc is not even at the normal offset from the opcode. The embedded instruction must never be executed directly; its opcode must never be the target of any control transfer instruction.

## 2.11.3. Arithmetic Instructions

### Add
- `iadd` `..., value1{I}, value2{I} → ..., result{I}`
- `ladd` `..., value1{L}, value2{L} → ..., result{L}`
- `fadd` `..., value1{L}, value2{L} → ..., result{L}`
- `dadd` `..., value1{D}, value2{D} → ..., result{D}`

### Subtract
- `isub` `..., value1{I}, value2{I} → ..., result{I}`
- `lsub` `..., value1{L}, value2{L} → ..., result{L}`
- `fsub` `..., value1{L}, value2{L} → ..., result{L}`
- `dsub` `..., value1{D}, value2{D} → ..., result{D}`

### Multiply
- `imul` `..., value1{I}, value2{I} → ..., result{I}`
- `lmul` `..., value1{L}, value2{L} → ..., result{L}`
- `fmul` `..., value1{L}, value2{L} → ..., result{L}`
- `dmul` `..., value1{D}, value2{D} → ..., result{D}`

### Divide
- `idiv` `..., value1{I}, value2{I} → ..., result{I}`
- `ldiv` `..., value1{L}, value2{L} → ..., result{L}`
- `fdiv` `..., value1{L}, value2{L} → ..., result{L}`
- `ddiv` `..., value1{D}, value2{D} → ..., result{D}`

### Remainder
- `irem` `..., value1{I}, value2{I} → ..., result{I}`
- `lrem` `..., value1{L}, value2{L} → ..., result{L}`
- `frem` `..., value1{L}, value2{L} → ..., result{L}`
- `drem` `..., value1{D}, value2{D} → ..., result{D}`

### Negate
- `ineg` `..., value1{I} → ..., result{I}`
- `lneg` `..., value1{L} → ..., result{L}`
- `fneg` `..., value1{F} → ..., result{F}`
- `dneg` `..., value1{D} → ..., result{D}`

### Shift
- `ishl` `..., value1{I}, value2{I} → ..., result{I}`
- `lshl` `..., value1{L}, value2{I} → ..., result{L}`
- `ishr` `..., value1{I}, value2{I} → ..., result{I}`
- `lshr` `..., value1{L}, value2{I} → ..., result{L}`
- `iushr` `..., value1{I}, value2{I} → ..., result{I}`
- `lushr` `..., value1{L}, value2{I} → ..., result{L}`

### Bitwise OR
- `ior` `..., value1{I}, value2{I} → ..., result{I}`
- `lor` `..., value1{L}, value2{L} → ..., result{L}`

### Bitwise AND
- `iand` `..., value1{I}, value2{I} → ..., result{I}`
- `land` `..., value1{L}, value2{L} → ..., result{L}`

### Bitwise XOR
- `ixor` `..., value1{I}, value2{I} → ..., result{I}`
- `lxor` `..., value1{L}, value2{L} → ..., result{L}`

### Local variable increment
- `iinc <index> <const>`
  - **Increment local variable by constant**:  The index is an unsigned byte that must be an index into the local variable array of the current frame (§2.6). The const is an immediate signed byte. The local variable at index must contain an int. The value const is first sign-extended to an int, and then the local variable at index is incremented by that amount.
  - The iinc opcode can be used in conjunction with the wide instruction (§wide) to access a local variable using a two-byte unsigned index and to increment it by a two-byte immediate signed value.

### Comparison
- `lcmp` `..., value1{L}, value2{L} → ..., result{I}`
- `fcmpg` `..., value1{F}, value2{F} → ..., result{I}`
- `fcmpl` `..., value1{F}, value2{F} → ..., result{I}`
- `dcmpg` `..., value1{D}, value2{D} → ..., result{I}`
- `dcmpl` `..., value1{D}, value2{D} → ..., result{I}`
If value1 is greater than value2, the int value 1 is pushed onto the operand stack.
Otherwise, if value1 is equal to value2, the int value 0 is pushed onto the operand stack.
Otherwise, if value1 is less than value2, the int value -1 is pushed onto the operand stack.
Otherwise, at least one of value1 or value2 is NaN. The f/gcmpg instruction pushes the int value 1 onto the operand stack and the f/gcmpl instruction pushes the int value -1 onto the operand stack.

## 2.11.4. Type Conversion Instructions
- `i2c` `..., value{I} → ..., result{I}`
- `i2b` `..., value{I} → ..., result{I}`
- `i2s` `..., value{I} → ..., result{I}`
- `i2l` `..., value{I} → ..., result{L}`
- `i2f` `..., value{I} → ..., result{F}`
- `i2d` `..., value{I} → ..., result{D}`
- `l2i` `..., value{L} → ..., result{I}`
- `l2d` `..., value{L} → ..., result{D}`
- `l2f` `..., value{L} → ..., result{F}`
- `f2i` `..., value{F} → ..., result{I}`
- `f2l` `..., value{F} → ..., result{L}`
- `f2d` `..., value{F} → ..., result{D}`
- `d2i` `..., value{D} → ..., result{I}`
- `d2l` `..., value{D} → ..., result{L}`
- `d2f` `..., value{D} → ..., result{F}`
NaN{F/D} -> 0{I/L}

## 2.11.5. Object Creation and Manipulation

### Create a new class instance
- `new <indexbyte1> <indexbyte2>` `... → ..., objectref{A}`
  - **Create new object**: The unsigned indexbyte1 and indexbyte2 are used to construct an index into the run-time constant pool of the current class (§2.6), where the value of the index is `(indexbyte1 << 8) | indexbyte2`. The run-time constant pool entry at the index must be a symbolic reference to a class or interface type. The named class or interface type is resolved (§5.4.3.1) and should result in a class type. Memory for a new instance of that class is allocated from the garbage-collected heap, and the instance variables of the new object are initialized to their default initial values (§2.3, §2.4). The objectref, a reference to the instance, is pushed onto the operand stack.
  - On successful resolution of the class, it is initialized if it has not already been initialized (§5.5).
  - The new instruction does not completely create a new instance; instance creation is not completed until an instance initialization method (§2.9.1) has been invoked on the uninitialized instance.

### Create a new array
- `newarray <atype>`  `..., count{I} → ..., arrayref{A}`
  - **Create new array of reference**: The atype is a code that indicates the type of array to create. It must take one of the following values: `T_BOOLEAN`, `T_CHAR`, `T_BYTE`, `T_SHORT`, `T_INT`, `T_LONG`, `T_FLOAT`, `T_DOUBLE`
- `anewarray <indexbyte1> <indexbyte2>` `..., count{I} → ..., arrayref{A}`
  - **Create new array of reference**
- `multianewarray`

### Access fields of classes and and fields of class instances
- `getstatic <indexbyte1> <indexbyte2>` `... → ..., value{ANY}`
- `getfield <indexbyte1> <indexbyte2>` `..., objectref{A} → ..., value{ANY}`
- `putstatic <indexbyte1> <indexbyte2>` `..., value{ANY} → ...`
- `putfield <indexbyte1> <indexbyte2>` `..., objectref{A}, value{ANY} → ...`

### Load an array component onto the operand stack
- `caload` `..., arrayref{A}, index>{I} → ..., value{I}`
  - **Load char from array**: The component of the array at index is retrieved and zero-extended to an int value.
- `baload` `..., arrayref{A}, index>{I} → ..., value{I}`
  - **Load byte from array**: The byte value in the component of the array at index is retrieved, sign-extended to an int value.
- `saload` `..., arrayref{A}, index>{I} → ..., value{I}`
  - **Load short from array**: The component of the array at index is retrieved and sign-extended to an int value.
- `iaload` `..., arrayref{A}, index>{I} → ..., value{I}`
  - **Load int from array**
- `laload` `..., arrayref{A}, index>{I} → ..., value{L}`
  - **Load long from array**
- `faload` `..., arrayref{A}, index>{I} → ..., value{F}`
  - **Load float from array**
- `daload` `..., arrayref{A}, index>{I} → ..., value{D}`
  - **Load double from array**
- `aaload` `..., arrayref{A}, index>{I} → ..., value{A}`
  - **Load reference from array**

### Store a value from the operand stack as an array component
- `castore` `..., arrayref{A}, index{I}, value{I} → ...`
- `bastore` `..., arrayref{A}, index{I}, value{I} → ...`
- `sastore` `..., arrayref{A}, index{I}, value{I} → ...`
- `iastore` `..., arrayref{A}, index{I}, value{I} → ...`
- `lastore` `..., arrayref{A}, index{I}, value{L} → ...`
- `fastore` `..., arrayref{A}, index{I}, value{F} → ...`
- `dastore` `..., arrayref{A}, index{I}, value{D} → ...`
- `aastore` `..., arrayref{A}, index{I}, value{A} → ...`

### Get the length of array
- `arraylength` `..., arrayref{A} → ..., length{I}`

### Check properties of class instances or arrays
- `instanceof <indexbyte1> <indexbyte2>` `..., objectref{A} → ..., result{I}`
  - **Determine if object is of given type**: The objectref, which must be of type reference, is popped from the operand stack. The unsigned indexbyte1 and indexbyte2 are used to construct an index into the run-time constant pool of the current class (§2.6), where the value of the index is `(indexbyte1 << 8) | indexbyte2`. The run-time constant pool entry at the index must be a symbolic reference to a class, array, or interface type.
  - If objectref is `null`, the instanceof instruction pushes an int result of 0 as an int onto the operand stack.
  - Otherwise, the named class, array, or interface type is resolved (§5.4.3.1). If objectref is an instance of the resolved class or array type, or implements the resolved interface, the instanceof instruction pushes an int result of 1 as an int onto the operand stack; otherwise, it pushes an int result of 0.
  - See documentation for rules for determining the outcome of checkcast.
- `checkcast <indexbyte1> <indexbyte2>` `..., objectref{A} → ..., objectref{A}`
  - **Check whether object is of given type**: The objectref must be of type reference. The unsigned indexbyte1 and indexbyte2 are used to construct an index into the run-time constant pool of the current class (§2.6), where the value of the index is `(indexbyte1 << 8) | indexbyte2`. The run-time constant pool entry at the index must be a symbolic reference to a class, array, or interface type.
  - If objectref is `null`, then the operand stack is unchanged.
  - Otherwise, the named class, array, or interface type is resolved (§5.4.3.1). If objectref can be cast to the resolved class, array, or interface type, the operand stack is unchanged; otherwise, the checkcast instruction throws a ClassCastException.
  - See documentation for rules for determining the outcome of checkcast.

## 2.11.6. Operand Stack Management Instructions

### Pop
- `pop`
  - `..., value{1} → ...`
  - **Pop the top value from the operand stack.**:  Pop the top one or two values from the operand stack.
- `pop2`
  - `..., value2{1}, value1{1} → ...`
  - `..., value{2} → ...`
  - **Pop the top operand stack value**: Pop the top one or two operand stack values

### Dup
- `dup`
  - `..., value{1} → ..., value{1}, value{1}`
  - **Duplicate the top operand stack value**:  Duplicate the top value on the operand stack and push the duplicated value onto the operand stack.
- `dup_x1`
  - `..., value2{1}, value1{1} → ..., value1{1}, value2{1}, value1{1}`
  - **Duplicate the top operand stack value and insert two values down**: Duplicate the top value on the operand stack and insert the duplicated value two values down in the operand stack.
- `dup_x2`
  - `..., value3{1}, value2{1}, value1{1} → ..., value1{1}, value3{1}, value2{1}, value1{1}`
  - `..., value2{2}, value1{1} → ..., value1{1}, value2{2}, value1{1}`
  - **Duplicate the top operand stack value and insert two or three values down**: Duplicate the top value on the operand stack and insert the duplicated value two or three values down in the operand stack.
- `dup2` ``
  - `..., value2{1}, value1{1} → ..., value2{1}, value1{1}, value2{1}, value1{1}`
  - `..., value{2} → ..., value{2}, value{2}`
  - **Duplicate the top one or two operand stack values**:  Duplicate the top one or two values on the operand stack and push the duplicated value or values back onto the operand stack in the original order.
- `dup2_x1` ``
  - `..., value3{1}, value2{1}, value1{1} → ..., value2{1}, value1{1}, value3{1}, value2{1}, value1{1}`
  - `..., value2{1}, value1{2} → ..., value1{2}, value2{1}, value1{2}`
  - **Duplicate the top one or two operand stack values and insert two or three values down**: Duplicate the top one or two values on the operand stack and insert the duplicated values, in the original order, one value beneath the original value or values in the operand stack.
- `dup2_x2`
  - `..., value4{1}, value3{1}, value2{1}, value1{1} → ..., value2{1}, value1{1}, value4{1}, value3{1}, value2{1}, value1{1}`
  - `..., value3{1}, value2{1}, value1{2} → ..., value1{2}, value3{1}, value2{1}, value1{2}`
  - `..., value3{2}, value2{1}, value1{1} → ..., value2{1}, value1{1}, value3{2}, value2{1}, value1{1}`
  - `..., value2{2}, value1{2} → ..., value1{2}, value2{2}, value1{2}`
  - **Duplicate the top one or two operand stack values and insert two, three, or four values down**: Duplicate the top one or two values on the operand stack and insert the duplicated values, in the original order, into the operand stack.

### Swap
- `swap`: `..., value2{1}, value1{1} → ..., value1{1}, value2{1}`
  - **Swap the top two operand stack values**: The Java Virtual Machine does not provide an instruction implementing a swap on operands of category 2 computational types.


## 2.11.7. Control Transfer Instructions

### Conditional branch
- `ifeq <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If equal to zero, jump to offset**
- `ifne <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If not euqual to zero, jump to offset**
- `iflt <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If less than zero, jump to offset**
- `ifle <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If less than or equal to zero, jump to offset**
- `ifgt <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If greater than zero, jump to offset**
- `ifge <branchbyte1> <branchbyte2>` `..., value{I} → ...`
  - **If greater than or equal to zero, jump to offset**
- `ifnull <branchbyte1> <branchbyte2>` `..., value{A} → ...`
  - **If equal to null, jump to offset**
- `ifnonnull <branchbyte1> <branchbyte2>` `..., value{A} → ...`
  - **If not equal to null, jump to offset**
- `if_icmpeq <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is equal to value2, jump to offset**
- `if_icmpne <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is not equal to value2, jump to offset**
- `if_icmplt <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is less than value2, jump to offset**
- `if_icmple <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is less than or equal to value2, jump to offset**
- `if_icmpgt <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is greater than value2, jump to offset**
- `if_icmpge <branchbyte1> <branchbyte2>` `..., value1{I}, value2{I} → ...`
  - **If value1 is greater than or equal to value2, jump to offset**
- `if_acmpeq <branchbyte1> <branchbyte2>` `..., value1{A}, value2{A} → ...`
  - **If value1 is equal to value2, jump to offset**
- `if_acmpne <branchbyte1> <branchbyte2>` `..., value1{A}, value2{A} → ...`
  - **If value1 is not equal to value2, jump to offset**

### Compound conditional branch
- `tableswitch <0-3 byte pad> <defaultbyte1> <defaultbyte2> <defaultbyte3> <defaultbyte4> <lowbyte1> <lowbyte2> <lowbyte3> <lowbyte4> <highbyte1> <highbyte2> <highbyte3> <highbyte4> <jump offsets...>` `..., index{I} → ...`
  - **Access jump table by index and jump**: A tableswitch is a variable-length instruction. Immediately after the tableswitch opcode, between zero and three bytes must act as padding, such that defaultbyte1 begins at an address that is a multiple of four bytes from the start of the current method (the opcode of its first instruction). Immediately after the padding are bytes constituting three signed 32-bit values: default, low, and high. Immediately following are bytes constituting a series of `high - low + 1` signed 32-bit offsets. The value low must be less than or equal to high. The `high - low + 1` signed 32-bit offsets are treated as a 0-based jump table. Each of these signed 32-bit values is constructed as `(byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4`.
  - The index must be of type int and is popped from the operand stack. If index is less than low or index is greater than high, then a target address is calculated by adding default to the address of the opcode of this tableswitch instruction. Otherwise, the offset at position index - low of the jump table is extracted. The target address is calculated by adding that offset to the address of the opcode of this tableswitch instruction. Execution then continues at the target address.
  - The target address that can be calculated from each jump table offset, as well as the one that can be calculated from default, must be the address of an opcode of an instruction within the method that contains this tableswitch instruction.
- `lookupswitch <0-3 byte pad> <defaultbyte1> <defaultbyte2> <defaultbyte3> <defaultbyte4> <npairs1> <npairs2> <npairs3> <npairs4> <match-offset pairs ...>` `..., key{I} → ...`
  - **Access jump table by key match and jump**: A lookupswitch is a variable-length instruction. Immediately after the lookupswitch opcode, between zero and three bytes must act as padding, such that defaultbyte1 begins at an address that is a multiple of four bytes from the start of the current method (the opcode of its first instruction). Immediately after the padding follow a series of signed 32-bit values: default, npairs, and then npairs pairs of signed 32-bit values. The npairs must be greater than or equal to 0. Each of the npairs pairs consists of an int match and a signed 32-bit offset. Each of these signed 32-bit values is constructed from four unsigned bytes as `(byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4`.
  - The table match-offset pairs of the lookupswitch instruction must be sorted in increasing numerical order by match.
  - The key must be of type int and is popped from the operand stack. The key is compared against the match values. If it is equal to one of them, then a target address is calculated by adding the corresponding offset to the address of the opcode of this lookupswitch instruction. If the key does not match any of the match values, the target address is calculated by adding default to the address of the opcode of this lookupswitch instruction. Execution then continues at the target address.
  - The target address that can be calculated from the offset of each match-offset pair, as well as the one calculated from default, must be the address of an opcode of an instruction within the method that contains this lookupswitch instruction.
  - The alignment required of the 4-byte operands of the lookupswitch instruction guarantees 4-byte alignment of those operands if and only if the method that contains the lookupswitch is positioned on a 4-byte boundary.
  - The match-offset pairs are sorted to support lookup routines that are quicker than linear search.

### Unconditional branch
- `goto <branchbyte1> <branchbyte2>`
  - **Branch always**
- `goto_w <branchbyte1> <branchbyte2> <branchbyte3> <branchbyte4>`
  - **Branch always (wide index)**
- `jsr <branchbyte1> <branchbyte2>` `... → ..., address{1}`
  - **Jump subroutine**
- `jsr_w <branchbyte1> <branchbyte2> <branchbyte3> <branchbyte4>` `... → ..., address{1}`
  - **Jump subroutine (wide index)**
- `ret <index>`
  - **Return from subroutine**: The index is an unsigned byte between 0 and 255, inclusive. The local variable at index in the current frame (§2.6) must contain a value of type returnAddress. The contents of the local variable are written into the Java Virtual Machine's pc register, and execution continues there.
  - Note that jsr (§jsr) pushes the address onto the operand stack and ret gets it out of a local variable. This asymmetry is intentional.
  - In Oracle's implementation of a compiler for the Java programming language prior to Java SE 6, the ret instruction was used with the jsr and jsr_w instructions (§jsr, §jsr_w) in the implementation of the finally clause (§3.13, §4.10.2.5).
  -  The ret instruction should not be confused with the return instruction (§return). A return instruction returns control from a method to its invoker, without passing any value back to the invoker.
  -  The ret opcode can be used in conjunction with the wide instruction (§wide) to access a local variable using a two-byte unsigned index.

## 2.11.8. Method Invocation and Return Instructions

### Invoke
- `invokevirtual <indexbyte1> <indexbyte2>` `..., objectref{A}, [arg1, [arg2 ...]] → ...`
  - Invokes an instance method of an object, dispatching on the (virtual) type of the object. This is the normal method dispatch in the Java programming language.
- `invokeinterface <indexbyte1> <indexbyte2> <count> 0` `..., objectref, [arg1, [arg2 ...]] → ...`
  - Invokes an interface method, searching the methods implemented by the particular run-time object to find the appropriate method.
- `invokespecial <indexbyte1> <indexbyte2>` `..., objectref{A}, [arg1, [arg2 ...]] → ...`
  - Invokes an instance method requiring special handling, either an instance initialization method (§2.9.1) or a method of the current class or its supertypes.
- `invokestatic <indexbyte1> <indexbyte2>` `..., [arg1, [arg2 ...]] → ...`
  - Invokes a class (static) method in a named class.
- `invokedynamic <indexbyte1> <indexbyte2> 0 0` `..., [arg1, [arg2 ...]] → ...`
  - Invokes the method which is the target of the call site object bound to the invokedynamic instruction. The call site object was bound to a specific lexical occurrence of the invokedynamic instruction by the Java Virtual Machine as a result of running a bootstrap method before the first execution of the instruction. Therefore, each occurrence of an invokedynamic instruction has a unique linkage state, unlike the other instructions which invoke methods.

### Return
- `return` `... → [empty]`
- `ireturn` `..., value{I} → [empty]`
- `lreturn` `..., value{L} → [empty]`
- `freturn` `..., value{F} → [empty]`
- `dreturn` `..., value{D} → [empty]`
- `areturn` `..., value{A} → [empty]`

## 2.11.9. Throwing Exceptions
- `athrow` `..., objectref{A} → objectref{A}`

## 2.11.10. Synchronization
- `monitorenter` `..., objectref{A} → ...`
- `monitorexit` `..., objectref{A} → ...`

## Other
- `nop`
  - **Do nothing.**


