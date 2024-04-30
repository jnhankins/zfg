
tuple def
* `()` - unit type
* `(x)` - tuple with one element `x`
* `(x, y)` - tuple with two elements
* `(...)` - tuples with n elements

tuple rules
* `(x)` == `x`
  * `(())` = `()`
  * `((x))` = `(x)`
  * `((x, y))` = `(x, y)`
  * `((x), y)` = `(x, y)`
  * `((x), (y))` = `(x, y)`
* `((), x, y)` = `(x, (), y)` = `(x, y, ())` = `(x, y)`

block def
* `{}` - no op
* `{ stmt }` -  block with one statement
* `{ stmt; stmt }` -  block with two statement
* `{ ... }` - block with n statements

block rules
* `{{}}` = `{}`
* `{{}; {stmt}}` = `{stmt}`
* etc.

functions
* `(x)(y){...}` - function with one parameter `x` returns `y` with impl block

```
let f (let x i32)(let y i32) = {...}; // let f ()
let z i32 = 13;
let y1 = z.f;
let y2 = z.f();
let y2 = f(x);
```
