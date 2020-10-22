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

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;
import org.isel.boardstar.model.Artist;
import org.isel.boardstar.model.Category;
import org.isel.boardstar.model.Game;

import java.util.Collection;
import java.util.stream.Stream;

import static pt.isel.mpd.util.StreamUtils.cache;

public class BgaService {
    private final BgaApi api;

    public BgaService(BgaApi api) {
        this.api = api;
    }

    private Stream<Category> categoryStream;
    private Stream<CategoryDto> categoryDtoStream;

    private Stream<CategoryDto> getCategoriesDto() {
        if (categoryDtoStream == null) categoryDtoStream = cache(api.getCategories().stream()).get();
        return categoryDtoStream;
    }

    public Stream<Category> getCategories() {
        if (categoryStream == null) categoryStream = cache(getCategoriesDto().map(this::toCategory)).get();
        return categoryStream;
    }

    public Stream<Game> searchByArtist(String artist) {
        return Stream
                .iterate(1, seed -> ++seed)
                .map(i -> api.searchByArtist(i, artist))
                .takeWhile(l -> !l.isEmpty())
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    public Stream<Game> searchByCategories(String... categoriesId) {
        return Stream
                .iterate(1, seed -> ++seed)
                .map(i -> api.searchByCategories(i, categoriesId))
                .takeWhile(l -> !l.isEmpty())
                .flatMap(Collection::stream)
                .map(this::toGame);
    }

    private Game toGame(GameDto gameDto) {
        return new Game(
                gameDto.getId(),
                gameDto.getName(),
                gameDto.getYearPublished(),
                gameDto.getDescription(),
                gameDto.getCategories()
                        .stream()
                        .map(CategoryDto::getId)
                        .map(this::findCategoryDtoFromId)
                        .map(this::toCategory),
                gameDto.getArtists()
                        .stream()
                        .map(this::toArtist)
        );
    }

    private Artist toArtist(String artist) {
        return new Artist(artist, searchByArtist(artist));
    }

    private Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName(),
                searchByCategories(categoryDto.getId())
        );
    }

    private CategoryDto findCategoryDtoFromId(String categoryID) {
        return getCategoriesDto()
                .filter(categoryDto -> categoryDto.getId().equals(categoryID))
                .findFirst()
                .orElse(new CategoryDto(categoryID, "Category Not Found", false));
    }
}
