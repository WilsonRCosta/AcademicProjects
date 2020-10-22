package pt.isel.poo.li21d.g14.snake.view.CellTiles;


import android.graphics.Canvas;
import android.graphics.Color;

import pt.isel.poo.li21d.g14.snake.view.CellTileView;


public class EmptyTileView extends CellTileView {

    public EmptyTileView() { }

    public void draw(Canvas canvas, int side) {
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0,0,side,side,paint);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }


}
