import junit.framework.TestCase;

import lexer.Lexem;
import token.Token;
import stack_machine.ReversePolishNotation;
import type.lists.CatDoublyLinkedList;

import java.util.*;

public class ReversePolishNotationTest extends TestCase {
    private final Map<List<Token>, List<Token>> translateData = new HashMap<>();

    protected void setData() {
        List<Token> tokens = new ArrayList<>();
        List<Token> rpn_tokens = new ArrayList<>();

        tokens.add(new Token(Lexem.DIGIT, "1"));
        tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        tokens.add(new Token(Lexem.VAR, "a"));
        tokens.add(new Token(Lexem.SEMICOLON, "."));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.add(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.add(new Token(Lexem.DIGIT, "10"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.add(new Token(Lexem.MULTIPLICATION_OP, "*"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.MORE_LOGICAL_OP, ">"));
        rpn_tokens.add(new Token(Lexem.GO_FALSE, "18"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.GO_TRUE, "24"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.IF_KW, "END"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();

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
        rpn_tokens.add(new Token(Lexem.DIGIT, "9"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.add(new Token(Lexem.MULTIPLICATION_OP, "*"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "20"));
        rpn_tokens.add(new Token(Lexem.LESS_LOGICAL_OP, "<"));
        rpn_tokens.add(new Token(Lexem.GO_FALSE, "18"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.GO_TRUE, "8"));
        rpn_tokens.add(new Token(Lexem.WHILE_KW, "END"));
        translateData.put(tokens, rpn_tokens);

        translateData.clear();
    }

    public void testTranslate() {
        setData();
        for (Iterator iterator = translateData.keySet().iterator(); iterator.hasNext();) {
            ReversePolishNotation rpn = new ReversePolishNotation((List<Token>) iterator.next());
            List<Token> actual = rpn.translate();
            assertEquals(translateData.get(iterator), actual);
        }
    }
}
