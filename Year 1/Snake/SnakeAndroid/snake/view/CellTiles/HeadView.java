package pt.isel.poo.li21d.g14.snake.view.CellTiles;

import android.graphics.Canvas;
import android.graphics.Color;

import pt.isel.poo.li21d.g14.snake.model.Dir;
import pt.isel.poo.li21d.g14.snake.model.Level;
import pt.isel.poo.li21d.g14.snake.view.CellTileView;

public class HeadView extends CellTileView {

    public void draw(Canvas canvas, int side) {
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(side/2,side/2,side/2,paint);
        paint.setColor(Color.BLACK);
        if(cell.dir== Dir.UP) {
            canvas.drawCircle(side/4,side/4,side/12,paint);
            canvas.drawCircle(3*side/4,side/4,side/12,paint);
        }
        else if(cell.dir==Dir.DOWN){
            canvas.drawCircle(side/4,3*side/4,side/12,paint);
            canvas.drawCircle(3*side/4,3*side/4,side/12,paint);
        }
        else if(cell.dir==Dir.LEFT){
            canvas.drawCircle(side/4,side/4,side/12, paint);
            canvas.drawCircle(side/4,3*side/4,side/12,paint);
        }
        else {
            canvas.drawCircle(3*side/4,side/4,side/12, paint);
            canvas.drawCircle(3*side/4,3*side/4,side/12,paint);
        }
        if(Level.snakeIsDead()) drawCross(canvas,side);
    }
    private void drawCross(Canvas canvas, int side) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        if(cell.dir== Dir.UP) {
            canvas.drawLine(0,0,side/2,side/2,paint);//left
            canvas.drawLine(0,side/2,side/2,0,paint);
            canvas.drawLine(side/2,0,side,side/2,paint);//right
            canvas.drawLine(side/2,side/2,side,0,paint);
        }
        else if(cell.dir==Dir.DOWN){
            canvas.drawLine(0,side/2,side/2,side,paint); //right
            canvas.drawLine(0,side,side/2,side/2,paint);
            canvas.drawLine(side/2,side/2,side,side,paint); //left
            canvas.drawLine(side/2,side,side,side/2,paint);
        }
        else if(cell.dir==Dir.RIGHT){
            canvas.drawLine(side/2,0,side,side/2,paint);//right
            canvas.drawLine(side/2,side/2,side,0,paint);
            canvas.drawLine(side/2,side/2,side,side,paint); //left
            canvas.drawLine(side/2,side,side,side/2,paint);
        }
        else {
            canvas.drawLine(0,0,side/2,side/2,paint);//left
            canvas.drawLine(0,side/2,side/2,0,paint);
            canvas.drawLine(0,side/2,side/2,side,paint); //right
            canvas.drawLine(0,side,side/2,side/2,paint);
        }
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }


}