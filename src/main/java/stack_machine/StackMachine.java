package stack_machine;

import lexer.Lexem;
import token.Token;
import vartable.VarTable;
import type_table.Method;
import type_table.TypeTable;
import types.hash.CatHashTable;
import types.lists.CatDoublyLinkedList;
import exception.ExecuteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class StackMachine {
    public enum State {
        NORMAL,
        END,
        FUNCTION_CALL,
        FUNCTION_EXECUTION,
        FUNCTION_END,
        RETURN_CALL,
        NEW_THREAD_CALL;
    }

    public static class Context {
        public List<Token> rpnList;
        public int pos;
        public Stack<Token> stack;
        public boolean newLine;
        public Map<String, VarTable.VarData> varTableData;

        public Context() {
            this.pos = 0;
            this.newLine = true;
            this.stack = new Stack<>();
            this.rpnList = new ArrayList<>();
            this.varTableData = VarTable.getInstance().getData();
        }
    }

    private Context context;
    private State state;

    public StackMachine(List<Token> rpnList) {
        this.context = new Context();
        this.context.rpnList = rpnList;
        this.state = State.NORMAL;
    }

    public Context getContext() {
        this.context.varTableData = VarTable.getInstance().getData();
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
        VarTable.getInstance().setData(this.context.varTableData);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void execute() throws ExecuteException {
        execute(context.rpnList.size());
    }

    public void execute(int n) throws ExecuteException {
        for (int i = 0;
             (i < n) && (context.pos < context.rpnList.size());
             ++i, ++context.pos) {
            Token curToken = context.rpnList.get(context.pos);
            Lexem curType = curToken.getType();
            String curValue = curToken.getValue();

            if (curType == Lexem.FUNCTION) {
                // If next is new thread call
                if ((context.pos != context.rpnList.size() - 1) &&
                        (context.rpnList.get(context.pos + 1).getType() == Lexem.NEW_THREAD)) {
                    continue;
                }

                state = State.FUNCTION_CALL;
                return;
            }
            if (curType == Lexem.NEW_THREAD) {
                state = State.NEW_THREAD_CALL;
                return;
            }
            if (curType == Lexem.RETURN_KW) {
                state = State.RETURN_CALL;
                return;
            }

            if (curType == Lexem.VAR ||
                    curType == Lexem.DIGIT ||
                    curType == Lexem.TYPE) {
                context.stack.push(curToken);
            }
            else {
                switch (curType) {
                    case ASSIGN_OP:
                        assign();
                        break;
                    case METHODS:
                        method(curValue);
                        break;
                    case ADDITION_OP:
                        doAdditionalOperation();
                        break;
                    case SUBTRACTION_OP:
                        doSubtractionOperation();
                        break;
                    case MULTIPLICATION_OP:
                        doMultiplicationOperation();
                        break;
                    case DIVISION_OP:
                        doDivisionOperation();
                        break;
                    case MORE_LOGICAL_OP:
                        greaterThan();
                        break;
                    case LESS_LOGICAL_OP:
                        lessThan();
                        break;
                    case EQUAL_LOGICAL_OP:
                        equal();
                        break;
                    case MORE_OR_EQUAL_LOGICAL_OP:
                        greaterOrEqual();
                        break;
                    case LESS_OR_EQUAL_LOGICAL_OP:
                        lessOrEqual();
                        break;
                    case OUTPUT_OP:
                        output();
                        break;
                    case OUTPUT_NEWLINE:
                        context.newLine = true;
                        break;
                    case FALSE_TRANSITION:
                        falseTransition();
                        break;
                    case UNCONDITIONAL_TRANSITION:
                        unconditionalTransition();
                        break;
                    default:
                        throw new ExecuteException("Unexpected token " + curType + ": " +
                                curValue + " during execution");
                }
            }
        }

        if ((context.pos == context.rpnList.size()) &&
                (state == State.FUNCTION_EXECUTION)) {
            state = State.FUNCTION_END;
        }
        else if ((context.pos == context.rpnList.size()) &&
                (state == State.NORMAL)) {
            state = State.END;
        }
    }

    private void checkForVarOfDigit(Token token) throws ExecuteException {
        if (token.getType() != Lexem.VAR &&
                token.getType() != Lexem.DIGIT) {
            throw new ExecuteException("Expected variable or digit, but got " +
                    token.getType() + ": " + token.getValue());
        }
    }

    private void checkForVar(Token token) throws ExecuteException {
        if (token.getType() != Lexem.VAR) {
            throw new ExecuteException("Expected variable, but got " +
                    token.getType() + ": " +
                    token.getValue());
        }
    }

    private void assign() throws ExecuteException {
        Token value = context.stack.pop();
        Token variable = context.stack.pop();

        Object realValue = null;
        String type = null;

        checkForVar(variable);

        if (value.getType() == Lexem.VAR) {
            type = VarTable.getInstance().getType(value.getValue());
            realValue = VarTable.getInstance().getValue(value.getValue());
        }
        else if (value.getType() == Lexem.TYPE) {
            if (value.getValue().equals("int")) {
                realValue = (Integer) 0;
            }
            if (value.getValue().equals("str")) {
                realValue = (String) "";
            }
            if (value.getValue().equals("list")) {
                realValue = new CatDoublyLinkedList<>();
            }
            if (value.getValue().equals("hashtable")) {
                realValue = new CatHashTable();
            }
            type = value.getValue();
        }
        else if (value.getType() == Lexem.DIGIT) {
            type = "int";
            realValue = Integer.parseInt(value.getValue());
        }

        VarTable.getInstance().add(variable.getValue(), type, realValue);
    }

    private Method findMethod(String name, List<Method> methods) {
        Method m = null;
        for (Method methodItem : methods) {
            if (methodItem.getName().equals(name)) {
                m = methodItem;
                break;
            }
        }

        return m;
    }

    private void wrongArgType(String name, String varType, String paramType,
                              String argType) throws ExecuteException {
        throw new ExecuteException("Method " + name + " of type " + varType +
                " accepts argument of type " + paramType +
                ", but got " + argType);
    }

    private void method(String name) throws ExecuteException {
        Token variable = context.stack.pop();
        checkForVar(variable);
        String varType = VarTable.getInstance().getType(variable.getValue());

        Method realMethod = findMethod(name, TypeTable.getInstance().get(varType));

        List<Object> args = new ArrayList<>();
        List<String> paramTypes = realMethod.getParamTypes();
        Collections.reverse(paramTypes);

        for (String paramType : paramTypes) {
            Token arg = context.stack.pop();
            String argType = null;
            Object value = null;

            if (arg.getType() == Lexem.VAR) {
                argType = VarTable.getInstance().getType(arg.getValue());
                value = VarTable.getInstance().getValue(arg.getValue());
            }
            else {
                if (arg.getType() == Lexem.DIGIT) {
                    argType = "int";
                    value = Integer.parseInt(arg.getValue());
                }
            }

            if (!paramType.equals("Object") && !argType.equals(paramType)) {
                wrongArgType(name, varType, paramType, argType);
            }

            args.add(value);
        }
        Collections.reverse(args);

        Object res = realMethod.invoke(VarTable.getInstance().getValue(variable.getValue()), args);

        String returnType = realMethod.getReturnType();
        if (returnType.equals("Object")) {
            if (res.getClass() == Integer.class) {
                returnType = "int";
            }
            else if (res.getClass() == CatDoublyLinkedList.class) {
                returnType = "list";
            }
            else if (res.getClass() == CatHashTable.class) {
                returnType = "hashtable";
            }
        }
        if (returnType.equals("int")) {
            context.stack.push(new Token(Lexem.DIGIT, Integer.toString((Integer) res)));
        }
        if (returnType.equals("list")) {
            context.stack.push(new Token(Lexem.DIGIT, ((CatDoublyLinkedList) res).toString()));
        }
        if (returnType.equals("hashtable")) {
            context.stack.push(new Token(Lexem.DIGIT, ((CatHashTable) res).toString()));
        }
    }

    private int tokenToInt(Token token) throws ExecuteException {
        checkForVarOfDigit(token);

        int res;
        if (token.getType() == Lexem.VAR) {
            try {
                res = (Integer) VarTable.getInstance().getValue(token.getValue());
            }
            catch (NullPointerException e) {
                throw new ExecuteException("Variable " + token.getValue() + " wasn't defined");
            }
        }
        else {
            res = Integer.parseInt(token.getValue());
        }

        return res;
    }

    private void doAdditionalOperation() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue +
                rhsValue)));
    }

    private void doSubtractionOperation() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue -
                rhsValue)));
    }

    private void doMultiplicationOperation() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue *
                rhsValue)));
    }

    private void doDivisionOperation() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue /
                rhsValue)));
    }

    private void greaterThan() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue >
                rhsValue ? 1 : 0)));
    }

    private void lessThan() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue <
                rhsValue ? 1 : 0)));
    }

    private void greaterOrEqual() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue >=
                rhsValue ? 1 : 0)));
    }

    private void lessOrEqual() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue <=
                rhsValue ? 1 : 0)));
    }

    private void equal() throws ExecuteException {
        int rhsValue = tokenToInt(context.stack.pop());
        int lhsValue = tokenToInt(context.stack.pop());

        context.stack.push(new Token(Lexem.DIGIT, Integer.toString(lhsValue ==
                rhsValue ? 1 : 0)));
    }

    private void input() throws ExecuteException {
        Token token = context.stack.pop();
        checkForVar(token);
        Scanner scanner = new Scanner(System.in);

        String str = scanner.nextLine();
        this.context.newLine = false;
        String type = "int";
        try {
            Integer.parseInt(str);
        }
        catch (NumberFormatException ex) {
            type = "str";
        }

        if (type.equals("int")) {
            VarTable.getInstance().add(token.getValue(), "int", Integer.parseInt(str));
        }
        else if (type.equals("str")) {
            VarTable.getInstance().add(token.getValue(), "str", str);
        }
    }

    private void output() throws ExecuteException {
        Token token = context.stack.pop();
        String str = "";
        if (token.getType() == Lexem.VAR) {
            String type = VarTable.getInstance().getType(token.getValue());
            Object value = VarTable.getInstance().getValue(token.getValue());
            if (type.equals("int")) {
                str = Integer.toString((Integer) value);
            }
            if (type.equals("double")) {
                str = (String) value;
            }
            if (type.equals("list")) {
                str = ((CatDoublyLinkedList) value).toString();
            }
            if (type.equals("hashtable")) {
                str = ((CatHashTable) value).toString();
            }
        }
        else if (token.getType() == Lexem.DIGIT) {
            str = token.getValue();
        }
        else {
            throw new ExecuteException("Expected variable or digit but got " +
                    token.getType() + ": " +
                    token.getValue());
        }
        System.out.print(str + "\n");

        context.newLine = false;
    }

    private void falseTransition() throws ExecuteException {
        Token pointer = context.stack.pop();
        Token condition = context.stack.pop();

        checkForVar(pointer);

        int conditionValue = tokenToInt(condition);
        if (conditionValue <= 0) {
            // -1 because where is ++context.pos in cycle
            context.pos = tokenToInt(pointer) - 1;
        }
    }

    private void unconditionalTransition() throws ExecuteException {
        Token pointer = context.stack.pop();
        if (pointer.getType() != Lexem.VAR) {
            throw new ExecuteException("Expected variable, but got " +
                    pointer.getType() + ": " +
                    pointer.getValue());
        }

        // -1 because where is ++context.pos in cycle
        context.pos = tokenToInt(pointer) - 1;
    }
}
