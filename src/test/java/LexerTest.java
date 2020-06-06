import junit.framework.TestCase;

import lexer.Lexem;
import lexer.Lexer;
import token.Token;
import type.lists.CatDoublyLinkedList;

import java.util.*;

public class LexerTest extends TestCase {
    private final Map<String, CatDoublyLinkedList<Token>> getTokensData = new HashMap<>();

    protected void setData() {
        CatDoublyLinkedList<Token> tokens = new CatDoublyLinkedList<>();

        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("1->a.", tokens);

        getTokensData.clear();

        tokens.addBack(new Token(Lexem.DIGIT, "2"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("2->a.\n1+a->b.", tokens);

        getTokensData.clear();

        tokens.addBack(new Token(Lexem.DIGIT, "10"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.MULTIPLICATION_OP, "*"));
        tokens.addBack(new Token(Lexem.DIGIT, "2"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.IF_KW, "?"));
        tokens.addBack(new Token(Lexem.LEFT_B, "("));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.MORE_LOGICAL_OP, ">"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.RIGHT_B, ")"));
        tokens.addBack(new Token(Lexem.LEFT_BR, "{"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.RIGHT_BR, "}"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.ELSE_KW, "|"));
        tokens.addBack(new Token(Lexem.LEFT_BR, "{"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.RIGHT_BR, "}"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("10->a.\na*2->b.\n? (b>a) {a+1->a.} | { b+1->b. }.", tokens);

        getTokensData.clear();

        tokens.addBack(new Token(Lexem.DIGIT, "9"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.MULTIPLICATION_OP, "*"));
        tokens.addBack(new Token(Lexem.DIGIT, "2"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.WHILE_KW, "!"));
        tokens.addBack(new Token(Lexem.LEFT_B, "("));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.LESS_LOGICAL_OP, "<"));
        tokens.addBack(new Token(Lexem.DIGIT, "20"));
        tokens.addBack(new Token(Lexem.RIGHT_B, ")"));
        tokens.addBack(new Token(Lexem.LEFT_BR, "{"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "b"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        tokens.addBack(new Token(Lexem.RIGHT_BR, "}"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        getTokensData.put("9->b.\nb*2->b.\n! (b<20) {b+1->b.}.", tokens);

        getTokensData.clear();
    }

    public void testGetTokens() {
        setData();
        for (Iterator iterator = getTokensData.keySet().iterator(); iterator.hasNext();) {
            Lexer lexer = new Lexer(iterator.next().toString());
            CatDoublyLinkedList<Token> actual = null;
            try {
                actual = lexer.getTokens();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assertEquals(getTokensData.get(iterator), actual);
        }
    }
}
