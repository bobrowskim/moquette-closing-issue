package bobrowskim.moquette.issue;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class NotClosingApplication {
    
    public static void main(String[] args) {
        try {
            new NotClosingApplication();
        } catch (IOException e) { System.err.println(e.getMessage()); }
    }
    
    private NotClosingApplication() throws IOException {
        MoquetteServer server = new MoquetteServer();
        server.start();
        try {
            SampleMqttClient client = SampleMqttClient.make();
            SampleMqttClient publisher = SampleMqttClient.make();
            
            client.addListener(System.out::println);

            publisher.publishSample();
            publisher.publishSample();
            Thread.sleep(1000);
            client.disconnect();
            publisher.disconnect();
        } catch (MqttException | InterruptedException e) { System.err.println(e.getMessage()); }
        
        server.stop();
    }
    
}
