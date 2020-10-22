package org.isel.boardstar;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;
import pt.isel.mpd.util.asyncrequest.AsyncHttpRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BgaWebApp {

    private static final BgaService service = new BgaService(new AsyncBgaWebApi(new AsyncHttpRequest()));
    private static final int MAX_GAMES_REQUEST = 100;

    private static final String HomeHTML =
                    "<html>" +
                    "<head><title>Boardstar Games</title></head>" +
                    "<body>" +
                    "<h2>Boardstar Games Application</h2>" +
                    "<form action=\"http://localhost:8080/categories\">" +
                    "<input type=\"submit\" value=\"Categories\" />" +
                    "</form>" +
                    "<br/>" +
                    "<h3>Search games by category</h3>" +
                    "<div>" +
                    "<label>Category ID: </label>" +
                    "<input type=\"text\" placeholder=\"Enter ID..\" id=\"id\"/ >" +
                    "<a href='' onclick=\"this.href='http://localhost:8080/categories/'+document.getElementById('id').value+'/games'\"> Search</a>" +
                    "</div>" +
                    "<br/>" +
                    "<h3>Search games by artist</h3>" +
                    "<div>" +
                    "<label>Artist Name: </label>" +
                    "<input type=\"text\" placeholder=\"Enter name..\" id=\"name\"/ >" +
                    "<a href='' onclick=\"this.href='http://localhost:8080/artists/'+document.getElementById('name').value+'/games'\"> Search</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx.contentType("text/html").result(HomeHTML));
        app.get("/categories", ctx -> handlerResponse(getCategories(ctx), ctx));
        app.get("/categories/:id/games", ctx -> handlerResponse(searchByCategories(ctx), ctx));
        app.get("/artists/:id/games", ctx -> handlerResponse(searchByArtist(ctx), ctx));
        app.get("/games/:id/artists", ctx -> handlerResponse(searchGameArtists(ctx), ctx));
    }

    private static void handlerResponse(Observable<String> data, Context context) throws IOException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();
        data
                .doOnSubscribe(disp -> {
                    context.contentType("text/html");
                    context.res.setHeader("Transfer-Encoding", "chunked");
                })
                .doOnNext(item -> {
                    writer.write(item);
                    writer.flush();
                })
                .doOnComplete(() -> cf.complete(null))
                .doOnError(cf::completeExceptionally)
                .subscribe();
    }

    private static Observable<String> getCategories(Context context) {
        String query = context.queryParam("categories");
        if (query != null) {
            String[] ids = query.split(",");
            return service
                    .getCategories()
                    .filter(category -> Arrays.asList(ids).contains(category.getId()))
                    .map(BgaWebApp::showCategoryHtml);
        }
        return service.getCategories().map(BgaWebApp::showCategoryHtml);
    }

    private static Observable<String> searchByCategories(Context context) {
        return showGamesData(
                service.searchByCategories(getLimit(context.queryParam("limit")), context.pathParam("id"))
        );
    }

    private static Observable<String> searchByArtist(Context context) {
        return showGamesData(
                service.searchByArtist(getLimit(context.queryParam("limit")), context.pathParam("id"))
        );
    }

    private static Observable<String> searchGameArtists(Context context) {
        return service.searchByGamesIds(getLimit(context.queryParam("limit")), context.pathParam("id"))
                .flatMap(Game::getArtists)
                .map(BgaWebApp::showArtistHtml);
    }

    private static Observable<String> showGamesData (Observable<Game> games) {
        return games.concatMap(game -> game.getCategories()
                .map(Category::getId)
                .reduce((first, second) -> first + "," + second)
                .toObservable()
                .map(cats -> showGameHtml(game) +
                        toGameArtistsHtml(game) +
                        String.format("<br><a href=/categories?categories=%s>Categories</a></br>", cats))
        );
    }

    private static int getLimit(String limit) {
        return limit == null ? MAX_GAMES_REQUEST : Integer.parseInt(limit);
    }

    private static String showCategoryHtml(Category category) {
        return "<h3>" +
                "<a href=/categories/" + category.getId() + "/games>" + category.getName() + "</a>" +
                "</h3>";
    }

    private static String showArtistHtml(Artist artist) {
        String artistName = artist.getName();
        return "<h3>" +
                "<a href=/artists/" + artistName.replace(" ", "%20") + "/games>" + artistName + "</a>" +
                "</h3>";
    }

    private static String showGameHtml(Game game) {
        return "<h3>" + game.getName() + "</h3>";
    }

    private static String toGameArtistsHtml(Game game) {
        return "<a href=/games/" + game.getId() + "/artists>Artists</a>";
    }
}