package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import time.TimeStamp;

import java.io.PrintWriter;

/**
 * Created by manabu on 2016/06/24.
 */
public class HcrisisPublish {
    public static void publishMsg(String topic, String content, int qos, String broker, String clientID, PrintWriter pw) {

        //String topic        = "MQTT Examples";
        //String content      = "Message from MqttPublishSample";
        //int qos             = 2;
        //String broker       = "tcp://broker.titech.ichilab.org:1883";
        clientID     = clientID + "-" + Integer.toString((int)Math.random()*100);
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientID, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            pw.write(TimeStamp.getTimeStamp() + " : Connecting to broker: "+broker + "\n");
            sampleClient.connect(connOpts);
            pw.write(TimeStamp.getTimeStamp() + " : Connected\n");
            pw.write(TimeStamp.getTimeStamp() + " : Publishing message: "+content + "\n");
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            message.setRetained(true);
            sampleClient.publish(topic, message);
            pw.write(TimeStamp.getTimeStamp() + " : Message published\n");
            sampleClient.disconnect();
            pw.write(TimeStamp.getTimeStamp() + " : Disconnected\n");
        } catch(MqttException me) {
            pw.write(TimeStamp.getTimeStamp() + " : reason "+me.getReasonCode() + "\n");
            pw.write(TimeStamp.getTimeStamp() + " : msg "+me.getMessage() + "\n");
            pw.write(TimeStamp.getTimeStamp() + " : loc "+me.getLocalizedMessage() + "\n");
            pw.write(TimeStamp.getTimeStamp() + " : cause "+me.getCause() + "\n");
            pw.write(TimeStamp.getTimeStamp() + " : excep "+me + "\n");
            me.printStackTrace();
        }
    }
}
