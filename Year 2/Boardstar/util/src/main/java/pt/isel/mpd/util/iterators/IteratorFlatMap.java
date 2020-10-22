package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorFlatMap<T, R>  implements Iterator<R> {
    private final Function<T, Iterable<R>> mapper;
    private Iterator<T> iter;
    private Iterator<R> auxIter;


    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        iter = src.iterator();
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        if (auxIter != null && auxIter.hasNext()) {
            return true;
        }
        else {
            if (!iter.hasNext()) return false;
            else {
                auxIter = mapper.apply(iter.next()).iterator();
                return auxIter.hasNext();
            }
        }
    }

    @Override
    public R next() {
        return auxIter.next();
    }
}
