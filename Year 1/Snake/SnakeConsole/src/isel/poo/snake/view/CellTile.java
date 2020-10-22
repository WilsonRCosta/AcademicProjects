package isel.poo.snake.view;

import isel.poo.console.tile.Tile;
import isel.poo.snake.model.Cell;
import isel.poo.snake.model.Cells.*;
import isel.poo.snake.view.CellTiles.*;

public class CellTile extends Tile {
    public static int SIDE=1;
    protected Cell cell;
    public CellTile(Cell cell) {
        this.cell=cell;
        setSize(SIDE,SIDE);
    }

    public static Tile tileOf(Cell cell) {

      if(cell instanceof Apple) return new Tile_Apple(cell);
      else if(cell instanceof Head) return new Tile_Head(cell);
      else if(cell instanceof Tail) return new Tile_Tail(cell);
      else if(cell instanceof Wall) return new Tile_Wall(cell);
      else if(cell instanceof Mouse) return new Tile_Mouse(cell);
      else if(cell instanceof Enemy) return new Tile_Enemy(cell);
      return new EmptyTile();
    }
}
