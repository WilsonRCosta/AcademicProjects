import isel.leic.utils.Time;

public class TUI {

    public static void init() {
        Hal.init();
        SerialEmitter.init();
        LCD.init();
    }

    public static void clearDisplay(){ LCD.writeCMD(0x01); } //clear display

    public static void shutDownLcd(){ LCD.writeCMD(0x08); } //display off

    public static void startGame() {
        clearDisplay();
        LCD.cursor(0, 1);
        LCD.write("Roulette Game");
        LCD.cursor(1, 1);
        LCD.write("1 * 2 * 3 *");
    }

    public static void showBets(){
        clearDisplay();
        LCD.cursor(1, 0);
        LCD.write("0123456789");
    }

    public static void showCredits(int coins) {
        String credits = coins+"";
        LCD.cursor(1,LCD.COLS-credits.length()-2);
        LCD.write(" $" + coins);
    }

    public static void showWin(int result){
        String credits = result+"";
        LCD.cursor(0, LCD.COLS-credits.length()-2);
        LCD.write("W$" + result + "");
    }

    public static void showLoss(int result){
        String credits = result+"";
        LCD.cursor(0, LCD.COLS-credits.length()-2);
        LCD.write("L$" + result + "");
    }

    public static void showTitle(){
        clearDisplay();
        LCD.cursor(0,1);
        LCD.write("ON MAINTENANCE");
    }

    public static void showMenu(){
        LCD.cursor(1,0);
        LCD.write("0-Stats  #-Count");
        Time.sleep(2000);
        LCD.cursor(1,0);
        LCD.write("*-Play   8-shutD");
        Time.sleep(2000);
    }

    public static void showConfirmation(){
        char keyboard;
        int time=0;
        clearDisplay();
        LCD.cursor(0,4);
        LCD.write("ShutDown");
        LCD.cursor(1,0);
        LCD.write("5-YES   other-NO");
        while(time<3){
            keyboard = KBD.getKey();
            time++;
            if(keyboard == '5') break;
        }
        Time.sleep(3000);
    }
}