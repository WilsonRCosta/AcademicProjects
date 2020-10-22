package pt.isel.mpd.util.iterators;

import java.util.Iterator;

public class IteratorSkip<T> implements Iterator<T> {
    private Iterator<T> iter;

    public IteratorSkip(Iterable<T> src, int skip) {
        iter = src.iterator();
        while(skip-- > 0 && iter.hasNext()) iter.next();
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public T next() {
        return iter.next();
    }
}
