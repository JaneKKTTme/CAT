package triad_optimizer.triad.argument;

import exception.ExecuteException;
import exception.NotImplementedException;
import triad_optimizer.triad.Tokenizable;

public abstract class TriadArgument implements Tokenizable {
    public abstract int getValue() throws ExecuteException, NotImplementedException;

    public abstract String toString();

    public abstract int hashCode();

    public abstract boolean equals(Object obj);
}
