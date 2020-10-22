package pt.isel.mpd;

import org.isel.boardstar.AsyncBgaWebApi;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.junit.Test;
import pt.isel.mpd.util.asyncrequest.AsyncHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;


public class AsyncWebApiTest {

    private static final String ARTIST = "Dimitri Bielak";
    private static final String ADVENTURE = "KUBCKBkGxV";
    private static final String CARD_GAME = "eX8uuNlQkQ";
    private final AsyncBgaWebApi api;

    public AsyncWebApiTest() {
        api = new AsyncBgaWebApi(new AsyncHttpRequest()); // <-- CHANGE REQUEST TYPE HERE
    }

    @Test
    public void testGetCategories() throws ExecutionException, InterruptedException {
        List<String> expected = List.of("4x", "Abstract", "Adventure", "Age of Reason", "Aliens");
        List<CategoryDto> categoryDtos = api.getCategories().get();
        List<String> cNames = new ArrayList<>();
        categoryDtos.forEach(catDto -> {
            cNames.add(catDto.getName());
            System.out.println(catDto.getName());
        });
        assertEquals(expected, cNames.subList(0, 5));
    }

    @Test
    public void testSearchGamesByArtist() throws ExecutionException, InterruptedException {
        List<String> expected = List.of("Inis", "Kemet", "Kemet: Ta-Seti", "Relic", "The Gathering Storm");
        List<GameDto> actual = api.searchByArtist(0, ARTIST).get();
        List<String> cNames = new ArrayList<>();
        actual.forEach(catDto -> {
            cNames.add(catDto.getName());
            System.out.println(catDto.getName());
        });
        assertEquals(expected, cNames.subList(0, 5));
    }

    @Test
    public void testSearchGamesByAdventureAndCardGame() throws ExecutionException, InterruptedException {
        List<String> expected = List.of("Arkham Horror: The Card Game", "Friday",
                "Arkham Horror", "Elder Sign", "The 7th Continent");
        List<GameDto> actual = api.searchByCategories(0, ADVENTURE, CARD_GAME).get();
        List<String> cNames = new ArrayList<>();
        actual.forEach(catDto -> {
            cNames.add(catDto.getName());
            System.out.println(catDto.getName());
        });
        assertEquals(expected, cNames.subList(0, 5));
    }
}
