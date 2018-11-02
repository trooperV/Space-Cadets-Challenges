# Bare Bones Interpreter
  ### Syntax
1. `clear varName;` to __initialize a variable__ and set it's value to __0__ <br>
2. `incr varName;` to __add 1__ to varName <br>
3. `decr varName;` to __subtract 1__ from varName <br>
4. `while varName not intValue do;` and `end;` to open and close a __while loop__ <br>
5. `if varA sign varB do;` and `endif;` to open and close an __if statement__ <br>
  Supported sign values `< > <= >= ==`<br>

  ### Implemented
1. Initializing variables and changing their values.
2. While loops. Work like regular __while loops__ but there is no implementation for __varName__ support instead of the __intValue__. Nesting works fine.
3. __If statements__. Nesting works fine. There is no implementation of __else__ yet.

  ### Programs 1, 2 and 3
1. `program1.bb` tests the functionality of the __while loop__
2. `program2.bb` multiplication of __2__ numbers, currently __7__ and __5__
3. `program3.bb` tests the functionality of the __if statement__
  
  ### How to use
  In order to run a program just specify its source file. The `bb` file will be printed on the screen, followed by every instruction from execution to end (currently __if__ instructions do not show up on the instruction log). On the final __n__ lines will be printed the names and values of the __n__ variables that were initialized during execution.
