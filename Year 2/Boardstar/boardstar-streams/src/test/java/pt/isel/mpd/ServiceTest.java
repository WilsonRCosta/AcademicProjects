package pt.isel.mpd;

import org.isel.boardstar.BgaService;
import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.request.AbstractRequest;
import pt.isel.mpd.util.request.HttpRequest;
import pt.isel.mpd.util.request.MockRequest;

import java.io.Reader;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.StreamUtils.*;

public class ServiceTest {

    static class RequestMediator extends AbstractRequest {
        private final AbstractRequest req;
        private int count;

        RequestMediator(AbstractRequest req) {
            this.req = req;
            count = 0;
        }

        @Override
        public Reader getReader(String path) {
            count++;
            return req.getReader(path);
        }
    }

    private static final String ARTIST = "Zoe Robinson";
    private static final String FANTASY = "ZTneo8TaIO";
    private static final String STRATEGY = "O0ogzwLUe8";
    private final BgaService mockService;
    private final BgaService httpService;
    private final RequestMediator mockRequest;
    private final HttpRequest httpRequest;

    public ServiceTest() {
        mockRequest = new RequestMediator(new MockRequest()); // <-- CHANGE REQUEST TYPE HERE
        mockService = new BgaService(new BgaWebApi(mockRequest));
        httpRequest = new HttpRequest();
        httpService = new BgaService(new BgaWebApi(httpRequest));
    }

    @Test
    public void testCacheGetCategories() {
        Supplier<Stream<Category>> supplier = cache(mockService.getCategories());
        Object[] expected = supplier.get().toArray();
        assertEquals(1, mockRequest.count);
        Object[] actual = supplier.get().toArray();
        assertEquals(1, mockRequest.count);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCacheInfiniteSequence() {
        Random r = new Random();
        Stream<Integer> nrs = generate(() -> r.nextInt(100));
        Supplier<Stream<Integer>> nrsSupplier = cache(nrs);
        Object[] expected = nrsSupplier.get().limit(10).toArray();
        Object[] actual = nrsSupplier.get().limit(10).toArray();
        assertArrayEquals(expected, actual);
    }


    @Test
    public void testIntersectionGamesWithFantasyAndStrategy() {
        Supplier<Stream<Game>> sameGames = () -> intersection(
                mockService.searchByCategories(FANTASY).limit(15),
                mockService.searchByCategories(STRATEGY).limit(15)
        );
        Object[] actual = sameGames.get().map(Game::getName).toArray();
        Object[] expected = List.of("Spirit Island", "Gloomhaven").toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInterleaveSequence() {
        Stream<String> src = Stream.of("1", "2", "3");
        Stream<String> other = Stream.of("a", "b", "c", "d", "e", "f");
        Stream<String> expected = Stream.of("1", "a", "2", "b", "3", "c", "d", "e", "f");
        Stream<String> actual = interleave(src, other);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testInterleaveHeadersWithContents() {
        Supplier<Stream<String>> headers = cache(httpService.getCategories().map(c -> c.getName() + ":"));

        Supplier<Stream<Stream<String>>> content = cache(httpService.getCategories()
                .map(Category::getGames) // Stream<Stream<Games>>
                .map(games -> games.limit(10).map(Game::getName))); // Stream<Stream<String>>

        //Stream<String> stringStream = interleaveHeadersContents(headers.get(), content.get());
        //Object[] vals = stringStream.toArray();
    }

}
