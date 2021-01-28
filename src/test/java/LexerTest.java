import junit.framework.TestCase;

import lexer.Lexem;
import lexer.Lexer;
import token.Token;
import types.lists.CatDoublyLinkedList;

import java.util.*;

public class LexerTest extends TestCase {
    private final Map<String, List<Token>> getTokensData = new HashMap<>();

    protected void setData() {
        List<Token> tokens = new ArrayList<>();

        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("1->a.", tokens);

        getTokensData.clear();

        tokens.add(new Token(Lexem.DIGIT, "2"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("2->a.\n1+a->b.", tokens);

        getTokensData.clear();

        tokens.add(new Token(Lexem.DIGIT, "10"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.MULTIPLICATION_OP, "*"));
        tokens.add(new Token(Lexem.DIGIT, "2"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.IF_KW, "?"));
        tokens.add(new Token(Lexem.LEFT_B, "("));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.MORE_LOGICAL_OP, ">"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.RIGHT_B, ")"));
        tokens.add(new Token(Lexem.LEFT_BR, "{"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.RIGHT_BR, "}"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.ELSE_KW, "|"));
        tokens.add(new Token(Lexem.LEFT_BR, "{"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.RIGHT_BR, "}"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("10->a.\na*2->b.\n? (b>a) {a+1->a.} | { b+1->b. }.", tokens);

        getTokensData.clear();

        tokens.add(new Token(Lexem.DIGIT, "9"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.MULTIPLICATION_OP, "*"));
        tokens.add(new Token(Lexem.DIGIT, "2"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.WHILE_KW, "!"));
        tokens.add(new Token(Lexem.LEFT_B, "("));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.LESS_LOGICAL_OP, "<"));
        tokens.add(new Token(Lexem.DIGIT, "20"));
        tokens.add(new Token(Lexem.RIGHT_B, ")"));
        tokens.add(new Token(Lexem.LEFT_BR, "{"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "b"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        tokens.add(new Token(Lexem.RIGHT_BR, "}"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("9->b.\nb*2->b.\n! (b<20) {b+1->b.}.", tokens);

        getTokensData.clear();
    }

    public void testGetTokens() {
        setData();
        for (Iterator iterator = getTokensData.keySet().iterator(); iterator.hasNext();) {
            Lexer lexer = new Lexer(iterator.next().toString());
            List<Token> actual = null;
            try {
                actual = lexer.getTokens();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assertEquals(getTokensData.get(iterator), actual);
        }
    }
}
