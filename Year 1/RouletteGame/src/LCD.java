import isel.leic.utils.Time;

public class LCD {
    public static final int LINES = 2, COLS=16;

    public static void init(){
        //4 bits - function set
        Time.sleep(15);
        writeNibble(false,0x03);
        Time.sleep(5);
        writeNibble(false,0x03);
        Time.sleep(1);
        writeNibble(false,0x03);
        writeNibble(false,0x02);
        //8 bits
        writeCMD(0x28); //Function set
        writeCMD(0x08); //display off
        writeCMD(0x01); //display clear
        writeCMD(0x06); //entry mode set
        writeCMD(0x0F); //display on
    }
    private static void writeNibble(boolean rs, int data){
        data<<=1;
        if(rs) data |= 1;
        SerialEmitter.send(SerialEmitter.Destination.LCD , data);
        Time.sleep(10);
    }
    private static void writeByte(boolean RS, int data){
        int aux = data;
        aux>>=4;
        writeNibble(RS,aux);
        Time.sleep(5);
        writeNibble(RS,data);
        Time.sleep(1);
    }

    public static void writeCMD(int data){
        writeByte(false,data);
    }

    private static void writeData(int data){ writeByte(true,data); }

    public static void write(char c){ writeData(c); }

    public static void write(String txt){
        for(int i=0; i<txt.length(); i++) writeData(txt.charAt(i));
    }

    public static void cursor(int lin, int col){
        int db7 = 0x80, db6 = 0x40;
        if(lin>0) writeCMD(db7+db6+col);
        else writeCMD(db7+col);
    }
}
