import junit.framework.TestCase;

import lexer.Lexem;
import token.Token;
import stack_machine.ReversePolishNotation;
import type.lists.CatDoublyLinkedList;

import java.util.*;

public class ReversePolishNotationTest extends TestCase {
    private final Map<CatDoublyLinkedList<Token>, CatDoublyLinkedList<Token>> translateData = new HashMap<>();

    protected void setData() {
        CatDoublyLinkedList<Token> tokens = new CatDoublyLinkedList<>();
        CatDoublyLinkedList<Token> rpn_tokens = new CatDoublyLinkedList<>();

        tokens.addBack(new Token(Lexem.DIGIT, "1"));
        tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.addBack(new Token(Lexem.VAR, "a"));
        tokens.addBack(new Token(Lexem.SEMICOLON, "."));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "10"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.addBack(new Token(Lexem.MULTIPLICATION_OP, "*"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.MORE_LOGICAL_OP, ">"));
        rpn_tokens.addBack(new Token(Lexem.GO_FALSE, "18"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "a"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.GO_TRUE, "24"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.IF_KW, "END"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "9"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.addBack(new Token(Lexem.MULTIPLICATION_OP, "*"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "20"));
        rpn_tokens.addBack(new Token(Lexem.LESS_LOGICAL_OP, "<"));
        rpn_tokens.addBack(new Token(Lexem.GO_FALSE, "18"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.addBack(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.addBack(new Token(Lexem.VAR, "b"));
        rpn_tokens.addBack(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.addBack(new Token(Lexem.GO_TRUE, "8"));
        rpn_tokens.addBack(new Token(Lexem.WHILE_KW, "END"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();
    }

    public void testTranslate() {
        setData();
        for (Iterator iterator = translateData.keySet().iterator(); iterator.hasNext();) {
            ReversePolishNotation rpn = new ReversePolishNotation((CatDoublyLinkedList<Token>) iterator.next());
            CatDoublyLinkedList<Token> actual = rpn.translate();
            assertEquals(translateData.get(iterator), actual);
        }
    }
}
