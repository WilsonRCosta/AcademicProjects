package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public class IteratorIterate<T> implements Iterator<T> {
    private UnaryOperator<T> acc;
    private T seed;
    private T lastValue;

    public IteratorIterate(T seed, UnaryOperator<T> acc) {
        this.seed = seed;
        this.acc = acc;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        lastValue = (lastValue == null) ? seed : acc.apply(lastValue);
        return lastValue;
    }
}
