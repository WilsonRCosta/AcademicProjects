package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> iter;
    private final Predicate<T> pred;
    private boolean called = false;
    private boolean predFailed = false;
    private T curr;

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.iter = src.iterator();
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if (called) return true;
        if (predFailed || !iter.hasNext()) return false;
        else {
            curr = iter.next();
            if (!pred.test(curr)) {
                predFailed = true;
                return false;
            } else {
                called = true;
                return true;
            }
        }
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        called = false;
        return curr;
    }
}
