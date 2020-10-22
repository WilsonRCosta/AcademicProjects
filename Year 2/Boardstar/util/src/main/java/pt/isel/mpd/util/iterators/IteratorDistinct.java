package pt.isel.mpd.util.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IteratorDistinct<T> implements Iterator<T> {
    private Iterator<T> iter;
    private T nextValue;
    private List<T> usedValues;

    public IteratorDistinct(Iterable<T> src) {
        iter = src.iterator();
        usedValues = new ArrayList<>();
    }

    public boolean hasNext() {
        if (nextValue != null) return true;
        while (iter.hasNext()) {
            nextValue = iter.next();
            if (!usedValues.contains(nextValue)) {
                usedValues.add(nextValue);
                return true;
            }
        }
        return false;
    }


    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        T returnValue = nextValue;
        nextValue = null;
        return returnValue;
    }
}
