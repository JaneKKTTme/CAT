import junit.framework.TestCase;

import lexer.Lexem;
import token.Token;
import stack_machine.StackMachine;

import java.util.*;

public class StackMachineTest extends TestCase {
    private final Map<List<Token>, Map<String, Double>> canculateData = new HashMap<>();

    protected void setData() {
        List<Token> rpn_tokens = new ArrayList<>();
        Map<String, Double> varTable = new HashMap<>();

        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        varTable.put("a", 1.0);
        canculateData.put(rpn_tokens, varTable);

        canculateData.clear();
        varTable.clear();

        rpn_tokens.add(new Token(Lexem.DIGIT, "2"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        rpn_tokens.add(new Token(Lexem.DIGIT, "1"));
        rpn_tokens.add(new Token(Lexem.VAR, "a"));
        rpn_tokens.add(new Token(Lexem.ADDITION_OP, "+"));
        rpn_tokens.add(new Token(Lexem.VAR, "b"));
        rpn_tokens.add(new Token(Lexem.ASSIGN_OP, "->"));
        varTable.put("a", 2.0);
        varTable.put("b", 3.0);
        canculateData.put(rpn_tokens, varTable);

        canculateData.clear();
        varTable.clear();

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
        varTable.put("a", 11.0);
        varTable.put("b", 20.0);
        canculateData.put(rpn_tokens, varTable);

        canculateData.clear();
        varTable.clear();

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
        varTable.put("b", 20.0);
        canculateData.put(rpn_tokens, varTable);

        canculateData.clear();
        varTable.clear();
    }

    public void testCanculate() {
        for (Iterator iterator = canculateData.keySet().iterator(); iterator.hasNext();) {
            StackMachine rpn = new StackMachine((List<Token>) iterator.next());
            Map<String, Double> actual = rpn.canculate();
            assertEquals(canculateData.get(iterator), actual);
        }
    }
}
