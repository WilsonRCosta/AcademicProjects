package isel.poo.snake.model.Cells;

import isel.poo.snake.model.Cell;
import isel.poo.snake.model.Dir;
import isel.poo.snake.model.Game;
import isel.poo.snake.model.Level;

public class Enemy extends Cell {

    public Enemy(){
            super();
            this.addTails = 4;
        }

    public String toString(){
        return "Enemy";
    }

    public char getChar(){ return '*'; }

    private void setNrTails(int numTails) { this.addTails += numTails; }

    @Override
    public void move() {
        int nPosL = 0, nPosC = 0;

        for (Dir dir : Dir.values()) {
            nPosL = Level.checkLin(this.pLine +dir.dLine);
            nPosC = Level.checkCol(this.pCol +dir.dCol);
            if(cellIsEmpty(nPosL,nPosC)||
                    Level.getCell(nPosL,nPosC) instanceof Mouse ||
                    Level.getCell(nPosL,nPosC) instanceof Apple)
                break;
        }

        if(!snakeIsDead){
            if(!collide(nPosL,nPosC ))
                moveTails(this,this.snakeBody,nPosL,nPosC);
        }
        else if(snakeBody.size()>0){
            Cell elemtail=snakeBody.pollLast();
            removeCell(elemtail.pLine,elemtail.pCol);
            Level.cellRemoved(elemtail.pLine,elemtail.pCol);

        }
    }

    private boolean collide(int nPosL, int nPosC){
        Head head = Level.getSnakeHead();
        Cell common = Level.getCell(nPosL,nPosC);
        if(common != null){
            switch (common.getChar()){
                case 'A':
                    Apple.remainingApples++;
                    Apple.gotEaten();
                    setNrTails(4);
                    break;
                case '@':
                case '#':
                    head.setNrTails(this.snakeBody.size());
                    snakeIsDead = true;
                    break;
                case 'M':
                    setNrTails(10);
                    break;
                case 'X':
                    snakeIsDead = true;
                    break;
            }
        }
        Game.cellUpdated(this.pLine, this.pCol, this);
        return snakeIsDead;
    }
}
