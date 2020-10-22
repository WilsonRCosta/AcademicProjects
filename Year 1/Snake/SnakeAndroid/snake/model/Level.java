package pt.isel.poo.li21d.g14.snake.model;

import pt.isel.poo.li21d.g14.snake.model.Cells.*;

import java.util.Iterator;
import java.util.LinkedList;

public class Level {

    public static Cell[][] map;
    public static int height;
    public static int width;
    public int level;
    public static Observer observer;


    public static LinkedList<Cell> heads;
    public Level(int levelNumber, int height, int width) {
        Apple.initApples =0;
        Apple.remainingApples =10;
        heads=new LinkedList<>();
        level=levelNumber;
        this.height=height;
        this.width=width;
        map=new Cell[height][width];
    }

    public void init(Game game) { }

    public int getWidth() {//todo
        return width;
    }

    public int getHeight() { //todo
        return height;
    }

    public int getNumber() {//todo
        return level;
    }

    public int getRemainingApples() {//todo
        return Apple.remainingApples;
    }

    public void step() {
        for (Iterator<Cell> it = heads.iterator(); it.hasNext() ; ) {
            Cell cell = it.next();
            if (map[cell.posl][cell.posc]!=cell){
                it.remove();
            }
            else cell.move();
        }
    }

    public boolean levelFinished() { return ((getRemainingApples()==0)); }

    public static boolean snakeIsDead() {
        Cell head=heads.getFirst();
        for (Iterator<Cell> it = heads.iterator(); it.hasNext() ; ) {
            head= it.next();
            if(head instanceof Head){
                break;
            }
        }
        return head.snakeisdead;
    }

    public void setSnakeDirection(Dir snakeDirection) {
        Cell head=heads.getFirst();
        for (Iterator<Cell> it = heads.iterator(); it.hasNext() ; ) {
            head= it.next();
            if(head instanceof Head){
                break;
            }
        }
        head.dir=snakeDirection;
    }

    public static void putCell(int l, int c, Cell cell) {
        map[l][c]=cell;
        cell.posc=c;
        cell.posl=l;
        if (cell instanceof Head)heads.add(cell);
        if (cell instanceof Apple) Apple.initApples++;
        if (cell instanceof Mouse)heads.add(cell);
        if (cell instanceof Enemy)heads.add(cell);
    }

    public static Cell getCell(int l, int c) {
        return map[l][c];

    }
    public static void cellRemoved(int l, int c){
        if (observer!=null)
            observer.cellRemoved(l,c);
    }
    public static void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell){
        if (observer!=null)
            observer.cellMoved(fromL,fromC,toL,toC,cell);
    }
    public static void cellCreated(int l, int c, Cell cell){
        if (observer!=null)
            observer.cellCreated(l,c,cell);
    }

    public  interface Observer {

        void cellCreated(int l, int c, Cell cell);
        void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell);

        void cellRemoved(int l, int c);
    }
    public void setObserver(Observer l) { this.observer=l; }
}
