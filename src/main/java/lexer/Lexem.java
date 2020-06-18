package lexer;

import java.util.regex.Pattern;

public enum Lexem {
    ADDITION_OP("\\+"),
    ASSIGN_OP("\\<-"),
    BOOL("true|false"),
    COMMA("\\,"),
    DIGIT("0|([1-9][0-9]*)"),
    DIRECTION("\\:"),
    DIVISION_OP("\\/"),
    ELSE_KW("\\|"),
    EQUAL_LOGICAL_OP("\\=="),
    GO_FALSE("go_false"),
    GO_TRUE("go_true"),
    IF_KW("\\?"),
    LEFT_B("\\("),
    LEFT_BR("\\{"),
    LESS_LOGICAL_OP("\\<"),
    LESS_OR_EQUAL_LOGICAL_OP("\\<="),
    METHODS("add|remove|get|put|contains|size"),
    MORE_LOGICAL_OP("\\>"),
    MORE_OR_EQUAL_LOGICAL_OP("\\>="),
    MULTIPLICATION_OP("\\*"),
    OUTPUT_OP("print"),
    RIGHT_B("\\)"),
    RIGHT_BR("\\}"),
    SEMICOLON("\\."),
    SUBTRACTION_OP("\\-"),
    TYPE("int|double|string|bool|list|hashtable"),
    VAR("^[a-z]+"),
    WHILE_KW("\\!");

    private Pattern pattern;

    Lexem(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}