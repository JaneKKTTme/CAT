package lexer;

import token.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String rawInput;

    public Lexer(String rawInput) {
        this.rawInput = rawInput;
    }

    public String getRawInput() {
        return rawInput;
    }

    public List<Token> getTokens() throws Exception {
        List<Token> tokens = new ArrayList<Token>();
        int lineCounter = 0;

        for (String line : getLines(rawInput)) {
            try {
                ++lineCounter;

                // While line contains something (except spaces)
                while (line.matches("(\\s*\\S+\\s*)+")) {
                    line = line.trim();

                    // Assigning first value in enum
                    Lexem relevantLexem = Lexem.values()[0];

                    // Modifying original regex pattern for our purpose
                    String real_regex = "^(" + relevantLexem.getPattern().pattern() + ")";
                    Matcher matcher = Pattern.compile(real_regex).matcher(line);

                    // Finding relevant lexeme type or throwing exception
                    while (!matcher.find()) {
                        relevantLexem = getNextLexem(relevantLexem);
                        real_regex = "^(" + relevantLexem.getPattern().pattern() + ")";
                        matcher.usePattern(Pattern.compile(real_regex));
                    }
                    // Relevant lexeme type was founded

                    String value = matcher.group(0);
                    tokens.add(new Token(relevantLexem, value));
                    // Replacing first founded value with spaces
                    line = matcher.replaceFirst("");
                }
            }
            catch (IndexOutOfBoundsException ex) {
                Scanner scanner = new Scanner(line);
                String unknownSymbol = scanner.next();
                scanner.close();

                throw new Exception("Unknown symbol at line " + lineCounter + " : " + unknownSymbol);
            }
        }

        return tokens;
    }

    private Lexem getNextLexem(Lexem lexemType) throws IndexOutOfBoundsException {
        int curPos = lexemType.ordinal();
        Lexem[] lexem = Lexem.values();

        if (curPos >= lexem.length) {
            throw new IndexOutOfBoundsException();
        }

        return lexem[curPos + 1];
    }

    private List<String> getLines(String str) {
        Scanner scanner = new Scanner(str);
        List<String> lines = new ArrayList<String>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }

        scanner.close();
        return lines;
    }
}