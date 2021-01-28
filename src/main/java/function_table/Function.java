package function_table;

import java.util.List;
import java.util.Map;

import token.Token;
import vartable.VarTable.VarData;

public class Function {
    private List<String> args;
    private List<Token> body;
    private Map<String, VarData> varTableData;

    public Function(List<String> args, List<Token> body, Map<String, VarData> varTableData) {
        this.args = args;
        this.body = body;
        this.varTableData = varTableData;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<Token> getBody() {
        return body;
    }

    public Map<String, VarData> getVarTableData() {
        return varTableData;
    }

    public String toString() {
        return "args = " + args + ", body = " + body + ", varTableData = " + varTableData;
    }

}
