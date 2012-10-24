package cs475;

import java.util.*;
import java.io.Serializable;

// An immutable pair class
public class Pair<T1, T2> implements Serializable {
    public final T1 first;
    public final T2 second;

    public Pair(T1 f, T2 s) {
        first = f;
        second = s;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Pair)) { return false; }
        try {
            Pair<T1, T2> otherPair = (Pair<T1, T2>) other;
            return (first.equals(otherPair.first) && second.equals(otherPair.second));
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public int hashCode() {
        int result  = 67;
        result = 37*result + first.hashCode();
        result = 37*result + second.hashCode();
        return result;
    }


}
