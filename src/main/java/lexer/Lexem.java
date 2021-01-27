package lexer;

import java.util.regex.Pattern;

public enum Lexem {
    ADDITION_OP("\\+", 3),
    ASSIGN_OP("\\<-", 2),
    BOOL("true|false", 0),
    COMMA("\\,", -1),
    DIGIT("0|([1-9][0-9]*)", 0),
    //DIRECTION("\\->", 10),
    DIVISION_OP("\\/", 4),
    ELSE_KW("\\|", 5),
    EQUAL_LOGICAL_OP("\\==", 2),
    FUNCTION("\\&[a-zA-Z]+", 5),
    FUNCTION_KW("function", -1),
    GO_FALSE("go_false", -1),
    GO_TRUE("go_true", -1),
    IF_KW("\\?", 5),
    LEFT_B("\\(", 1),
    LEFT_BR("\\{", 1),
    LESS_LOGICAL_OP("\\<", 2),
    LESS_OR_EQUAL_LOGICAL_OP("\\<=", 2),
    METHODS("\\.[a-zA-Z]+", 5),
    MORE_LOGICAL_OP("\\>", 2),
    MORE_OR_EQUAL_LOGICAL_OP("\\>=", 2),
    MULTIPLICATION_OP("\\*", 4),
    OUTPUT_OP("print|say", 2),
    RIGHT_B("\\)", 1),
    RIGHT_BR("\\}", 1),
    RETURN_KW("return", 5),
    SEMICOLON("\\;", 10),
    SUBTRACTION_OP("\\-", 3),
    TYPE("int|double|string|bool|list|hashtable", 0),
    VAR("^[a-z]+", 0),
    WHILE_KW("\\!", 5),
    // Types for RPN
    FALSE_TRANSITION("",10),
    UNCONDITIONAL_TRANSITION("",10),
    INPUT_OP("",10),
    NEW_THREAD("",4),
    RPN_OUTPUT_OP("",2),
    OUTPUT_NEWLINE("",2),
    EMPTY_LEXEME("",-1);

    private Pattern pattern;
    private int priority;

    Lexem(String regexp, int priority) {
        this.pattern = Pattern.compile(regexp);
        this.priority = priority;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public int getPriority() {
        return this.priority;
    }
}