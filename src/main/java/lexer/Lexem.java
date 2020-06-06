package lexer;

import java.util.regex.Pattern;

public enum Lexem {
    ADDITION_OP("\\+"),
    ASSIGN_OP("\\->"),
    BOOL("TRUE|FALSE"),
    DIGIT("0|([1-9][0-9]*)"),
    DIVISION_OP("\\/"),
    ELSE_KW("\\|"),
    EQUAL_LOGICAL_OP("\\=="),
    GO_FALSE(""),
    GO_TRUE(""),
    IF_KW("\\?"),
    LEFT_B("\\("),
    LEFT_BR("\\{"),
    LESS_LOGICAL_OP("\\<"),
    LESS_OR_EQUAL_LOGICAL_OP("\\<="),
    MORE_LOGICAL_OP("\\>"),
    MORE_OR_EQUAL_LOGICAL_OP("\\>="),
    MULTIPLICATION_OP("\\*"),
    OUTPUT_OP("\\-->"),
    RIGHT_B("\\)"),
    RIGHT_BR("\\}"),
    SEMICOLON("\\."),
    SUBTRACTION_OP("\\-"),
    TYPE("int|double|string"),
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