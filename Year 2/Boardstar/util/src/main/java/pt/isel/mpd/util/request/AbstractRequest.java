package pt.isel.mpd.util.request;

import pt.isel.mpd.util.iterators.IteratorReader;
import pt.isel.mpd.util.spliterators.SpliteratorReader;

import java.io.Reader;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractRequest implements Request {

    @Override
    public abstract Reader getReader(String path);

    @Override
    final public Iterable<String> getLines(String path) {
        return () -> new IteratorReader(getReader(path));
    }

    @Override
    final public Stream<String> stream(String path) {
        Supplier<Reader> sup = () -> getReader(path);
        return StreamSupport.stream(new SpliteratorReader(sup), false);
    }

}
