package parser;

import exception.LangParseException;
import lexer.Lexem;
import token.Token;
import type.lists.CatDoublyLinkedList;

import java.util.*;
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

    private ParseResult most_depth_error_res;
    private int pos = -1;
    private final CatDoublyLinkedList<Token> tokens;

    public Parser(CatDoublyLinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    private ParseResult additionalOp() {
        return matchToken(match(), Lexem.ADDITION_OP);
    }

    private ParseResult andOperation(CatDoublyLinkedList<Function<Object, ParseResult>> expressions) {
        CatDoublyLinkedList<ParseResult> results = new CatDoublyLinkedList<>();
        for (int i = 0; i < expressions.size(); i++) {
            Function<Object, ParseResult> func = (Function<Object, ParseResult>) expressions.get(i);
            ParseResult cur_res = func.apply(null);
            if (results.size() != 0) {
                cur_res.depth += ((ParseResult) results.get(results.size() - 1)).depth;
            }
            if (!cur_res.success) {
                return cur_res;
            }
            results.addBack(cur_res);
        }

        return (ParseResult) results.get(results.size() - 1);
    }

    private ParseResult assignExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return valueExpr();});
        expressions.addBack((arg0) -> {return assignOp();});
        expressions.addBack((arg0) -> {return var();});
        expressions.addBack((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult assignOp() {
        return matchToken(match(), Lexem.ASSIGN_OP);
    }

    private ParseResult arithmExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return value();});
        expressions.addBack((arg0) -> {
            CatDoublyLinkedList<Function<Object, ParseResult>> and_expressions = new CatDoublyLinkedList<>();
            and_expressions.addBack((arg) -> {return op();});
            and_expressions.addBack((arg) -> {return value();});
            return plusOperation((arg) -> andOperation(and_expressions));
        });

        return andOperation(expressions);
    }

    private void back(int step) {
        pos -= step;
    }

    private ParseResult body() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return leftBr();});
        expressions.addBack((arg0) -> {
            return plusOperation((arg) -> {return expr();});
        });
        expressions.addBack((arg0) -> {return rightBr();});

        return andOperation(expressions);
    }

    private ParseResult digit() {
        return matchToken(match(), Lexem.DIGIT);
    }

    private ParseResult divisionOp() {
        return matchToken(match(), Lexem.DIVISION_OP);
    }

    private ParseResult elseHead() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return elseKeyword();});
        expressions.addBack((arg0) -> {return body();});

        return andOperation(expressions);
    }

    private ParseResult elseKeyword() {
        return matchToken(match(), Lexem.ELSE_KW);
    }

    private ParseResult equalLogicalOp() {
        return matchToken(match(), Lexem.EQUAL_LOGICAL_OP);
    }

    private ParseResult expr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return assignExpr();});
        expressions.addBack((arg0) -> {return outputExpr();});
        expressions.addBack((arg0) -> {return ifExpr();});
        expressions.addBack((arg0) -> {return whileExpr();});

        return orOperation(expressions);
    }

    public CatDoublyLinkedList<Token> getTokens() {
        return this.tokens;
    }

    private ParseResult ifExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return ifHead();});
        expressions.addBack((arg0) -> {return body();});
        expressions.addBack((arg0) -> {return elseHead();});
        expressions.addBack((arg0) -> {return body();});
        expressions.addBack((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult ifHead() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return ifKeyword();});
        expressions.addBack((arg0) -> {return leftB();});
        expressions.addBack((arg0) -> {return logicalExpr();});
        expressions.addBack((arg0) -> {return rightB();});

        return andOperation(expressions);
    }

    private ParseResult ifKeyword() {
        return matchToken(match(), Lexem.IF_KW);
    }

    public ParseResult lang() throws LangParseException {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {
            return plusOperation((arg) -> {return expr();});
        });
        ParseResult result = andOperation(expressions);
        if (!result.success) {
            throw new LangParseException(most_depth_error_res.error_mes);
        }

        return result;
    }

    private ParseResult leftB() {
        return matchToken(match(), Lexem.LEFT_B);
    }

    private ParseResult leftBr() {
        return matchToken(match(), Lexem.LEFT_BR);
    }

    private ParseResult lessLogicalOp() {
        return matchToken(match(), Lexem.LESS_LOGICAL_OP);
    }

    private ParseResult lessOrEqualLogicalOp() {
        return matchToken(match(), Lexem.LESS_OR_EQUAL_LOGICAL_OP);
    }

    private ParseResult logicalExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return value();});
        expressions.addBack((arg0) -> {return logicalOp();});
        expressions.addBack((arg0) -> {return value();});

        return andOperation(expressions);
    }

    private ParseResult logicalOp() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return moreLogicalOp();});
        expressions.addBack((arg0) -> {return lessLogicalOp();});
        expressions.addBack((arg0) -> {return moreOrEqualLogicalOp();});
        expressions.addBack((arg0) -> {return lessOrEqualLogicalOp();});
        expressions.addBack((arg0) -> {return equalLogicalOp();});

        return orOperation(expressions);
    }

    private Token match() {
        return (Token) tokens.get(++pos);
    }

    private ParseResult matchToken(Token token, Lexem type) {
        if (!token.getLexem().equals(type)) {
            return new ParseResult(false, 1, type + " expected, but " +
                    token.getLexem().name() + ": " +
                    token.getValue() + " found");
        }

        return new ParseResult(true, 1, "");
    }

    private ParseResult moreLogicalOp() {
        return matchToken(match(), Lexem.MORE_LOGICAL_OP);
    }

    private ParseResult moreOrEqualLogicalOp() {
        return matchToken(match(), Lexem.MORE_OR_EQUAL_LOGICAL_OP);
    }

    private ParseResult multiplicationOp() {
        return matchToken(match(), Lexem.MULTIPLICATION_OP);
    }

    private ParseResult op() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return additionalOp();});
        expressions.addBack((arg0) -> {return subtractionOp();});
        expressions.addBack((arg0) -> {return multiplicationOp();});
        expressions.addBack((arg0) -> {return divisionOp();});

        return orOperation(expressions);
    }

    private ParseResult orOperation(CatDoublyLinkedList<Function<Object, ParseResult>> expressions) {
        CatDoublyLinkedList<ParseResult> results = new CatDoublyLinkedList<>();
        for (int i = 0; i < expressions.size(); i++) {
            Function<Object, ParseResult> func = (Function<Object, ParseResult>) expressions.get(i);
            ParseResult cur_res = func.apply(null);
            if (cur_res.success) {
                return cur_res;
            }
            else {
                results.addBack(cur_res);
                if (!func.equals(expressions.get(expressions.size() - 1))) {
                    back(cur_res.depth);
                }
            }
        }
        most_depth_error_res = Collections.max((Collection) results, new Comparator<ParseResult>() {
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

        return (ParseResult) results.get(results.size() - 1);
    }

    private ParseResult outputExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return var();});
        expressions.addBack((arg0) -> {return outputOp();});
        expressions.addBack((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult outputOp() {
        return matchToken(match(), Lexem.OUTPUT_OP);
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

    private ParseResult rightB() {
        return matchToken(match(), Lexem.RIGHT_B);
    }

    private ParseResult rightBr() {
        return matchToken(match(), Lexem.RIGHT_BR);
    }

    private ParseResult semicolon() {
        return matchToken(match(), Lexem.SEMICOLON);
    }

    private ParseResult subtractionOp() {
        return matchToken(match(), Lexem.SUBTRACTION_OP);
    }

    private ParseResult valueExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return value();});
        expressions.addBack((arg0) -> {return arithmExpr();});

        return orOperation(expressions);
    }

    private ParseResult whileExpr() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return whileHead();});
        expressions.addBack((arg0) -> {return body();});
        expressions.addBack((arg0) -> {return semicolon();});

        return andOperation(expressions);
    }

    private ParseResult whileHead() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return whileKeyword();});
        expressions.addBack((arg0) -> {return leftB();});
        expressions.addBack((arg0) -> {return logicalExpr();});
        expressions.addBack((arg0) -> {return rightB();});

        return andOperation(expressions);
    }

    private ParseResult whileKeyword() {
        return matchToken(match(), Lexem.WHILE_KW);
    }

    private ParseResult value() {
        CatDoublyLinkedList<Function<Object, ParseResult>> expressions = new CatDoublyLinkedList<>();
        expressions.addBack((arg0) -> {return var();});
        expressions.addBack((arg0) -> {return digit();});

        return orOperation(expressions);
    }

    private ParseResult var() {
        return matchToken(match(), Lexem.VAR);
    }
}
