import exception.LangParseException;
import token.Token;
import lexer.Lexer;
import parser.Parser;
import stack_machine.ReversePolishNotation;
import stack_machine.StackMachine;
import type.lists.CatDoublyLinkedList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CatUI {
    public static void main(String[] args) throws Exception, LangParseException {

        /*if (args.length < 1) {
            System.err.println("Usage: CatUI <filename>");
            System.exit(-1);
        }
        String rawInput = Files.readString(Paths.get(args[0]));*/

        String rawInput = "5->a.";

        Lexer lexer = new Lexer(rawInput);
        List<Token> lexer_result = lexer.getTokens();
        System.out.println("LEXER RESULT: " + lexer_result + "\n");

        Parser parser = new Parser(lexer.getTokens());
        parser.lang();

        ReversePolishNotation rpn = new ReversePolishNotation(lexer.getTokens());
        List<Token> rpn_result = rpn.translate();
        System.out.println("RPN RESULT: " + rpn_result + "\n");

        StackMachine stackMachine = new StackMachine(rpn_result);
        System.out.println("STACK MACHINE RESULT: " + stackMachine.canculate() + "\n");
    }
}
