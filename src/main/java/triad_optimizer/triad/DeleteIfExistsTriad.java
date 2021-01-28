package triad_optimizer.triad;

import java.util.List;

import triad_optimizer.triad.argument.*;
import exception.NotImplementedException;
import token.Token;

/**
 * Specific construction, which tells to delete variable from table if table has it
 */
public class DeleteIfExistsTriad extends Triad {
    public DeleteIfExistsTriad(Variable var) {
        super(var, new Digit(0), null, 0, 0);
    }

    @Override
    public int evaluate() throws NotImplementedException {
        throw new NotImplementedException("Can't evaluate the DeleteIfExist Triad");
    }

    @Override
    public void setSecond(TriadArgument second) throws NotImplementedException {
        throw new NotImplementedException("Can't set second argument of DeleteIfExist Triad");
    }

    @Override
    public Token getOperation() throws NotImplementedException {
        throw new NotImplementedException("Can't get operation of DeleteIfExist Triad");
    }

    @Override
    public void setOperation(Token operation) throws NotImplementedException {
        throw new NotImplementedException("Can't set operation of DeleteIfExist Triad");
    }

    @Override
    public List<Token> tokenize() throws NotImplementedException {
        throw new NotImplementedException("Can't tokenize DeleteIfExist Triad");
    }

    @Override
    public String toString() {
        return "DELETE_IF_EXIST(" + getFirst() + ", 0)";
    }
}
