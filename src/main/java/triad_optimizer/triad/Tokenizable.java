package triad_optimizer.triad;

import java.util.List;

import exception.NotImplementedException;
import token.Token;

public interface Tokenizable {
    List<Token> tokenize() throws NotImplementedException;
}
