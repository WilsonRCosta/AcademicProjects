package pt.isel.poo.li21d.g14.snake.model;


import pt.isel.poo.li21d.g14.snake.model.Cells.*;

public abstract class Cell {
    public boolean snakeisdead;

    public int posl;
    public int posc;
    public static Dir dir;
    public static Cell newInstance(char type) {
        if(type=='A') return new Apple();
        else if(type=='@') return new Head(Dir.UP);
        else if(type=='#') return new Tail();
        else if(type=='X') return new Wall();
        else if(type=='M') return new Mouse();
        else if(type=='*') return new Enemy();
        return null;
    }

    public  void move(){ }

    public abstract char getChar();

    public void removeCell(int l, int c){ Level.map[l][c] = null; }

    public void moveCell(Cell cell, int l, int c){ Level.map[l][c] = cell; }

    public static boolean cellIsEmpty(int l, int c){ return Level.map[l][c] == null; }

}
