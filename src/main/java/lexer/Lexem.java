package lexer;

import java.util.regex.Pattern;

public enum Lexem {
    TYPE("int|double|string"),
    ASSIGN_OP("\\->"),
    OUTPUT_OP("\\-->"),
    BOOL("TRUE|FALSE"),
    DIGIT("0|([1-9][0-9]*)"),
    ADDITION_OP("\\+"),
    SUBTRACTION_OP("\\-"),
    MULTIPLICATION_OP("\\*"),
    DIVISION_OP("\\/"),
    MORE_LOGICAL_OP("\\>"),
    LESS_LOGICAL_OP("\\<"),
    MORE_OR_EQUAL_LOGICAL_OP("\\>="),
    LESS_OR_EQUAL_LOGICAL_OP("\\<="),
    EQUAL_LOGICAL_OP("\\=="),
    LEFT_B("\\("),
    RIGHT_B("\\)"),
    LEFT_BR("\\{"),
    RIGHT_BR("\\}"),
    IF_KW("\\?"),
    ELSE_KW("\\|"),
    WHILE_KW("\\!"),
    SEMICOLON("\\."),
    VAR("^[a-z]+"),
    GO_TRUE(""),
    GO_FALSE("");

    private Pattern pattern;

    Lexem(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}