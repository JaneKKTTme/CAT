package stack_machine;

import lexer.Lexem;
import token.Token;
import type.hash.CatHashTable;
import type.lists.CatDoublyLinkedList;
import vartable.VarTable;

import java.util.*;

public class StackMachine{
    private Token current_token;
    private final Stack<Token> stack = new Stack<>();
    private Integer tokenIndex;
    private final List<Token> tokens;
    private final VarTable varTable = new VarTable();

    public StackMachine(List<Token> tokens) {
        this.tokens = tokens;
        this.tokenIndex = -1;
        this.current_token = null;
    }

    public VarTable canculate() {
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
        varTable.add(second_element, Double.parseDouble(first_element));
    }

    public void doCanculate() {
        while (tokenIndex != tokens.size()) {
            if (current_token.getLexem() == Lexem.OUTPUT_OP) {
                doOutputOp();
            }
            else if (current_token.getLexem() == Lexem.TYPE) {
                Token f_element = stack.pop();
                String first_element = f_element.getValue();
                Object type = null;
                if (current_token.getValue().equals("int")) {
                    type = (Integer) 0;
                }
                if (current_token.getValue().equals("str")) {
                    type = (String) "";
                }
                if (current_token.getValue().equals("list")) {
                    type = new CatDoublyLinkedList<>();
                }
                if (current_token.getValue().equals("hashtable")) {
                    type = new CatHashTable<>();
                }
                varTable.add(first_element, current_token.getValue(), type);
                iterate();
            }
            else if (current_token.getLexem() == Lexem.METHODS) {
                doMethod(current_token.getValue());
            }
            else if (current_token.getLexem() == Lexem.DIGIT || current_token.getLexem() == Lexem.VAR) {
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
                        first_element = varTable.getValue(f_element.getValue()).toString();
                    }
                    if (s_element.getLexem() == Lexem.VAR) {
                        second_element = varTable.getValue(s_element.getValue()).toString();
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
                if (tmp.getLexem().equals(Lexem.VAR)) {
                    value = varTable.getValue(tmp.getValue()).toString();
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

    public Object doHashTableMethod(String method, CatHashTable<Object, Object> value, List<Object> variables) {
        if (method.equals("put")) {
            value.put(variables.get(1), variables.get(0));
        }
        else if (method.equals("remove")) {
            value.remove(variables.get(0));
        }
        else if (method.equals("get")) {
            System.out.println(value.get(variables.get(0)));
        }
        else if (method.equals("contains")) {
            value.contains(variables.get(0));
        }
        else if (method.equals("size")) {
            return value.size();
        }
        return value;
    }

    public Object doListMethod(String method, CatDoublyLinkedList value, List<Object> variables) {
        if (method.equals("add")) {
            value.addBack(variables.get(0));
        }
        else if (method.equals("remove")) {
            value.removeNodeByIndex(Integer.valueOf((String) variables.get(0)));
        }
        else if (method.equals("get")) {
            System.out.println(value.get(Integer.valueOf((String) variables.get(0))));
        }
        else if (method.equals("contains")) {
            value.contains(variables.get(0));
        }
        else if (method.equals("size")) {
            return value.size();
        }
        return (Object) value;
    }

    public Token doLogicalOp(Double first_element, Double second_element) {
        boolean logical_res = false;
        if (current_token.getLexem().equals(Lexem.MORE_LOGICAL_OP)) {
            logical_res = second_element > first_element;
        }
        else if (current_token.getLexem().equals(Lexem.LESS_LOGICAL_OP)) {
            logical_res = second_element < first_element;
        }
        else if (current_token.getLexem() == Lexem.MORE_OR_EQUAL_LOGICAL_OP) {
            logical_res = second_element >= first_element;
        }
        else if (current_token.getLexem().equals(Lexem.LESS_OR_EQUAL_LOGICAL_OP)) {
            logical_res = second_element <= first_element;
        }
        else if (current_token.getLexem().equals(Lexem.EQUAL_LOGICAL_OP)) {
            logical_res = second_element == first_element;
        }

        return new Token(Lexem.BOOL, Boolean.toString(logical_res));
    }

    public void doMethod(String method) {
        List<Object> variables = new ArrayList<>();
        if (!stack.isEmpty())
            while (!stack.peek().getLexem().equals(Lexem.VAR)) {
                variables.add(stack.pop().getValue());
            }
        Token t_element = stack.pop();
        String element = t_element.getValue();
        if (varTable.contains(element) && element != null) {
            if (varTable.getType(element).equals("list")) {
                Object value = varTable.getValue(element);
                value = doListMethod(method, (CatDoublyLinkedList) value, variables);
                if (value instanceof Integer) {
                    varTable.add("tmp", "int", (Integer) value);
                }
                else {
                    varTable.setValue(element, (CatDoublyLinkedList) value);
                }
            }
            else if (varTable.getType(element).equals("hashtable")) {
                CatHashTable<Object, Object> value = (CatHashTable<Object, Object>) varTable.getValue(element);
                value = (CatHashTable) doHashTableMethod(method, value, variables);
                varTable.setValue(element, value);
            }

        }
        else {
            throw new NoSuchElementException("Variable not found: " + element + ".");
        }
    }

    public void doOutputOp() {
        String element = "";
        if (!stack.isEmpty()) {
            Token t_element = stack.pop();
            element = t_element.getValue();
        }
        else {
            element = "tmp";
        }
        String output = "";
        if (varTable.contains(element)) {
            if (varTable.getType(element).equals("int")) {
                output += (Integer) varTable.getValue(element);
            }
            if (varTable.getType(element).equals("str")) {
                output += (String) varTable.getValue(element);
            }
            if (varTable.getType(element).equals("list")) {
                CatDoublyLinkedList value = (CatDoublyLinkedList) varTable.getValue(element);
                output += value.toString();
            }
            if (varTable.getType(element).equals("hashtable")) {
                CatHashTable<Object, Object> value = (CatHashTable) varTable.getValue(element);
                output += value.toString();
            }
        }
        else {
            output += element;
        }
        System.out.println(output);
    }

    private void iterate() {
        tokenIndex += 1;
        if (tokenIndex < tokens.size()) {
            current_token = tokens.get(tokenIndex);
        }
    }
}
