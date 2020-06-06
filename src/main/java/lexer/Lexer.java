package lexer;

import token.Token;
import type.lists.CatDoublyLinkedList;

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

    public CatDoublyLinkedList<Token> getTokens() throws Exception {
        CatDoublyLinkedList<Token> tokens = new CatDoublyLinkedList<Token>();
        int lineCounter = 0;
        CatDoublyLinkedList<String> tmp = getLines(rawInput);
        for (int i = 0; i < tmp.size(); i++) {
            String line = tmp.get(i).toString();
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
                    tokens.addBack(new Token(relevantLexem, value));
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

    private CatDoublyLinkedList<String> getLines(String str) {
        Scanner scanner = new Scanner(str);
        CatDoublyLinkedList<String> lines = new CatDoublyLinkedList<String>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.addBack(line);
        }
        scanner.close();

        return lines;
    }
}