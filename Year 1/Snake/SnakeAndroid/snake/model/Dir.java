package pt.isel.poo.li21d.g14.snake.model;


public enum Dir {
        UP(-1,0) , RIGHT(0,1),
        DOWN(1,0), LEFT(0,-1);

        public final int dLine;
        public final int dCol;

        Dir(int dl, int dc) {
            dLine = dl;
            dCol = dc;
        }

        public static Dir get(int dLine, int dCol) {
            for (Dir d : values())
                if (d.dCol==dCol && d.dLine==dLine) return d;
            return null;
        }
    public static Dir getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
    }


