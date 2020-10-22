package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.request.MockRequest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.LazyQueries.*;


public class BgaWebApiTest {

    private final BgaWebApi api;
    private static final String ARTIST = "Dimitri Bielak";
    private static final String ADVENTURE = "KUBCKBkGxV";
    private static final String CARD_GAME = "eX8uuNlQkQ";

    public BgaWebApiTest() {
        api = new BgaWebApi(new MockRequest()); // <-- CHANGE REQUEST TYPE HERE
    }

    @Test
    public void testGetFirst5Categories() {
        Iterator<String> expected = Arrays.asList("123", "4x", "Abstract", "Adventure", "Age of Reason").iterator();
        for (CategoryDto categoryDto : limit(api.getCategories(), 5)) {
            assertEquals(categoryDto.getName(), expected.next());
        }
    }

    @Test
    public void testGetFirstCategoryStartingWithW() {
        Optional<CategoryDto> first = first(filter(api.getCategories(), cat -> cat.getName().charAt(0) == 'W'));
        first.ifPresentOrElse(cat -> assertEquals(cat.getName(), "War"), Assert::fail);
    }

    @Test
    public void testSearchFirst5GamesOfArtist() {
        Iterator<String> expected = Arrays.asList("Inis","Kemet", "Kemet: Ta-Seti", "Relic", "The Gathering Storm").iterator();
        Iterable<GameDto> actual = limit(api.searchByArtist(0, ARTIST), 5);
        for (GameDto gameDto : actual) {
            assertEquals(gameDto.getName(), expected.next());
        }
    }

    @Test
    public void testSearchGamesByAdventureAndCardGame() {
        Iterator<String> expected = Arrays.asList("Arkham Horror: The Card Game", "Friday",
                "Arkham Horror", "The 7th Continent", "Shadowrun: Crossfire").iterator();
        Iterable<GameDto> actual = limit(api.searchByCategories(0, ADVENTURE, CARD_GAME), 5);
        for (GameDto gameDto : actual) {
            assertEquals(gameDto.getName(), expected.next());
        }
    }
}
