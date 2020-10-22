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

import io.reactivex.rxjava3.core.Observable;
import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BgaService {
    private final BgaApi api;

    public BgaService(BgaApi api) {
        this.api = api;
    }

    private Observable<Category> category;
    private Observable<CategoryDto> categoryDto;

    private Observable<CategoryDto> getCategoriesDto() {
        if (categoryDto == null)
            categoryDto = fromCF(api.getCategories())
                    .flatMap(Observable::fromIterable)
                    .cache();
        return categoryDto;
    }

    public Observable<Category> getCategories() {
        if (category == null)
            category = fromCF(api.getCategories())
                    .flatMap(Observable::fromIterable)
                    .map(this::toCategory)
                    .cache();
        return category;
    }

    public Observable<Game> searchByArtist(int numOfGames, String artist) {
        return Observable
                .intervalRange(1, numOfGames / api.LIMIT_DEFAULT + 1, 0, 500, TimeUnit.MILLISECONDS)
                .map(i -> api.searchByArtist(i.intValue(), artist))
                .flatMap(BgaService::fromCF)
                //.takeWhile(list -> !list.isEmpty())
                .flatMap(Observable::fromIterable)
                .map((gameDto) -> toGame(numOfGames, gameDto));
    }

    public Observable<Game> searchByCategories(int numOfGames, String... categoriesId) {
        return Observable
                .intervalRange(1, numOfGames / api.LIMIT_DEFAULT + 1, 0, 500, TimeUnit.MILLISECONDS)
                .map(i -> api.searchByCategories(i.intValue(), categoriesId))
                .flatMap(BgaService::fromCF)
                //.takeWhile(l -> !l.isEmpty())
                .flatMap(Observable::fromIterable)
                .map((gameDto) -> toGame(numOfGames, gameDto));
    }

    public Observable<Game> searchByGamesIds(int numOfGames, String... gamesIds) {
        return fromCF(api.searchGameByIds(gamesIds))
                .flatMap(Observable::fromIterable)
                .map((gameDto) -> toGame(numOfGames, gameDto));
    }


    private Game toGame(int numOfGames, GameDto gameDto) {
        return new Game(
                gameDto.getId(),
                gameDto.getName(),
                gameDto.getYearPublished(),
                gameDto.getDescription(),
                Observable
                        .fromIterable(gameDto.getCategories())
                        .map(CategoryDto::getId)
                        .flatMap(this::findCategoryDtoFromId)
                        .map(this::toCategory),
                Observable
                        .fromIterable(gameDto.getArtists())
                        .map((artistDto) -> toArtist(numOfGames, artistDto))
        );
    }

    private Artist toArtist(int numOfGames, String artist) {
        return new Artist(artist, searchByArtist(numOfGames, artist));
    }

    private Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName(),
                (size) -> this.searchByCategories(size, categoryDto.getName())
        );
    }

    private Observable<CategoryDto> findCategoryDtoFromId(String categoryID) {
        return getCategoriesDto()
                .filter(categoryDto -> categoryDto.getId().equals(categoryID));
    }

    private static <T> Observable<T> fromCF(CompletableFuture<T> cf) {
        return Observable
                .create(subscriber -> cf
                        .thenAccept(item -> {
                            subscriber.onNext(item);
                            subscriber.onComplete();
                        })
                        .exceptionally(err -> {
                            subscriber.onError(err);
                            return null;
                        })
                );
    }
}
