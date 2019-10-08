package bobrowskim.moquette.issue;

import java.io.IOException;

public class StartStopApplication {
    
    public static void main(String[] args) throws InterruptedException {
        MoquetteServer server = new MoquetteServer();
        try {
            server.start();
            Thread.sleep(1000);
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
