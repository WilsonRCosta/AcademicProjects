import isel.leic.UsbPort;

public class Hal {
    private static int lastBits;

    public static void init(){ lastBits = 0x00; }

    public static void setBits(int value){
        lastBits |= value;
        UsbPort.out(~lastBits);
    }

    public static void clrBits(int value){
        lastBits &= (~value);
        UsbPort.out(~lastBits);
    }

    public static void writeBits(int mask, int value){
        clrBits(mask);
        setBits(mask&value);
    }

    public static int readBits(int mask) {
        return mask & (~UsbPort.in());
    }

    public static boolean isBit(int mask){
        return (mask & (~UsbPort.in()))!=0;
    }
}
