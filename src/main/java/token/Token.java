package token;

import lexer.Lexem;

public class Token {
    private Lexem lexem;
    private String value;

    public Token(Lexem lexem, String value) {
        this.lexem = lexem;
        this.value = value;
    }

    public Lexem getLexem() {
        return lexem;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + this.getLexem() + ": \"" + this.getValue() + '\"' + ']';
    }
}