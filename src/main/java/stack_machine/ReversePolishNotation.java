package stack_machine;

import lexer.Lexem;
import token.Token;
import type.lists.CatDoublyLinkedList;

import java.util.*;

public class ReversePolishNotation {
    private Token current_token;
    private static Map<Lexem, Integer> lexemPriority = new HashMap<>();
    private CatDoublyLinkedList<Token> result;
    private Integer tokenIndex;
    private final CatDoublyLinkedList<Token> tokens;

    public ReversePolishNotation(CatDoublyLinkedList<Token> tokens) {
        this.tokens = tokens;
        this.tokenIndex = -1;
        this.result = new CatDoublyLinkedList<Token>();
        this.current_token = null;
    }

    public CatDoublyLinkedList<Token> doTranslationForBody() {
        CatDoublyLinkedList<Token> body_result = new CatDoublyLinkedList<>();
        while (current_token.getLexem() != Lexem.RIGHT_BR) {
            CatDoublyLinkedList tmp = doTranslationForExpressionOp(Lexem.SEMICOLON);
            for (int i = 0; i < tmp.size(); i++) {
                body_result.addBack((Token) tmp.get(i));
            }
        }
        iterate();

        return body_result;
    }

    public CatDoublyLinkedList<Token> doTranslationForExpressionOp(Lexem lexem) {
        CatDoublyLinkedList<Token> expression_result = new CatDoublyLinkedList<>();
        Stack<Token> stack = new Stack<>();
        while (current_token.getLexem() != lexem) {
            if (current_token.getLexem() == Lexem.DIGIT || current_token.getLexem() == Lexem.VAR ||
                    current_token.getLexem() == Lexem.BOOL) {
                expression_result.addBack(current_token);
            }
            else if (current_token.getLexem() == Lexem.ADDITION_OP || current_token.getLexem() == Lexem.SUBTRACTION_OP ||
                    current_token.getLexem() == Lexem.MULTIPLICATION_OP || current_token.getLexem() == Lexem.DIVISION_OP ||
                    current_token.getLexem() == Lexem.ASSIGN_OP || current_token.getLexem() == Lexem.MORE_LOGICAL_OP ||
                    current_token.getLexem() == Lexem.LESS_LOGICAL_OP || current_token.getLexem() == Lexem.MORE_OR_EQUAL_LOGICAL_OP ||
                    current_token.getLexem() == Lexem.LESS_OR_EQUAL_LOGICAL_OP || current_token.getLexem() == Lexem.EQUAL_LOGICAL_OP) {
                if (stack.isEmpty()) {
                    stack.push(current_token);
                }
                else {
                    Integer stack_pr = lexemPriority.get(stack.peek().getLexem());
                    Integer current_pr = lexemPriority.get(current_token.getLexem());
                    while (stack_pr >= current_pr) {
                        expression_result.addBack(stack.pop());
                        if (stack.isEmpty()) {
                            break;
                        }
                        else {
                            stack_pr = lexemPriority.get(stack.peek().getLexem());
                        }
                    }
                    stack.push(current_token);
                }
            }
            else if (current_token.getLexem() == Lexem.LEFT_B) {
                stack.push(current_token);
            }
            else if (current_token.getLexem() == Lexem.RIGHT_B) {
                while (stack.peek().getLexem() != Lexem.LEFT_B) {
                    expression_result.addBack(stack.pop());
                }
                stack.pop();
            }
            iterate();
        }
        iterate();
        while (!stack.isEmpty()) {
            expression_result.addBack(stack.pop());
        }

        return expression_result;
    }

    public CatDoublyLinkedList<Token> doTranslationForIf() {
        CatDoublyLinkedList<Token> if_result = new CatDoublyLinkedList<>();
        CatDoublyLinkedList tmp = doTranslationForLogicalOp();
        for (int i = 0; i < tmp.size(); i++) {
            if_result.addBack((Token) tmp.get(i));
        }
        tmp = doTranslationForBody();
        for (int i = 0; i < tmp.size(); i++) {
            if_result.addBack((Token) tmp.get(i));
        }
        int false_pos = if_result.size() + result.size() + 1;
        for (int i = 0; i < if_result.size(); i++) {
            if (((Token) if_result.get(i)).getLexem() == Lexem.GO_FALSE) {
                if_result.set(i, new Token(Lexem.GO_FALSE, String.valueOf(false_pos)));
            }
        }
        if_result.addBack(new Token(Lexem.GO_TRUE, "-1"));
        if (((Token) tokens.get(tokenIndex)).getLexem() == Lexem.ELSE_KW) {
            tmp = doTranslationForBody();
            for (int i = 0; i < tmp.size(); i++) {
                if_result.addBack((Token) tmp.get(i));
            }
            if_result.addBack(new Token(Lexem.IF_KW, "END"));
        }
        int true_pos = if_result.size() + result.size();
        for (int i = 0; i < if_result.size(); i++) {
            if (((Token) if_result.get(i)).getLexem() == Lexem.GO_TRUE) {
                if_result.set(i, new Token(Lexem.GO_TRUE, String.valueOf(true_pos)));
            }
        }

        return if_result;
    }

