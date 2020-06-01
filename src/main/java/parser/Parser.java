package parser;

import exception.LangParseException;
import lexer.Lexem;
import token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Parser {
    private class ParseResult {
        public boolean success;
        public int depth;
        public String error_mes;

        public ParseResult() {
            this.success = true;
            this.depth = 0;
            this.error_mes = "";
        }

        public ParseResult(boolean success, int depth, String error_mes) {
            this.success = success;
            this.depth = depth;
            this.error_mes = error_mes;
        }
    }

    private final List<Token> tokens;
    private int pos = -1;
    private ParseResult most_depth_error_res;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public ParseResult lang() throws LangParseException {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {
            return plusOperation((arg) -> {return expr();});
        });

        ParseResult result = andOperation(expressions);
        if (!result.success) {
            throw new LangParseException(most_depth_error_res.error_mes);
        }

        return result;
    }

    private ParseResult expr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg) -> {return assignExpr();});
        expressions.add((arg) -> {return outputExpr();});
        expressions.add((arg) -> {return ifExpr();});
        expressions.add((arg) -> {return whileExpr();});

        return orOperation(expressions);
    }

    private ParseResult assignExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return valueExpr();});
        expressions.add((arg0) -> {return assignOp();});
        expressions.add((arg0) -> {return var();});
        expressions.add((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult outputExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return var();});
        expressions.add((arg0) -> {return outputOp();});
        expressions.add((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult ifExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return ifHead();});
        expressions.add((arg0) -> {return body();});
        expressions.add((arg0) -> {return elseHead();});
        expressions.add((arg0) -> {return body();});
        expressions.add((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult whileExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return whileHead();});
        expressions.add((arg0) -> {return body();});
        expressions.add((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult var() {
        return matchToken(match(), Lexem.VAR);
    }

    private ParseResult assignOp() {
        return matchToken(match(), Lexem.ASSIGN_OP);
    }

    private ParseResult outputOp() {
        return matchToken(match(), Lexem.OUTPUT_OP);
    }

    private ParseResult value() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return var();});
        expressions.add((arg0) -> {return digit();});

        return orOperation(expressions);
    }

    private ParseResult digit() {
        return matchToken(match(), Lexem.DIGIT);
    }

    private ParseResult op() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return additionalOp();});
        expressions.add((arg0) -> {return subtractionOp();});
        expressions.add((arg0) -> {return multiplicationOp();});
        expressions.add((arg0) -> {return divisionOp();});

        return orOperation(expressions);
    }

    private ParseResult additionalOp() {
        return matchToken(match(), Lexem.ADDITION_OP);
    }

    private ParseResult subtractionOp() {
        return matchToken(match(), Lexem.SUBTRACTION_OP);
    }

    private ParseResult multiplicationOp() {
        return matchToken(match(), Lexem.MULTIPLICATION_OP);
    }

    private ParseResult divisionOp() {
        return matchToken(match(), Lexem.DIVISION_OP);
    }

    private ParseResult logicalExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return value();});
        expressions.add((arg0) -> {return logicalOp();});
        expressions.add((arg0) -> {return value();});

        return andOperation(expressions);
    }

    private ParseResult valueExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return value();});
        expressions.add((arg0) -> {return arithmExpr();});

        return orOperation(expressions);
    }

    private ParseResult arithmExpr() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return value();});
        expressions.add((arg0) -> {
            List<Function<Object, ParseResult>> and_expressions = new ArrayList<>();
            and_expressions.add((arg) -> {return op();});
            and_expressions.add((arg) -> {return value();});
            return plusOperation((arg) -> andOperation(and_expressions));
        });

        return andOperation(expressions);
    }

    private ParseResult logicalOp() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return moreLogicalOp();});
        expressions.add((arg0) -> {return lessLogicalOp();});
        expressions.add((arg0) -> {return moreOrEqualLogicalOp();});
        expressions.add((arg0) -> {return lessOrEqualLogicalOp();});
        expressions.add((arg0) -> {return equalLogicalOp();});

        return orOperation(expressions);
    }

    private ParseResult moreLogicalOp() {
        return matchToken(match(), Lexem.MORE_LOGICAL_OP);
    }

    private ParseResult lessLogicalOp() {
        return matchToken(match(), Lexem.LESS_LOGICAL_OP);
    }

    private ParseResult moreOrEqualLogicalOp() {
        return matchToken(match(), Lexem.MORE_OR_EQUAL_LOGICAL_OP);
    }

    private ParseResult lessOrEqualLogicalOp() {
        return matchToken(match(), Lexem.LESS_OR_EQUAL_LOGICAL_OP);
    }

    private ParseResult equalLogicalOp() {
        return matchToken(match(), Lexem.EQUAL_LOGICAL_OP);
    }

    private ParseResult body() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return leftBr();});
        expressions.add((arg0) -> {
            return plusOperation((arg) -> {return expr();});
        });
        expressions.add((arg0) -> {return rightBr();});

        return andOperation(expressions);
    }

    private ParseResult elseHead() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return elseKeyword();});
        expressions.add((arg0) -> {return body();});

        return andOperation(expressions);
    }

    private ParseResult elseKeyword() {
        return matchToken(match(), Lexem.ELSE_KW);
    }

    private ParseResult ifHead() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return ifKeyword();});
        expressions.add((arg0) -> {return leftB();});
        expressions.add((arg0) -> {return logicalExpr();});
        expressions.add((arg0) -> {return rightB();});

        return andOperation(expressions);
    }

    private ParseResult rightB() {
        return matchToken(match(), Lexem.RIGHT_B);
    }

    private ParseResult leftB() {
        return matchToken(match(), Lexem.LEFT_B);
    }

    private ParseResult rightBr() {
        return matchToken(match(), Lexem.RIGHT_BR);
    }

    private ParseResult leftBr() {
        return matchToken(match(), Lexem.LEFT_BR);
    }

    private ParseResult ifKeyword() {
        return matchToken(match(), Lexem.IF_KW);
    }

    private ParseResult whileHead() {
        List<Function<Object, ParseResult>> expressions = new ArrayList<>();
        expressions.add((arg0) -> {return whileKeyword();});
        expressions.add((arg0) -> {return leftB();});
        expressions.add((arg0) -> {return logicalExpr();});
        expressions.add((arg0) -> {return rightB();});

        return andOperation(expressions);
    }

    private ParseResult whileKeyword() {
        return matchToken(match(), Lexem.WHILE_KW);
    }

    private ParseResult semicolon() {
        return matchToken(match(), Lexem.SEMICOLON);
    }

    private ParseResult orOperation(List<Function<Object, ParseResult>> expressions) {
        List<ParseResult> results = new ArrayList<>();

        for (Function<Object, ParseResult> func : expressions) {
            ParseResult cur_res = func.apply(null);
            if (cur_res.success) {
                // System.out.println(new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName() + "::OR::Matched: " + func);
                return cur_res;
            }
            else {
                // System.out.println(new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName() + "::OR::Not matched: " + func);
                results.add(cur_res);
                // if func isn't the last
                if (!func.equals(expressions.get(expressions.size() - 1))) {
                    back(cur_res.depth);
                }
            }
        }

        // If no one expression matches then store the most depth result
        most_depth_error_res = Collections.max(results, new Comparator<ParseResult>() {
            public int compare(ParseResult left, ParseResult right) {
                if (left.depth > right.depth) {
                    return 1;
                }
                if (right.depth > left.depth) {
                    return -1;
                }

                return 0;
            }
        });

        return results.get(results.size() - 1);
    }

    private ParseResult andOperation(List<Function<Object, ParseResult>> expressions) {
        List<ParseResult> results = new ArrayList<>();
        for (Function<Object, ParseResult> func : expressions) {
            ParseResult cur_res = func.apply(null);
            if (results.size() != 0) {
                cur_res.depth += results.get(results.size() - 1).depth;
            }

            if (!cur_res.success) {
                // System.out.println(new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName() + "::AND::Not matched: " + func);
                return cur_res;
            }

            // System.out.println(new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName() + "::AND::Matched: " + func);
            results.add(cur_res);
        }

        return results.get(results.size() - 1);
    }

    private ParseResult plusOperation(Function<Object, ParseResult> expression) {
        ParseResult cur_res = new ParseResult();
        int depth_sum = 0;
        int counter = -1;

        while(cur_res.success) {
            ++counter;
            cur_res = expression.apply(null);
            depth_sum += cur_res.depth;
        }

        if (counter < 1) {
            back(depth_sum);
            return cur_res;
        }

        back(cur_res.depth);
        cur_res.depth = depth_sum - cur_res.depth;
        cur_res.success = true;

        return cur_res;
    }

    private Token match() {
        // Token token = tokens.get(++pos);
        // System.out.println("Current token = " + token + " on pos = " + pos);
        // return token;
        return tokens.get(++pos);
    }

    private void back(int step) {
        pos -= step;
        // System.out.println("back::pos = " + pos);
    }

    private ParseResult matchToken(Token token, Lexem type) {
        if (!token.getLexem().equals(type)) {
            return new ParseResult(false, 1, type + " expected, but " +
                    token.getLexem().name() + ": " +
                    token.getValue() + " found");
        }

        return new ParseResult(true, 1, "");
    }
}
