package pt.isel.mpd.util.spliterators;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SpliteratorInterleave<T> extends Spliterators.AbstractSpliterator  {

    private Spliterator<T> src;
    private Spliterator<T> other;

    public SpliteratorInterleave(Spliterator<T> src, Spliterator<T> other) {
        super(src.estimateSize() + other.estimateSize(),
                src.characteristics() & other.characteristics());
        this.src = src;
        this.other = other;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        Spliterator<T> aux = src;
        if(aux.tryAdvance(action)) {
            src = other;
            other = aux;
            return true;
        }
        return other.tryAdvance(action);
    }
}
