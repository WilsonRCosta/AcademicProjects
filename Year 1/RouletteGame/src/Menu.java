public class Menu {
    private static int M = 0x80; //input

    public static boolean accessMenu(){
        if(Hal.isBit(M)){
            while (Hal.isBit(M));
            return true;
        }
        return false;
    }
}