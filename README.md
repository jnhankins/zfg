
## Types
- `()`: Unit type
  - **`(`** **`)`**
- `bit`: 1-bit unsigned integer, (Java boolean)
  - e.g. `false`, `0bit`, or `0b0000_bit`, etc.
  - e.g. `true`, `1bit`, or `0b0001_bit`, etc.
  - Ex: <code>let x <u>bit</u> = true</code>
- `u08`: 8-bit unsigned integer
  - Ex: <code>let x <u>u08</u> = 7u08</code>
- `u16`: 16-bit unsigned integer (Java char)
- `u32`: 32-bit unsigned integer
- `u64`: 64-bit unsigned integer
- `i08`: 8-bit signed integer  (Java byte)
- `i16`: 16-bit signed integer (Java short)
- `i32`: 32-bit signed integer (Java int)
- `i64`: 64-bit signed integer (Java long)
- `f32`: 32-bit floating-point number (Java float)
- `f64`: 64-bit floating-point number (Java double)
- `rec`: Record type
  - e.g. `(let x i32)` one immutable field `x` of type `i32`
  - e.g. `(mut x i32, let y f64)` one mutable field `x` of type `i32` and one immutable field of type `i32`
  - e.g. `(x i32, y f64)` <-- modifier defaults to `let` when omitted
  - Rule: **`(`** *`modifier`*? *`symbol`* *`type`* **`,`** ... **`)`**
  - Note: When there are mupltiple fields, successive files must be separated by a comma. The list
    fields can always be optionally terminated by a comma. There must be at least one field
    otherwise the type will be interpreted as the unit type, i.e. `()`.
  - Ex: <code>let point <u>(x i32, y i32)</u> = (3, 7)</code>
- `fun`: Function type
  - e.g. `(let x i32)i32` immutable parameter `x` has type `i32`; return type is `i32`
  - e.g. `(mut x i32, mut y i32) ()` mutable parameters `x` and `y` have type `i32`; return type is `unit`
  - e.g. `()()` parameters: none; returns: `unit`
  - e.g. `()i32` parameters: none; returns: a `i32`
  - e.g. `()(x i32)` parameters: none; returns: rec `(let x i32)`
  - e.g. `()(y i32)()` parameters: none; returns: fun `(let y i32)()`
  - e.g. `()((x i32)())` return type `((x i32)())` is **invalid**: it's not unit, a primitive, a rec or a fun
  - Rule: *`record`* *`type`*
  - Ex: <code>let inc<u>(x i32) i32</u> = x + 1 </code>
- `arr`: Array type
  - Note: all arrays have built in field `let size i32` and `size >= 0`
  - e.g. `[i32]` array of n `i32` elements, *length is not checked at compiletime*
  - e.g. `[i32 5]` array of 5 `i32` elements, *length is checked at compiletime*
  - e.g. `[[i32 3]]` array array of n `[i32 3]` elements
  - e.g. `[[i32 3] 5]` array array of 5 `[i32 3]` elements
  - Rule: **`[`** *type* **`]`**
  - Rule: **`[`** *type* **`*`** *size*? **`]`**
  - Ex: <code>let my_array <u>[i32]</u> = [1, 2, 3]</code>
  - Ex: <code>let my_array <u>[i32 * 3]</u> = [1, 2, 3]</code>
  - Ex: <code>let my_array <u>[i32 * 3]</u> = [1, 2]</code> **Error:** "type `[i32 * 2]` cannot be assigned to type `[i32 * 3]`"
  - **TODO: how to fill and / or initialize arrays?**



## Symbol Definitions

- `let`: Immutable symbol definition
  - a *let-symbol* **can** be defined in any scope
  - a *let-symbol* **cannot** be reassigned
  - a *let-symbol* referent **cannot** be mutated
  - a *let-symbol* **can** be redefined in the same scope
  - a *let-symbol* **can** be hidden by a definition in a nested scope
  - a *let-symbol* **can** be used on any line of code after the definition until end of scope or its redefined
  - **`let`** *`symbol`* *`type`* **`=`** *`expression`*
- `mut`: Mutable symbol definition
  - a *mut-symbol* **can** be defined in any scope
  - a *mut-symbol* **can** be reassigned
  - a *mut-symbol* referent **can** be mutated
  - a *mut-symbol* **can** be hidden by redefining the symbol in the same scope
  - a *mut-symbol* **can** be hidden by a definition in a nested scope
  - a *mut-symbol* **can** be used on any line of code after the definition until end of scope or its redefined
  - **`mut`** *`symbol`* *`type`* **`=`** *`expression`*
- `pub`: Publish symbol definition
  - a *pub-symbol* **can only** be defined in a *module* scope but not in a *parameter* scope
  - a *pub-symbol* **cannot** be reassigned
  - a *pub-symbol* referent **cannot** be mutated
  - a *pub-symbol* **cannot** be hidden by redefining the symbol in the same scope
  - a *pub-symbol* **can** be hidden by a definition in a nested scope
  - a *pub-symbol* **can** be used on any line of code after the definition until end of scope or its redefined
  - **`pub`** *`symbol`* *`type`* **`=`** *`expression`*
- `use`:
  - **`use`** *`symbol`* *`type`* **`=`** *`expression`*
