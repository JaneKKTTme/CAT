package token;

import lexer.Lexem;

public class Token {
    private Lexem lexem;
    private String value;

    public Token(Lexem lexem, String value) {
        this.lexem = lexem;
        this.value = value;
    }

    public Lexem getType() {
        return lexem;
    }

    public void setType(Lexem lexem) {
        this.lexem = lexem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int a = lexem.hashCode();
        final int b = value.hashCode();
        final int x = 31;

        return a * x + b;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Token other = (Token) obj;

        return lexem == other.lexem && value.equals(other.value);
    }

    public String toString() {
        return this.getType() + " : \"" + this.getValue() + "\"";
    }

    public static Token fromString(String str) {
        String[] typeAndValue = str.split("\\s+:\\s+");
        Lexem type = Lexem.valueOf(typeAndValue[0]);

        String value = typeAndValue[1];
        if (value.length() > 2) {
            value = value.substring(1, value.length() - 1);
        }
        else {
            value = "";
        }

        return new Token(type, value);
    }
}