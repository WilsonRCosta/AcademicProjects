package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.console.tile.Tile;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Apple extends CellTile {
    public Tile_Apple(Cell cell){
        super(cell);
    }

    public void paint() {
        Console.color(Console.BLACK,Console.LIGHT_GRAY);
        print(0,0, "O");
    }
}
