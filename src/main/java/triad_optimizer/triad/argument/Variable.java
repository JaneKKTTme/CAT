package triad_optimizer.triad.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lexer.Lexem;
import token.Token;
import vartable.VarTable;

public class Variable extends TriadArgument {
    String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int getValue() {
        return (int) VarTable.getInstance().getValue(name);
    }

    @Override
    public List<Token> tokenize() {
        return new ArrayList<>(Arrays.asList(new Token(Lexem.VAR, name)));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
        Variable other = (Variable) obj;

        return name.equals(other.name);
    }
}
