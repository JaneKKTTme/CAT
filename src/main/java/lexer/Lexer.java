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
        List<Token> tokens = new ArrayList<>();
        int lineCounter = 0;
        for (String line : getLines(rawInput)) {
            try {
                ++lineCounter;
                while (line.matches("(\\s*\\S+\\s*)+")) {
                    line = line.trim();
                    Lexem relevantLexem = Lexem.values()[0];
                    String real_regex = "^(" + relevantLexem.getPattern().pattern() + ")";
                    Matcher matcher = Pattern.compile(real_regex).matcher(line);
                    while (!matcher.find()) {
                        relevantLexem = getNextLexem(relevantLexem);
                        real_regex = "^(" + relevantLexem.getPattern().pattern() + ")";
                        matcher.usePattern(Pattern.compile(real_regex));
                    }
                    String value = matcher.group(0);
                    tokens.add(new Token(relevantLexem, value));
                    line = matcher.replaceFirst("");
                }
            }
            catch (IndexOutOfBoundsException ex) {
                Scanner scanner = new Scanner(line);
                String unknownSymbol = scanner.next();
                scanner.close();
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
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        scanner.close();

        return lines;
    }
}