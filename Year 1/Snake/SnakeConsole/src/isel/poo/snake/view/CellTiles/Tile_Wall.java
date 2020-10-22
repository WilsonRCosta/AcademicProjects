package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Wall extends CellTile {
    public Tile_Wall(Cell cell){
        super(cell);
    }
    public void paint() {

        Console.color(Console.BROWN,Console.BROWN);
        print(0,0, " ");

    }
}
