package view;

import controller.Bubbles;
import isel.leic.pg.Console;

public class Panel {
    private static final char BUBBLE = '\u25CF';

    public static final int
            LINES_NUM = Math.max(Bubbles.LINES + 2, 10),
            MESSAGE_SIZE = 5,
            COLS = Bubbles.COLS + 3 + MESSAGE_SIZE + 1;

    private static final int
            BOARD_LINE = 1 + (LINES_NUM - Bubbles.LINES - 2) / 2,
            BOARD_COL = 1,
            TITLE_COL = Bubbles.COLS + 3,
            LEVEL_LINE = 1,
            TOT_POINTS_LINE = 4,
            GRP_POINTS_LINE = 7;

    private static int level, totalPoints, groupPoints;

    private static final int[] colors = {
            Console.RED, Console.GREEN, Console.BLUE, Console.CYAN,
            Console.PINK, Console.ORANGE, Console.BROWN, Console.MAGENTA, Console.WHITE
    };

    public static void open() {
        Console.fontSize(30);
        Console.open("Bubbles Breaker", LINES_NUM, COLS);
        Console.exit(true);
        drawBoardLimits();
        drawTitles();
    }

    public static void close() { Console.close(); }

    public static void drawBubble(int l, int c, int color, boolean cursor, boolean group) {
        Console.cursor(l + BOARD_LINE, c + BOARD_COL);
        int background = cursor ? Console.LIGHT_GRAY : (group ? Console.GRAY : Console.BLACK);
        int font = (color == 0) ? Console.BLACK : colors[color - 1];
        Console.color(font, background);
        Console.print(color == 0 ? ' ' : BUBBLE);
    }

    private static void drawBoardLimits() {
        Console.color(Console.WHITE, Console.BLACK);

        Console.cursor(BOARD_LINE - 1, BOARD_COL);
        for (int i = 0; i < Bubbles.COLS; i++) Console.print('-');

        Console.cursor(BOARD_LINE + Bubbles.LINES, BOARD_COL);
        for (int i = 0; i < Bubbles.COLS; i++) Console.print('-');

        for (int l = 0; l < Bubbles.LINES; l++) {
            Console.cursor(BOARD_LINE + l, BOARD_COL - 1);
            Console.print('|');
            Console.cursor(BOARD_LINE + l, BOARD_COL + Bubbles.COLS);
            Console.print('|');
        }
    }

    private static void drawTitles() {
        Console.color(Console.WHITE, Console.GRAY);

        Console.cursor(LEVEL_LINE, TITLE_COL);
        printCentered("LEVEL", MESSAGE_SIZE);

        Console.cursor(TOT_POINTS_LINE, TITLE_COL);
        printCentered("TOTAL", MESSAGE_SIZE);

        Console.cursor(GRP_POINTS_LINE, TITLE_COL);
        printCentered("GROUP", MESSAGE_SIZE);

        updateLevel(level);
        updateTotal(totalPoints);
        updateGroup(groupPoints);
    }

    public static void updateLevel(int value) {
        level = value;
        updateValue(value, LEVEL_LINE);
    }

    public static void updateTotal(int value) {
        totalPoints = value;
        updateValue(value, TOT_POINTS_LINE);
    }

    public static void updateGroup(int value) {
        groupPoints = value;
        updateValue(value, GRP_POINTS_LINE);
    }

    public static void updateValue(int value, int line) {
        Console.color(Console.YELLOW, Console.DARK_GRAY);
        Console.cursor(line + 1, TITLE_COL);
        printCentered("" + value, MESSAGE_SIZE);
    }

    public static void printMessage(String txt) {
        clearArea();
        Console.color(Console.RED, Console.YELLOW);
        String[] words = txt.split(" ");
        int line = (LINES_NUM - words.length) / 2;
        for (int i = 0; i < words.length; i++) {
            Console.cursor(line + i, TITLE_COL - 1);
            printCentered(words[i], MESSAGE_SIZE + 1);
        }
        while (Console.isKeyPressed()) ;
        Console.waitKeyPressed(5000);
        clearArea();
        drawTitles();
    }

    private static void clearArea() {
        Console.color(Console.BLACK, Console.BLACK);
        for (int l = 0; l < LINES_NUM; l++) {
            Console.cursor(l, TITLE_COL - 1);
            for (int i = 0; i < MESSAGE_SIZE + 2; i++) Console.print(' ');
        }
    }

    private static void printSpaces(int size) {
        for (int i = 0; i < size; i++) Console.print(' ');
    }

    private static void printCentered(String txt, int field) {
        int len = txt.length();
        int half = (field - len) / 2;
        printSpaces(field - len - half);
        Console.print(txt);
        printSpaces(half);
    }
}