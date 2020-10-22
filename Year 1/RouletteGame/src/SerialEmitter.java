//import com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_DOM_SAX_SAXEvent;
import isel.leic.utils.Time;

public class SerialEmitter {

    public static enum Destination {RDIsplay,LCD};

    private static final int SOC = 0x02; //output
    private static final int CLK = 0x04; //output
    private static final int SDX = 0x08; //output

    public static void init(){
        Hal.clrBits(SOC);
        Hal.clrBits(SDX);
        Hal.clrBits(CLK);
    }

    public static void send(Destination addr, int data) {
        int parity = 0;
        int aux = data;

        Hal.setBits(SOC);
        Hal.clrBits(CLK);
        Time.sleep(1);

        if(addr.ordinal() == 1) Hal.setBits(SDX);
        else Hal.clrBits(SDX);

        Hal.setBits(CLK);
        Time.sleep(1);
        Hal.clrBits(CLK);
        Time.sleep(1);

        for(int i=0; i<5; i++){
            if((aux & 1) == 1) {
                Hal.setBits(SDX);
                parity++;
            }
            else Hal.clrBits(SDX);

            aux >>= 1;
            Hal.setBits(CLK);
            Time.sleep(1);
            Hal.clrBits(CLK);
            Time.sleep(1);
        }

        if(parity % 2 == 0) Hal.setBits(SDX);
        else Hal.clrBits(SDX);

        Hal.setBits(CLK);
        Time.sleep(1);
        Hal.clrBits(CLK);
        Time.sleep(1);
        Hal.clrBits(SOC);
    }
}