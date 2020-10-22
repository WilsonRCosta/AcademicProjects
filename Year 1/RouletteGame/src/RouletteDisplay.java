import isel.leic.utils.Time;

public class RouletteDisplay {

    public static int[] display_number = new int[24];
    public static int off;

    public static void init() {
        for (int i = 0; i < display_number.length; i++) display_number[i] = i;
        off = display_number[23]; //shutdown display
    }

    public static void showNumber(int number) {
        SerialEmitter.send(SerialEmitter.Destination.RDIsplay, display_number[number]);
    }

    public static void animation() {
        int startup = 0;
        while (startup < (int) (Math.random() * 4)) {
            for (int i = display_number[10]; i < display_number[16]; i++) {
                showNumber(i);
                Time.sleep(200);
            }
            startup++;
        }
    }
}