import exception.LangParseException;
import token.Token;
import lexer.Lexer;
import parser.Parser;
import stack_machine.ReversePolishNotation;
import stack_machine.StackMachine;
import vartable.VarTable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CatUI {
    public static void main(String[] args) throws Exception, LangParseException {

        if (args.length < 1) {
            System.err.println("Usage: CatUI <filename>");
            System.exit(-1);
        }
        String rawInput = Files.readString(Paths.get(args[0]));

        Lexer lexer = new Lexer(rawInput);
        List<Token> lexerResult = lexer.getTokens();
        System.out.println("LEXER RESULT: " + lexerResult + "\n");

        Parser parser = new Parser(lexer.getTokens());
        parser.lang();

        ReversePolishNotation rpn = new ReversePolishNotation(lexer.getTokens());
        List<Token> rpnResult = rpn.translate();
        System.out.println("RPN RESULT: " + rpnResult + "\n");

        StackMachine stackMachine = new StackMachine(rpnResult);
        VarTable stackMachineResult = stackMachine.canculate();
        System.out.println("\nSTACK MACHINE RESULT: " + stackMachineResult + "\n");
    }
}
