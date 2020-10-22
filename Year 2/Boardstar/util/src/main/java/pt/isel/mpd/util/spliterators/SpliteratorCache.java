package pt.isel.mpd.util.spliterators;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class SpliteratorCache<T> implements Spliterator<T> {
    private final Spliterator<T> src;
    private final List<T> cache;
    private boolean isCacheFull = false;

    public SpliteratorCache(Stream<T> src) {
        this.src = src.spliterator();
        cache = new ArrayList<T>();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return isCacheFull ? cache.spliterator() :
                new Spliterators.AbstractSpliterator<T>(estimateSize(), characteristics()) {

                    private int counter = 0;

                    @Override
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if (counter >= cache.size()) {
                            //Object[] obj = new Object[1];
                            if (!src.tryAdvance((t) -> {
                                cache.add(t);
                                action.accept(t);
                            })) {
                                isCacheFull = true;
                                return false;
                            }
                            // Correto?
                            //cache.add((T) obj[0]);
                            counter++;
                            return true;
                        }
                        T element = cache.get(counter++);
                        if (element == null)
                            return false;
                        action.accept(element);
                        return true;
                    }
                };
    }

    @Override
    public long estimateSize() {
        return src.estimateSize();
    }

    @Override
    public int characteristics() {
        return src.characteristics();
    }


}
