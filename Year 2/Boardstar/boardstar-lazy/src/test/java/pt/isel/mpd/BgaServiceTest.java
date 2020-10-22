package pt.isel.mpd;

import org.isel.boardstar.BgaService;
import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import org.junit.Test;
import pt.isel.mpd.util.request.AbstractRequest;
import pt.isel.mpd.util.request.MockRequest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.LazyQueries.*;

public class BgaServiceTest {

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
    private final BgaService service;
    private final RequestMediator req;

    private static final String ARTIST = "Zoe Robinson";
    private static final String FANTASY = "ZTneo8TaIO";
    private static final String STRATEGY = "O0ogzwLUe8";

    public BgaServiceTest() {
        req = new RequestMediator(new MockRequest()); // <-- CHANGE REQUEST TYPE HERE
        service = new BgaService(new BgaWebApi(req));
    }

    @Test
    public void testGetCategoriesStartingWithLetterW() {
        Iterable<Category> actual = limit(
                filter(service.getCategories(), c -> c.getName().charAt(0) == 'W'),3);
        Iterator<String> expected = Arrays.asList("War", "Wargame", "Werewolves").iterator();

        actual.forEach((category -> assertEquals(category.getName(),expected.next())));
    }

    @Test
    public void testSearchArtistCoworkersInAllGames() {
        Iterable<Artist> actual = filter(flatMap(service.searchByArtist(ARTIST), Game::getArtists),
                        artist -> !artist.getName().equals(ARTIST));

        List<String> artistNames = new ArrayList<>();
        actual.forEach((a) -> artistNames.add(a.getName()));

        assertEquals(78, count(distinct(artistNames)));
    }

    @Test
    public void testSearchStrategyAndFantasyGamesOfCurrentYear() {
        Iterable<Game> actual = filter(
                service.searchByCategories(STRATEGY,FANTASY), game -> game.getYear() == 2020);
        Iterator<String> expected = Arrays.asList("Super Fantasy Brawl","Bellum of Mutants and Men","Obsidia").iterator();
        actual.forEach(game -> assertEquals(game.getName(), expected.next()));
    }

    @Test
    public void testLazyGetAuthorGames() {
        Iterator<Game> games = service.searchByArtist("Dimitri Bielak").iterator();
        int count = 0;
        int games_number_per_page = 15;

        // 1st page (15 games) -> 1 IO access
        while (count++ < games_number_per_page && games.hasNext())
            games.next();

        assertEquals(1, req.count);

        // 2nd page (4 games) -> 1 IO access
        games_number_per_page = 4;
        count = 0;
        while (count++ < games_number_per_page && games.hasNext())
            games.next();

        assertEquals(2, req.count);

        // 3rd page (0 games) -> 1 IO access
        while (games.hasNext()) games.next();

        assertEquals(3, req.count);
    }

    @Test
    public void testCacheGetCategories() {
        Iterable<Category> cache = cache(service.getCategories());
        Object[] expected = toArray(cache);
        assertEquals(1, req.count);
        Object[] actual = toArray(cache);
        assertEquals(1, req.count);
        assertArrayEquals(expected, actual);
    }
}
