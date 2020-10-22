package pt.isel.mpd.util;

import pt.isel.mpd.util.spliterators.SpliteratorCache;
import pt.isel.mpd.util.spliterators.SpliteratorInterleave;

import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static <T> Supplier<Stream<T>> cache(Stream<T> src) {
        SpliteratorCache cache = new SpliteratorCache(src);
        return () -> StreamSupport.stream(cache.trySplit(), false);
    }

    public static <T> Stream<T> interleave(Stream<T> src, Stream<T> other) {
        return StreamSupport.stream(
                new SpliteratorInterleave(src.spliterator(), other.spliterator()), false);
    }

    public static <T> Stream<T> intersection(Stream<T> src, Stream<T> other) {
        Supplier<Stream<T>> cacheOther = cache(other.distinct());
        return src.distinct().filter(e -> cacheOther.get().anyMatch(e::equals));

    }

    public static Stream<String> interleaveHeadersContents (Stream<String> headers, Stream<Stream<String>> contents) {
        Supplier<Stream<String>> supplier = cache(headers);
        // TODO
        return interleave(supplier.get(), contents.flatMap(list -> list));
    }

}
