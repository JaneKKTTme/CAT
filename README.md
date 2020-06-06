# CAT (Catch and torture)
-------------------------

What is it?
-------------

Welcome! 
I am so happy to present you my university project for subject 'System Software'. The task, which was given to us, is creation an interpreter of our own programming language.


Installation
--------------

>*mvn package*

>*java -cp target/CAT-1.0.jar CatUI sample/BasicFunctionality.cat*


Functionality
---------------

- Math operations.
- Conditional operator 'if'.
- While loop.

**Basic part:**
- [x] Lexer is the process of converting a sequence of characters into a sequence of tokens.
- [x] Parser is a software component that takes input data and builds a data structure.
- [x] Reverse Polish Notation(RPN) is a mathematical notation in which operators follow their operands.
- [x] Stack-machine is a computer that uses a LIFO stack to hold short-lived temporary values. Return table of values.

**Additional part:**
- [x] Unlimited nested parentheses in math expresiions.
- [x] Implementation of a doubly linked list and using it in language.
- [ ] Implementation of a hash table and using it in language.


Grammar
---------

All grammar is described in file *CAT.gram*.




