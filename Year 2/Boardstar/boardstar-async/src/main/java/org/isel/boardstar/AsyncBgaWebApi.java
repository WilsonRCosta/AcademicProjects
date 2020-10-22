/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2020, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 */

package org.isel.boardstar;

import com.google.gson.Gson;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.dto.GetCategoriesDto;
import org.isel.boardstar.dto.GetGamesDto;
import pt.isel.mpd.util.asyncrequest.AsyncRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AsyncBgaWebApi implements BgaApi {

    private final static String
            HOST = "https://www.boardgameatlas.com/api/",
            PATH_GET_CATEGORIES = "game/categories?client_id=%s",
            PATH_SEARCH_GAMES_BY_ARTIST_NAME = "search?artist=%s&client_id=%s",
            PATH_SEARCH_GAMES_BY_CATEGORIES_ID = "search?categories=%s&client_id=%s",
            PATH_SEARCH_GAME_BY_ID = "search?ids=%s&client_id=%s",
            PATH_SKIP_AND_LIMIT = "&skip=%d&limit=%d",
            CLIENT_ID;

    private final AsyncRequest req;
    private final Gson gson;

    static {
        try (
                InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("CLIENT_ID.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))
        ) {
            CLIENT_ID = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CLIENT_ID.txt. Put your client ID in resources folder.");
        }
    }

    public AsyncBgaWebApi(AsyncRequest req) {
        this.req = req;
        gson = new Gson();
    }

    private String getSkipAndLimitByPageNumber(int pageNumber) {
        return String.format(PATH_SKIP_AND_LIMIT, Math.max((pageNumber - 1), 0) * LIMIT_DEFAULT, LIMIT_DEFAULT);
    }

    @Override
    public CompletableFuture<List<GameDto>> searchByArtist(int pageNumber, String artist) {
        String path = HOST + String.format(PATH_SEARCH_GAMES_BY_ARTIST_NAME, artist.replace(" ", "%20"), CLIENT_ID)
                + getSkipAndLimitByPageNumber(pageNumber);
        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, GetGamesDto.class))
                .thenApply(GetGamesDto::getGames);
    }

    @Override
    public CompletableFuture<List<GameDto>> searchByCategories(int pageNumber, String... categoriesId) {
        String path = HOST + String.format(PATH_SEARCH_GAMES_BY_CATEGORIES_ID, String.join(",", categoriesId), CLIENT_ID)
                + getSkipAndLimitByPageNumber(pageNumber);
        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, GetGamesDto.class))
                .thenApply(GetGamesDto::getGames);
    }

    @Override
    public CompletableFuture<List<CategoryDto>> getCategories() {
        String path = HOST + String.format(PATH_GET_CATEGORIES, CLIENT_ID);
        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, GetCategoriesDto.class))
                .thenApply(GetCategoriesDto::getCategories);
    }

    @Override
    public CompletableFuture<List<GameDto>> searchGameByIds(String... gamesIds) {
        String path = HOST + String.format(PATH_SEARCH_GAME_BY_ID, String.join(",", gamesIds), CLIENT_ID);
        return req
                .getBody(path)
                .thenApply(reader -> gson.fromJson(reader, GetGamesDto.class))
                .thenApply(GetGamesDto::getGames);
    }


}
