package stack_machine;

import lexer.Lexem;
import token.Token;
import type.hash.CatHashTable;
import type.lists.CatDoublyLinkedList;

import java.util.*;

public class StackMachine{
    private Token current_token;
    private final Stack<Token> stack = new Stack<>();
    private Integer tokenIndex;
    private final CatDoublyLinkedList<Token> tokens;
    private final CatHashTable<String, Double> varTable = new CatHashTable<>();

    public StackMachine(CatDoublyLinkedList<Token> tokens) {
        this.tokens = tokens;
        this.tokenIndex = -1;
        this.current_token = null;
    }

    public CatHashTable<String, Double> canculate() {
        iterate();
        doCanculate();

        return varTable;
    }

    public Token doArithmOp(Double first_element, Double second_element) {
        Double arithm_res = 0.0;
        if (current_token.getLexem() == Lexem.ADDITION_OP) {
            arithm_res = second_element + first_element;
        }
        else if (current_token.getLexem() == Lexem.SUBTRACTION_OP) {
            arithm_res = second_element - first_element;
        }
        else if (current_token.getLexem() == Lexem.MULTIPLICATION_OP) {
            arithm_res = second_element * first_element;
        }
        else if (current_token.getLexem() == Lexem.DIVISION_OP) {
            arithm_res = second_element / first_element;
        }

        return new Token(Lexem.DIGIT, arithm_res.toString());
    }

    public void doAssignOp(String first_element, String second_element) {
        varTable.put(first_element, Double.parseDouble(second_element));
    }

    public void doCanculate() {
        while (tokenIndex != tokens.size()) {
            if (current_token.getLexem() == Lexem.DIGIT || current_token.getLexem() == Lexem.VAR ||
                    current_token.getLexem() == Lexem.BOOL) {
                stack.push(current_token);
            } else if (current_token.getLexem() == Lexem.ADDITION_OP || current_token.getLexem() == Lexem.SUBTRACTION_OP ||
                    current_token.getLexem() == Lexem.MULTIPLICATION_OP || current_token.getLexem() == Lexem.DIVISION_OP ||
                    current_token.getLexem() == Lexem.ASSIGN_OP || current_token.getLexem() == Lexem.MORE_LOGICAL_OP ||
                    current_token.getLexem() == Lexem.LESS_LOGICAL_OP || current_token.getLexem() == Lexem.MORE_OR_EQUAL_LOGICAL_OP ||
                    current_token.getLexem() == Lexem.LESS_OR_EQUAL_LOGICAL_OP || current_token.getLexem() == Lexem.EQUAL_LOGICAL_OP) {
                Token op = current_token;
                Token f_element = stack.pop();
                String first_element = f_element.getValue();
                Token s_element = stack.pop();
                String second_element = s_element.getValue();
                if (op.getLexem() == Lexem.ASSIGN_OP) {
                    doAssignOp(first_element, second_element);
                } else {
                    if (f_element.getLexem() == Lexem.VAR) {
                        first_element = varTable.get(f_element.getValue()).toString();
                    }
                    if (s_element.getLexem() == Lexem.VAR) {
                        second_element = varTable.get(s_element.getValue()).toString();
                    }
                    if (op.getLexem() == Lexem.ADDITION_OP || op.getLexem() == Lexem.SUBTRACTION_OP ||
                            op.getLexem() == Lexem.MULTIPLICATION_OP || op.getLexem() == Lexem.DIVISION_OP) {
                        stack.push(doArithmOp(Double.parseDouble(first_element), Double.parseDouble(second_element)));
                    }
                    if (op.getLexem() == Lexem.MORE_LOGICAL_OP || op.getLexem() == Lexem.LESS_LOGICAL_OP ||
                            op.getLexem() == Lexem.MORE_OR_EQUAL_LOGICAL_OP || op.getLexem() == Lexem.LESS_OR_EQUAL_LOGICAL_OP ||
                            op.getLexem() == Lexem.EQUAL_LOGICAL_OP) {
                        stack.push(doLogicalOp(Double.parseDouble(first_element), Double.parseDouble(second_element)));
                    }
                }
            }
            else if (current_token.getLexem() == Lexem.GO_FALSE) {
                Token tmp = stack.pop();
                String value;
                if (tmp.getLexem() == Lexem.VAR) {
                    value = varTable.get(tmp.getValue()).toString();
                }
                else {
                    value = tmp.getValue();
                }
                if (!Boolean.parseBoolean(value)) {
                    tokenIndex = Integer.parseInt(current_token.getValue()) - 1;
                }
            }
            else if (current_token.getLexem() == Lexem.GO_TRUE) {
                tokenIndex = Integer.parseInt(current_token.getValue()) - 1;
            }
            iterate();
        }
    }

    public Token doLogicalOp(Double first_element, Double second_element) {
        boolean logical_res = false;
        if (current_token.getLexem() == Lexem.MORE_LOGICAL_OP) {
            logical_res = second_element > first_element;
        }
        else if (current_token.getLexem() == Lexem.LESS_LOGICAL_OP) {
            logical_res = second_element < first_element;
        }
        else if (current_token.getLexem() == Lexem.MORE_OR_EQUAL_LOGICAL_OP) {
            logical_res = second_element >= first_element;
        }
        else if (current_token.getLexem() == Lexem.LESS_OR_EQUAL_LOGICAL_OP) {
            logical_res = second_element <= first_element;
        }
        else if (current_token.getLexem() == Lexem.EQUAL_LOGICAL_OP) {
            logical_res = second_element == first_element;
        }

        return new Token(Lexem.BOOL, Boolean.toString(logical_res));
    }

    private void iterate() {
        tokenIndex += 1;
        if (tokenIndex < tokens.size()) {
            current_token = (Token) tokens.get(tokenIndex);
        }
    }
}
