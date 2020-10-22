package isel.poo.snake.model;

import isel.poo.snake.model.Cells.*;

import java.util.Iterator;
import java.util.LinkedList;

public class Level {
    static Cell[][] map;
    public static int height;
    public static int width;
    public int level;
    private static Observer observer;


    private static LinkedList<Cell> heads;
    public Level(int levelNumber, int height, int width) {
        Apple.initApples =0;
        Game.setScore(0);
        Apple.remainingApples =10;
        heads=new LinkedList<>();
        level=levelNumber;
        Level.height=height;
        Level.width =width;
        map=new Cell[height][width];


    }

    void init(Game game) { }

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

    public static int checkLin(int nPosL){
        if(nPosL<0) nPosL = Level.height-1;
        else if(nPosL>Level.height-1) nPosL = 0;
        return nPosL;
    }

    public static int checkCol(int nPosC){
        if(nPosC<0) nPosC = Level.width-1;
        else if(nPosC>Level.width-1) nPosC = 0;
        return nPosC;
    }

    public void step() {
        for (Iterator<Cell> it = heads.iterator(); it.hasNext() ; ) {
            Cell cell = it.next();
            if (map[cell.pLine][cell.pCol]!=cell){
                it.remove();
            }
            else cell.move();
        }
    }

    public boolean isFinished() { return (snakeIsDead()||(getRemainingApples()==0)); }

    public boolean snakeIsDead() { return getCellHead(heads.getFirst()).snakeIsDead; }

    public void setSnakeDirection(Dir snakeDirection) { getSnakeHead().dir = snakeDirection; }

    private Cell getCellHead(Cell cell) {
        for (Cell curr : heads) {
            cell = curr;
            if (cell instanceof Head) break;
        }
        return cell;
    }

    public static void putCell(int l, int c, Cell cell) {
        map[l][c]=cell;
        cell.pCol = c;
        cell.pLine = l;
        if (cell instanceof Head)heads.add(cell);
        if (cell instanceof Apple)Apple.initApples++;
        if (cell instanceof Mouse)heads.add(cell);
        if (cell instanceof Enemy)heads.add(cell);
    }

    public static Cell getCell(int l, int c) { return map[l][c]; }

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
    public void setObserver(Observer l) { observer=l; }

    public static Head getSnakeHead(){
        Cell cell;
        for (Iterator<Cell> it = heads.iterator(); it.hasNext(); ){
            cell = it.next();
            if(cell instanceof Head){
                Head head = (Head)cell;
                return head;
            }
        }
        return null;
    }
}
