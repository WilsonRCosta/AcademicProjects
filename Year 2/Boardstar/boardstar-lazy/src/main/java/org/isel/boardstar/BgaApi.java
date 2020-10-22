package org.isel.boardstar;

import org.isel.boardstar.dto.CategoryDto;
import org.isel.boardstar.dto.GameDto;

import java.util.List;

public interface BgaApi {

    List<GameDto> searchByArtist(int pageNumber, String artist);

    List<GameDto> searchByCategories(int pageNumber, String... categoriesId);

    List<CategoryDto> getCategories();
}
