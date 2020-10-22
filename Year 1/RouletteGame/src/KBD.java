import isel.leic.utils.Time;

public class KBD {
    private static final int KEY = 0x0F; //input
    private static final int D_VAL = 0x10; //input
    private static final int ACK = 0x80; //output

    public static final char NONE = 0;
    //public static char[] key = new char[]{'*', NONE, NONE, NONE, NONE, '3', '6', '9', '#', '2', '5', '8', '0', '1', '4', '7'};
    public static char[] key = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '0', '#'};
    public static void init() { }

    public static char getKey() {
        if (Hal.isBit(D_VAL)) {
            int current_key = Hal.readBits(KEY);
            Hal.setBits(ACK);
            while (Hal.isBit(D_VAL)) ; //enquanto houver D_VAL
            Hal.clrBits(ACK);
            return key[current_key];
        }
        return NONE;
    }

    public static char waitKey(long timeout){
        timeout += Time.getTimeInMillis();
        while(Time.getTimeInMillis() < timeout){
            char k = getKey();
            if(k != NONE) return k;
        }
        return NONE;
    }
}