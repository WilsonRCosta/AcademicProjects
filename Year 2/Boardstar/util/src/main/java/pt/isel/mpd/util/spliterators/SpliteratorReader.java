package pt.isel.mpd.util.spliterators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpliteratorReader extends Spliterators.AbstractSpliterator {
    final Supplier<Reader> sup;
    BufferedReader reader;

    public SpliteratorReader(Supplier<Reader> sup) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        this.sup = sup;
    }

    public BufferedReader getReader() {
        if (reader == null) {
            reader = new BufferedReader(sup.get());
        }
        return reader;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        String line = readLine();
        if (line == null) return false;
        else action.accept(line);
        return true;
    }

    private String readLine() {
        try {
            return getReader().readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
