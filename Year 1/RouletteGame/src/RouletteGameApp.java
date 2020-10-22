import isel.leic.utils.Time;

public class RouletteGameApp {

    // private static final char[] BetNumber = {KBD.key[11],KBD.key[12],KBD.key[8],KBD.key[4],KBD.key[13],KBD.key[9],KBD.key[5],KBD.key[14],KBD.key[10],KBD.key[6]};
    private static final char[] BetNumber = {'0','1','2','3','4','5','6','7','8','9'};
    private static final int randomNumber = (int)(Math.random()*9);
    private static char keyboard;
    private static int coins = 0;
    private static int loss;
    private static int totalGames;
    private static int totalCoins;
    private static int[] bets = new int[10];
    private static int[] counter = new int[10];
    private static int[] coinsOfNumber = new int[10];

    public static void main(String[] args) {
        init();
        TUI.startGame();
        initialize();
    }

    private static void init(){ TUI.init(); RouletteDisplay.init(); }

    private static void initialize(){
        TUI.showCredits(coins);
        keyboard = KBD.getKey();
        while( keyboard != '*' || coins == 0) {
            if(Menu.accessMenu()) menu();
            keyboard = KBD.getKey();
            if (CoinAcceptor.accepted()){
                coins+=2;
                totalCoins++;
                TUI.showCredits(coins);
            }
        }
        if(keyboard == '*'){
           TUI.showBets();
           TUI.showCredits(coins);
           play();
           TUI.startGame();
           initialize();
        }
    }

    private static void play(){
        totalGames++;
        loss = 0;
        boolean end = false;
        int[] aux = new int[counter.length];
        counter = aux;
        while(!end){
            keyboard = KBD.getKey();
            if(keyboard != '*' && keyboard != '#')
                for(int i = 0;  i < BetNumber.length; ++i)
                    if(keyboard == BetNumber[i]) {
                        if(counter[i]>=9) break;
                        if(coins>=1){
                            bets[i]++;
                            if(randomNumber == counter[i]) coinsOfNumber[i] = counter[i]*2;
                            coins--;
                            loss++;
                            TUI.showCredits(coins);
                        }
                        else { numberGenerator(coins); end = true; break; }
                        counter[i] += 1;
                        if(counter[i]>=10) break;
                        LCD.cursor(0,i);
                        LCD.write(counter[i]+"");
                    }
            if(keyboard == '#') {numberGenerator(coins); break;} //acaba a aposta e gera o numero
        }
    } //insere no lcd os valores apostados pelo jogador no keyboard e decrementa os créditos

    private static void numberGenerator(int decrease){
        int time = 5;
        RouletteDisplay.animation();
        for (int i = randomNumber; i < 10; i++) { //ciclo percorre os numeros até chegar ao numero sorteado
            RouletteDisplay.showNumber(i);
            Time.sleep(time);
            time*=2;

            if(i==9)
                for (int j = 0; j < randomNumber+1; j++) {
                    RouletteDisplay.showNumber(j);
                    if(j==randomNumber) break;
                    Time.sleep(time);
                    time*=2;
                }
        }
        resultCredits(decrease);

        for (int i = 0; i < 10; i++) { //ciclo que coloca o numero intermitente
            RouletteDisplay.showNumber(randomNumber);
            Time.sleep(200);
            RouletteDisplay.showNumber(RouletteDisplay.off);
            Time.sleep(200);
        }
    } //gera o número da roleta

    private static void resultCredits(int decrease){
        int result;
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] != 0 && randomNumber == i) {
                result = decrease + counter[randomNumber] * 2;
                coins += result;
                TUI.showWin(counter[randomNumber] * 2);
                break;
            } else { TUI.showLoss(loss); break; }
        }
    }

    private static void menu(){
        TUI.showTitle();
        keyboard = KBD.getKey();
        while (true){
            TUI.showMenu();
            keyboard = KBD.getKey();
            if(keyboard == '*') {
                coins = 100;
                break;
            }
            if(keyboard == '8'){
                TUI.showConfirmation();
                exit();
            }
            if(keyboard == '0') {
                stats();
                Time.sleep(5000);
                menu();
            }
            if(keyboard == '#') {
                count();
                Time.sleep(5000);
                menu();
            }
        }
    }

    private static void count(){
        TUI.clearDisplay();
        LCD.write("Games:"+totalGames);
        LCD.cursor(1,0);
        LCD.write("Coins:"+totalCoins);
    }

    private static void stats(){
        TUI.clearDisplay();

        for (int i = 0; i < 10; i++) {
            keyboard = KBD.getKey();
            while(true){
                keyboard = KBD.getKey();
                if(keyboard == '8'){ keyboard = KBD.getKey(); LCD.cursor(i+1,0); break; }
                if(keyboard == '2'){ keyboard = KBD.getKey(); LCD.cursor(i-1,0); break; }
                else { LCD.cursor(i,0); break; }
            }
            LCD.write(i + ": -> " + bets[i] + " $:" + coinsOfNumber[i]);
        }
    }

    private static void exit(){
        keyboard = KBD.getKey();
        while(true) {
            keyboard = KBD.getKey();
            if (keyboard == '5') {
                TUI.shutDownLcd();
                RouletteDisplay.showNumber(RouletteDisplay.off);
                break;
            } else menu();
        }
    }
}