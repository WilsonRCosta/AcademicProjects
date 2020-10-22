package pt.isel.mpd;

import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.AsyncBgaWebApi;
import org.isel.boardstar.BgaService;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.asyncrequest.AsyncHttpRequest;
import pt.isel.mpd.util.asyncrequest.AsyncRequest;

import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;

public class AsyncServiceTest {

    static class MediatorRequest implements AsyncRequest {
        final AsyncRequest req;
        int count = 0;

        public MediatorRequest(AsyncRequest req) {
            this.req = req;
        }

        @Override
        public CompletableFuture<Reader> getBody(String path) {
            count++;
            System.out.println(path);
            return req.getBody(path);
        }

        @Override
        public void close() throws Exception {
            req.close();
        }
    }

    private static final String ARTIST = "Zoe Robinson";
    private static final String FANTASY = "ZTneo8TaIO";
    private static final String STRATEGY = "O0ogzwLUe8";

    private final BgaService httpService;
    private final MediatorRequest asyncHttp;

    public AsyncServiceTest() {
        asyncHttp = new MediatorRequest(new AsyncHttpRequest());
        httpService = new BgaService(new AsyncBgaWebApi(asyncHttp));
    }

    @Test
    public void testGetFirst3Categories() {
        Iterable<Category> actual = httpService
                .getCategories()
                .take(3)
                .blockingIterable();
        Iterator<String> expected = Arrays.asList("4x", "Abstract", "Adventure").iterator();
        actual.forEach(category -> assertEquals(category.getName(), expected.next()));
    }

    @Test
    public void testSearchArtistCoworkersInAllGames() {
        Observable<Game> gameObservable = httpService.searchByArtist(60, ARTIST);
        assertEquals(0, asyncHttp.count);

        Iterable<Game> games = gameObservable.blockingIterable();
        assertEquals(0, asyncHttp.count);

        games.forEach(game -> System.out.println(game.getName()));
        assertEquals(4, asyncHttp.count);
        // 7 games encountered -> Api delivers 15 games per page
        // 60/15 = 4 requests -> 1 request with 7 games
        // 2nd, 3rd, 4th request with 0 games
    }

    @Test
    public void testSearchStrategyAndFantasyGames() {
        Observable<Game> gameObservable = httpService.searchByCategories(60, STRATEGY, FANTASY);
        assertEquals(0, asyncHttp.count);

        Iterable<Game> games = gameObservable.blockingIterable();
        assertEquals(0, asyncHttp.count);

        games.forEach(game -> {
            System.out.println(game.getName());
        });
        assertEquals(4, asyncHttp.count);
        // 14 games encountered -> Api delivers 15 games per page
        // 60/15 = 4 requests -> 1 request with 14 games
        // 2nd, 3rd, 4th request with 0 games
    }

}
