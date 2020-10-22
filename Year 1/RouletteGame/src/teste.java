import isel.leic.utils.Time;

public class teste {
        public static void main(String[] args) {

//            while(1==1){
//                Time.sleep(2000);
//                SerialEmitter.send(SerialEmitter.Destination.RDIsplay,0x05);
//                LCD.write("oLA");
//            }


            //        Hal.clrBits(0xFF);
        /*int n = 0x01;
        while (UsbPort.in() != 0x80) {
            for (int i = 0; i < 7; i++) {
                if(UsbPort.in() == 0x80) break;
                UsbPort.out(n);
                Time.sleep(1000);
                n = n << 1;
            }

            for (int i = 0; i < 7; i++) {
                if(UsbPort.in() == 0x80) break;
                UsbPort.out(n);
                Time.sleep(1000);
                n = n >> 1;
            }
        }*/

            KBD.init();
            while (true) {
                char k = KBD.getKey();
                if (k != KBD.NONE)
                    System.out.println(k);
                Time.sleep(1);
            }

        }}
