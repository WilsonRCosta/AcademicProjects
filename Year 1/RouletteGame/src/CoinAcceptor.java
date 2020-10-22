public class CoinAcceptor {

    private static final int Coin = 0x40; //input
    private static final int accept = 0x40; //output

    public static boolean accepted(){
        if(Hal.isBit(Coin)) {
            Hal.setBits(accept);
            while (Hal.isBit(Coin)) ;
            Hal.clrBits(accept);
            return true;
        }
        else {
            Hal.clrBits(accept);
            return false;
        }
    }
}