    public CatDoublyLinkedList<Token> doTranslationForLogicalOp() {
        CatDoublyLinkedList<Token> logical_result = new CatDoublyLinkedList<>();
        iterate();
        iterate();
        CatDoublyLinkedList tmp = doTranslationForExpressionOp(Lexem.RIGHT_B);
        for (int i = 0; i < tmp.size(); i++) {
            logical_result.addBack((Token) tmp.get(i));
        }
        logical_result.addBack(new Token(Lexem.GO_FALSE, "-1"));

        return logical_result;
    }

    public CatDoublyLinkedList<Token> doTranslationForWhile() {
        CatDoublyLinkedList<Token> while_result = new CatDoublyLinkedList<>();
        CatDoublyLinkedList tmp = doTranslationForLogicalOp();
        for (int i = 0; i < tmp.size(); i++) {
            while_result.addBack((Token) tmp.get(i));
        }
        tmp = doTranslationForBody();
        for (int i = 0; i < tmp.size(); i++) {
            while_result.addBack((Token) tmp.get(i));
        }
        int false_pos = while_result.size() + result.size() + 1;
        for (int i = 0; i < while_result.size(); i++) {
            if (((Token) while_result.get(i)).getLexem() == Lexem.GO_FALSE) {
                while_result.set(i, new Token(Lexem.GO_FALSE, String.valueOf(false_pos)));
            }
        }
        while_result.addBack(new Token(Lexem.GO_TRUE, "-1"));
        int true_pos = result.size();
        for (int i = 0; i < while_result.size(); i++) {
            if (((Token) while_result.get(i)).getLexem() == Lexem.GO_TRUE) {
                while_result.set(i, new Token(Lexem.GO_TRUE, String.valueOf(true_pos)));
            }
        }
        while_result.addBack(new Token(Lexem.WHILE_KW, "END"));

        return while_result;
    }

    public CatDoublyLinkedList<Token> doTranslationToRPN() {
        while (tokenIndex < tokens.size()) {
            if (current_token.getLexem() == Lexem.IF_KW) {
                CatDoublyLinkedList tmp = doTranslationForIf();
                for (int i = 0; i < tmp.size(); i++) {
                    result.addBack((Token) tmp.get(i));
                }
            }
            else if (current_token.getLexem() == Lexem.WHILE_KW) {
                CatDoublyLinkedList tmp = doTranslationForWhile();
                for (int i = 0; i < tmp.size(); i++) {
                    result.addBack((Token) tmp.get(i));
                }
            }
            else {
                CatDoublyLinkedList tmp = doTranslationForExpressionOp(Lexem.SEMICOLON);
                for (int i = 0; i < tmp.size(); i++) {
                    result.addBack((Token) tmp.get(i));
                }
            }
        }

        return result;
    }

    private void iterate() {
        tokenIndex += 1;
        if (tokenIndex < tokens.size()) {
            current_token = (Token) tokens.get(tokenIndex);
        }
    }

    private static void setLexemPriority() {
        lexemPriority.put(Lexem.ASSIGN_OP, 0);
        lexemPriority.put(Lexem.LEFT_B, 0);
        lexemPriority.put(Lexem.RIGHT_B, 0);
        lexemPriority.put(Lexem.OUTPUT_OP, 0);
        lexemPriority.put(Lexem.MORE_LOGICAL_OP, 1);
        lexemPriority.put(Lexem.LESS_LOGICAL_OP, 1);
        lexemPriority.put(Lexem.MORE_OR_EQUAL_LOGICAL_OP, 1);
        lexemPriority.put(Lexem.LESS_OR_EQUAL_LOGICAL_OP, 1);
        lexemPriority.put(Lexem.EQUAL_LOGICAL_OP, 2);
        lexemPriority.put(Lexem.ADDITION_OP, 1);
        lexemPriority.put(Lexem.SUBTRACTION_OP, 1);
        lexemPriority.put(Lexem.MULTIPLICATION_OP, 2);
        lexemPriority.put(Lexem.DIVISION_OP, 2);
    }

    public CatDoublyLinkedList<Token> translate() {
        setLexemPriority();
        iterate();

        return doTranslationToRPN();
    }
}