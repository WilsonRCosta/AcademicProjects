package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorLimit<T> implements Iterator<T> {
    private Iterator<T> iter;
    private int limit;

    public IteratorLimit(Iterable<T> src, int limit) {
        iter = src.iterator();
        this.limit = limit;
    }
    @Override
    public boolean hasNext() {
        return limit > 0 && iter.hasNext();
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        limit--;
        return iter.next();
    }
}
