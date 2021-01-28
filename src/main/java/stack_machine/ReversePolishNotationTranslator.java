package stack_machine;

import function_table.Function;
import function_table.FunctionTable;
import lexer.Lexem;
import token.Token;
import vartable.VarTable;
import vartable.VarTable.VarData;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ReversePolishNotationTranslator {
    private List<Token> tokens;

    private void fromStackToList(Stack<Token> stack, List<Token> list) {
        while (!stack.empty()) {
            list.add(stack.pop());
        }
    }

    public ReversePolishNotationTranslator(List<Token> tokens) {
        this.tokens = tokens;
    }

    private int extractFunction(int pos) {
        String funcName = tokens.get(pos+1).getValue();

        List<String> args = new ArrayList<>();
        pos = extractFunctionArgs(pos, funcName, args);

        List<Token> funcBody = new ArrayList<>();
        Map<String, VarData> funcVarTableData = new HashMap<>();
        pos = extractFunctionBody(pos, funcName, funcBody, funcVarTableData);

        FunctionTable.getInstance().put(funcName, new Function(args, funcBody, funcVarTableData));

        return pos - 1;
    }

    private int extractFunctionArgs(int pos, String funcName, List<String> args) {
        for (pos += 3; tokens.get(pos).getType() != Lexem.RIGHT_B; ++pos) {
            Token curToken = tokens.get(pos);
            if (curToken.getType() == Lexem.VAR) {
                args.add(constructVariableName(funcName, curToken.getValue()));
            }
        }

        return pos;
    }

    private int extractFunctionBody(int pos, String funcName, List<Token> funcBody,
                                    Map<String, VarData> funcVarTableData) {
        List<Token> nonRpnBody = new ArrayList<>();
        int unclosedBracketsCount = 1;

        // Extracting body
        for (pos += 2; unclosedBracketsCount != 0; ++pos) {
            Token curToken = tokens.get(pos);
            Lexem curType = curToken.getType();

            if (curType == Lexem.LEFT_BR) {
                ++unclosedBracketsCount;
            }
            else if (curType == Lexem.RIGHT_BR) {
                --unclosedBracketsCount;
            }
            else if (curType == Lexem.VAR) {
                curToken.setValue(constructVariableName(funcName, curToken.getValue()));
            }

            nonRpnBody.add(curToken);
        }
        nonRpnBody.remove(nonRpnBody.size() - 1); // Removing last bracket

        // Copying current values
        List<Token> tokensCopy = this.tokens;
        this.tokens = nonRpnBody;

        // Replacing with function values
        Map<String, VarData> varTableDataCopy = new HashMap<>();
        varTableDataCopy.putAll(VarTable.getInstance().getData());
        VarTable.getInstance().clear();

        // Making RPN
        funcBody.addAll(getRpn());

        // Moving values back
        this.tokens = tokensCopy;
        funcVarTableData.putAll(VarTable.getInstance().getData());
        VarTable.getInstance().setData(varTableDataCopy);

        return pos;
    }

    private String constructVariableName(String funcName, String varName) {
        return funcName + "_" + varName;
    }

    public List<Token> getRpn() {
        List<Token> rpnList = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        Stack<Lexem> exprWithTransitions = new Stack<>();
        Stack<Integer> whileKwPositions = new Stack<>();
        Stack<Token> varsWithMethodCalled = new Stack<>();
        Stack<Token> methodCalled = new Stack<>();
        int transitionNumber = 0;
        boolean wasInput = false; // true -- was "Jon" token, false -- was "Ygritte" token

        for (int i = 0; i < tokens.size(); ++i) {
            Token curToken = tokens.get(i);
            Lexem curType = curToken.getType();

            if (curType == Lexem.FUNCTION_KW) {
                i = extractFunction(i);
                continue;
            }

            // Skipping tokens with priority < 0
            if (curType.getPriority() < 0) {
                continue;
            }

            // Processing METHOD
            if (curType == Lexem.METHODS) {
                varsWithMethodCalled.push(rpnList.remove(rpnList.size() - 1));
                methodCalled.push(curToken);
                continue;
            }

            // Processing INPUT_OUTPUT_OP
            if (curType == Lexem.OUTPUT_OP) {
                fromStackToList(stack, rpnList);
                stack.add(new Token(Lexem.OUTPUT_OP, "print"));
                continue;
            }

            // Processing variables, digits and strings
            if (curType == Lexem.VAR || curType == Lexem.DIGIT ||
                    curType == Lexem.TYPE) {
                rpnList.add(curToken);
                continue;
            }

            // Processing open parenth
            if (curType == Lexem.LEFT_B) {
                stack.push(curToken);
                continue;
            }

            // Processing semicolon
            if (curType == Lexem.SEMICOLON) {
                fromStackToList(stack, rpnList);
                continue;
            }

            // Processing close parenth
            if (curType == Lexem.RIGHT_B) {
                if (!methodCalled.empty()) {
                    rpnList.add(varsWithMethodCalled.pop());
                    rpnList.add(methodCalled.pop());
                }
                Token top = stack.pop();
                while (top.getType() != Lexem.LEFT_B) {
                    rpnList.add(top);
                    top = stack.pop();
                }

                continue;
            }

            // Processing open bracket
            if (curType == Lexem.LEFT_BR) {
                // Inserting false transition
                if (!exprWithTransitions.empty()) {
                    rpnList.add(
                            new Token(Lexem.VAR,
                                    "_p" + Integer.toString(++transitionNumber)
                            )
                    );
                    rpnList.add(new Token(Lexem.FALSE_TRANSITION, "!F"));
                }

                continue;
            }

            // Processing close bracket
            if (curType == Lexem.RIGHT_BR) {
                // Setting transition variable
                if (!exprWithTransitions.empty()) {
                    int falseTransitionPointer = rpnList.size();
                    int oldTransitionNumber = transitionNumber;

                    if (exprWithTransitions.lastElement() == Lexem.WHILE_KW) {
                        falseTransitionPointer += 2; // To skip unconditional transition

                        String transVar = "_p" +
                                Integer.toString(++transitionNumber);
                        rpnList.add(new Token(Lexem.VAR, transVar));
                        rpnList.add(new Token(Lexem.UNCONDITIONAL_TRANSITION, "!"));
                        VarTable.getInstance().add(transVar, "int",
                                whileKwPositions.pop());
                    }
                    // Adding pointer for false transition
                    VarTable.getInstance().add("_p" + Integer.toString(oldTransitionNumber), "int",
                            falseTransitionPointer);
                    exprWithTransitions.pop();
                }

                continue;
            }

            // Processing if and while
            if (curType == Lexem.IF_KW || curType == Lexem.WHILE_KW) {
                exprWithTransitions.push(curType);
                if (curType == Lexem.WHILE_KW) {
                    whileKwPositions.push(rpnList.size());
                }
                continue;
            }

            // Processing other types
            if (stack.empty() ||
                    stack.peek().getType().getPriority() < curType.getPriority()) {
                stack.push(curToken);
            }
            else {
                while (!stack.empty() &&
                        stack.peek().getType().getPriority() >= curType.getPriority()) {
                    rpnList.add(stack.pop());
                }

                stack.push(curToken);
            }
        }

        fromStackToList(stack, rpnList);

        return rpnList;
    }

}