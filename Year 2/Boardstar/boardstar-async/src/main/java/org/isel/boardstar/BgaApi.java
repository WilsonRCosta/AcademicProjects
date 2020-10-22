package org.isel.boardstar;

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BgaApi {

    int LIMIT_DEFAULT = 30;

    CompletableFuture<List<GameDto>> searchByArtist(int pageNumber, String artist);

    CompletableFuture<List<GameDto>> searchByCategories(int pageNumber, String... categoriesId);

    CompletableFuture<List<CategoryDto>> getCategories();

    CompletableFuture<List<GameDto>> searchGameByIds(String... gamesIds);
}
