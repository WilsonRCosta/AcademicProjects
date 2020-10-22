package pt.isel.mpd.util.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/*
 * A sequência resultante deve guardar em memória os elementos que vão sendo obtidos
 * por um iterador. O método next() retorna sempre os elementos que já estejam guardados
 * em memória e só obtém um novo elemento caso este não esteja cached.
 *
 * */

public class IterableCache<T> implements Iterable<T> {

    private Iterator<T> iter;
    private List<T> cache;
    private boolean isCacheFull = false;


    public IterableCache(Iterable<T> src) {
        iter = src.iterator();
        cache = new ArrayList<>();
    }

    @Override
    public Iterator<T> iterator() {
        // if cache is full return it
        return isCacheFull ? cache.iterator() : new Iterator<T>() {

            int counter = 0;
            boolean called = false;

            @Override
            public boolean hasNext() {
                if (called) return true;

                if (iter.hasNext()) {
                    called = true;
                    return true;
                }

                //no more items in Iterator -> cache is full
                isCacheFull = true;
                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                called = false;

                if (counter >= cache.size()) {
                    T element = iter.next();
                    cache.add(element);
                    counter++;
                    return element;
                }
                // in case cache is not full but we still need elements stored in cache
                return cache.get(counter++);
            }
        };
    }
};