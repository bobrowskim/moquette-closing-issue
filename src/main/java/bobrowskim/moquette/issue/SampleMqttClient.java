package bobrowskim.moquette.issue;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class SampleMqttClient implements MqttCallback {
    
    private final static String TOPIC = "topic";
    
    private final MqttClient client;
    private final List<Consumer<String>> consumers = new ArrayList<>();
    
    private SampleMqttClient() throws MqttException {
        String clientId = String.valueOf(System.currentTimeMillis());
        client = new MqttClient("tcp://127.0.0.1:1883", clientId, new MemoryPersistence());
    }
    
    static SampleMqttClient make() throws MqttException {
        SampleMqttClient mqtt = new SampleMqttClient();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setConnectionTimeout(3);
        connOpts.setAutomaticReconnect(true);
        mqtt.client.setCallback(mqtt);
        mqtt.client.connect(connOpts);
        System.out.println(String.format("Client [%s], connected: %b", mqtt.client.getClientId(), mqtt.client.isConnected()));
        if (mqtt.client.isConnected()) {
            mqtt.client.subscribe(TOPIC);
        }
        return mqtt;
    }
    
    public void publishSample() {
        try {
            MqttMessage mqttMessage =  new MqttMessage();
            mqttMessage.setQos(0);
            mqttMessage.setPayload("Message".getBytes());
            client.publish(TOPIC, mqttMessage);
        } catch (MqttException e) { System.err.println(e.getMessage()); }
    }
    
    public void addListener(Consumer<String> consumer) {
        consumers.add(consumer);
    }
    
    public void disconnect() throws MqttException {
        client.disconnect();
        client.close(true);
    }
    
    @Override
    public void connectionLost(Throwable throwable) { }
    
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if (TOPIC.equals(s)) {
            String msg = new String(mqttMessage.getPayload());
            consumers.forEach(consumer -> consumer.accept(msg));
        }
    }
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            System.out.println("Message delivered: " + iMqttDeliveryToken.getMessage().toString());
        } catch (MqttException ignored) { }
    }
}
