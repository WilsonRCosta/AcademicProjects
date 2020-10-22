package pt.isel.mpd;

import org.isel.boardstar.BgaWebApi;
import org.isel.boardstar.dto.CategoryDto;
import org.junit.Test;
import pt.isel.mpd.util.request.MockRequest;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class BgaWebApiTest {

    private static final String ARTIST = "Dimitri Bielak";
    private static final String ADVENTURE = "KUBCKBkGxV";
    private static final String CARD_GAME = "eX8uuNlQkQ";
    private final BgaWebApi api;

    public BgaWebApiTest() {
        api = new BgaWebApi(new MockRequest()); // <-- CHANGE REQUEST TYPE HERE
    }

    @Test
    public void testGetFirst5Categories() {
        Stream<String> expected = Stream.of("123", "4x", "Abstract", "Adventure", "Age of Reason");
        Stream<String> actual = api.getCategories().stream().limit(5).map(CategoryDto::getName);
        assertEquals(expected.reduce(String::concat), actual.reduce(String::concat));
    }

    /*
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

     */

}
