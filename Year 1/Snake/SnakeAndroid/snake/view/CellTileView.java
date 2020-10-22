package pt.isel.poo.li21d.g14.snake.view;

import android.content.Context;
import android.graphics.Paint;

import pt.isel.poo.li21d.g14.snake.model.Cell;
import pt.isel.poo.li21d.g14.snake.view.CellTiles.EmptyTileView;
import pt.isel.poo.li21d.g14.tile.Tile;

public abstract class CellTileView implements Tile {
    protected Paint paint = new Paint();
    protected Cell cell;
    public static Context ctx;

    public static Tile tileOf(Cell cell) {

        if(cell == null) return new EmptyTileView();
        try {
            String clsName = "pt.isel.poo.li21d.g14.snake.view.CellTiles."+cell.getClass().getSimpleName()+"View";
            return (Tile) Class.forName(clsName).newInstance();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setContext(Context context) {
        ctx = context;
    }

}

