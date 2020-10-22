package isel.poo.snake.view.CellTiles;

import isel.leic.pg.Console;
import isel.poo.snake.model.Cell;
import isel.poo.snake.view.CellTile;

public class Tile_Enemy extends CellTile {

    public Tile_Enemy(Cell cell){
        super(cell);
    }

    public void paint() {
        Console.color(Console.BLACK, Console.DARK_GRAY);
        if(!cell.snakeIsDead)
            print(0, 0, "@");
        else
            print(0, 0, "X");
    }
}
