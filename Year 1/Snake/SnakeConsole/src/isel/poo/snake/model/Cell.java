package isel.poo.snake.model;


import isel.poo.snake.model.Cells.*;

import java.util.LinkedList;

public abstract class Cell {
    public boolean snakeIsDead;
    protected int addTails;
    public int pLine, pCol;
    private int moveLastTailL, moveLastTailC;
    protected Dir dir;
    protected LinkedList<Cell> snakeBody = new LinkedList<>();

    public static Cell newInstance(char type) {
        if(type=='A') return new Apple();
        else if(type=='@') return new Head(Dir.UP);
        else if(type=='#') return new Tail();
        else if(type=='X') return new Wall();
        else if(type=='M') return new Mouse();
        else if(type=='*') return new Enemy();
        return null;
    }

    //redefinido por todas as células que sejam móveis
    public void move(){ }

    public abstract char getChar();

    protected void removeCell(int l, int c){ Level.map[l][c] = null; }

    protected void moveCell(Cell cell, int l, int c){ Level.map[l][c] = cell; }

    protected static boolean cellIsEmpty(int l, int c){ return Level.map[l][c] == null; }

    //mover caudas das cobras
    protected void moveTails(Cell cell, LinkedList<Cell> snakeBody, int nPosL, int nPosC){
        Level.cellMoved(cell.pLine,cell.pCol,nPosL,nPosC,cell);
        removeCell(cell.pLine,cell.pCol);
        int lastLin = cell.pLine, lastCol = cell.pCol;

        if(snakeBody.size()>0)
            updateTail(cell,snakeBody,lastLin,lastCol);

        if(cell.addTails>0)
            createNewTail(cell,snakeBody,lastLin,lastCol);

        cell.pLine = nPosL;
        cell.pCol = nPosC;
        moveCell(cell,cell.pLine,cell.pCol);
    }

    //atualizar ganda célula para a cauda das cobras
    private void updateTail(Cell cell, LinkedList<Cell> snakeBody, int lastLin, int lastCol){
        Cell lastTail = snakeBody.pollLast();
        removeCell(lastTail.pLine,lastTail.pCol);
        moveCell(lastTail,lastLin,lastCol);
        Level.cellMoved(lastTail.pLine,lastTail.pCol,lastLin,lastCol,lastTail);
        cell.moveLastTailL = lastTail.pLine;
        cell.moveLastTailC = lastTail.pCol;
        lastTail.pCol = lastCol;
        lastTail.pLine = lastLin;
        snakeBody.addFirst(lastTail);
    }

    //criar nova célula para a cauda das cobras
    private void createNewTail(Cell cell, LinkedList<Cell> snakeBody, int lastLin, int lastCol) {
        Cell tail = Cell.newInstance('#');
        if(snakeBody.size()==0) {
            Level.putCell(lastLin,lastCol,tail);
            Level.cellCreated(lastLin,lastCol,tail);
        }
        else {
            Level.putCell(cell.moveLastTailL,cell.moveLastTailC,tail);
            Level.cellCreated(cell.moveLastTailL,cell.moveLastTailC,tail);
        }
        --cell.addTails;
        snakeBody.add(tail);
    }
}
