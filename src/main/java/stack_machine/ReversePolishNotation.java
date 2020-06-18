package stack_machine;

import lexer.Lexem;
import token.Token;

import java.util.*;

public class ReversePolishNotation {
    private Token current_token;
    private static Map<Lexem, Integer> lexemPriority = new HashMap<>();
    private final List<Token> result;
    private Integer tokenIndex;
    private final List<Token> tokens;

    public ReversePolishNotation(List<Token> tokens) {
        this.tokens = tokens;
        this.tokenIndex = -1;
        this.result = new ArrayList<>();
        this.current_token = null;
    }

    public List<Token> doTranslationForBody() {
        List<Token> body_result = new ArrayList<>();
        while (current_token.getLexem() != Lexem.RIGHT_BR) {
            body_result.addAll(doTranslationForExpressionOp(Lexem.SEMICOLON));
        }
        iterate();

        return body_result;
    }

    public List<Token> doTranslationForExpressionOp(Lexem lexem) {
        List<Token> expression_result = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        while (current_token.getLexem() != lexem) {
            if (current_token.getLexem() == Lexem.METHODS) {
                Token tmp = current_token;
                iterate();
                while (current_token.getLexem() != Lexem.RIGHT_B && tokens.get(tokenIndex+1).getLexem() != Lexem.SEMICOLON) {
                    if (current_token.getLexem().equals(Lexem.DIGIT) || current_token.getLexem().equals(Lexem.BOOL))  {
                        expression_result.add(current_token);
                    }
                    iterate();
                }
                expression_result.add(tmp);
            }
            else if (current_token.getLexem() == Lexem.DIGIT || current_token.getLexem() == Lexem.VAR ||
                    current_token.getLexem() == Lexem.BOOL || current_token.getLexem() == Lexem.TYPE) {
                expression_result.add(current_token);
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
                        expression_result.add(stack.pop());
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
                    expression_result.add(stack.pop());
                }
                stack.pop();
            }
            if (current_token.getLexem() != Lexem.SEMICOLON) {
                iterate();
            }
        }
        if (current_token.getLexem() == Lexem.SEMICOLON) {
            iterate();
        }
        while (!stack.isEmpty()) {
            expression_result.add(stack.pop());
        }

        return expression_result;
    }

    public List<Token> doTranslationForIf() {
        List<Token> if_result = new ArrayList<>();
        if_result.addAll(doTranslationForLogicalOp());
        if_result.addAll(doTranslationForBody());
        int false_pos = if_result.size() + result.size() + 1;
        for (int i = 0; i < if_result.size(); i++) {
            if (if_result.get(i).getLexem() == Lexem.GO_FALSE) {
                if_result.set(i, new Token(Lexem.GO_FALSE, String.valueOf(false_pos)));
            }
        }
        if_result.add(new Token(Lexem.GO_TRUE, "-1"));
        if (tokens.get(tokenIndex).getLexem() == Lexem.ELSE_KW) {
            if_result.addAll(doTranslationForBody());
            if_result.add(new Token(Lexem.IF_KW, "END"));
        }
        int true_pos = if_result.size() + result.size();
        for (int i = 0; i < if_result.size(); i++) {
            if (if_result.get(i).getLexem() == Lexem.GO_TRUE) {
                if_result.set(i, new Token(Lexem.GO_TRUE, String.valueOf(true_pos)));
            }
        }

        return if_result;
    }

    public List<Token> doTranslationForLogicalOp() {
        List<Token> logical_result = new ArrayList<>();
        iterate();
        iterate();
        logical_result.addAll(doTranslationForExpressionOp(Lexem.RIGHT_B));
        logical_result.add(new Token(Lexem.GO_FALSE, "-1"));

        return logical_result;
    }

    public List<Token> doTranslationForOutputOp() {
        List<Token> output_result = new ArrayList<>();
        Token tmp = current_token;
        while (current_token.getLexem() != Lexem.LEFT_B) {
            iterate();
        }
        output_result.addAll(doTranslationForExpressionOp(Lexem.SEMICOLON));
        output_result.add(tmp);
        return output_result;
    }

    public List<Token> doTranslationForWhile() {
        List<Token> while_result = new ArrayList<>();
        while_result.addAll(doTranslationForLogicalOp());
        while_result.addAll(doTranslationForBody());
        int false_pos = while_result.size() + result.size() + 1;
        for (int i = 0; i < while_result.size(); i++) {
            if (while_result.get(i).getLexem() == Lexem.GO_FALSE) {
                while_result.set(i, new Token(Lexem.GO_FALSE, String.valueOf(false_pos)));
            }
        }
        while_result.add(new Token(Lexem.GO_TRUE, "-1"));
        int true_pos = result.size();
        for (int i = 0; i < while_result.size(); i++) {
            if (while_result.get(i).getLexem() == Lexem.GO_TRUE) {
                while_result.set(i, new Token(Lexem.GO_TRUE, String.valueOf(true_pos)));
            }
        }
        while_result.add(new Token(Lexem.WHILE_KW, "END"));

        return while_result;
    }

    public List<Token> doTranslationToRPN() {
        while (tokenIndex < tokens.size()) {
            if (current_token.getLexem() == Lexem.IF_KW) {
                result.addAll(doTranslationForIf());
            }
            else if (current_token.getLexem() == Lexem.WHILE_KW) {
                result.addAll(doTranslationForWhile());
            }
            else if (current_token.getLexem() == Lexem.OUTPUT_OP) {
                result.addAll(doTranslationForOutputOp());
            }
            else {
                result.addAll(doTranslationForExpressionOp(Lexem.SEMICOLON));
            }
        }

        return result;
    }

    private void iterate() {
        tokenIndex += 1;
        if (tokenIndex < tokens.size()) {
            current_token = tokens.get(tokenIndex);
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
        lexemPriority.put(Lexem.METHODS, 1);
    }

    public List<Token> translate() {
        setLexemPriority();
        iterate();

        return doTranslationToRPN();
    }
}