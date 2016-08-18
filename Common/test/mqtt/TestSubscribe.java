package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.sql.Timestamp;


/**
 * Created by manabu on 2016/06/24.
 */
public class TestSubscribe implements MqttCallback{
    public static void main(String[] args) {
        //QoSレベル
        int qos             = 2;
        //ブローカー
        String broker       = "tcp://stgexcloud.cloudapp.net:1883";
        //クライアントの識別子
        String clientId     = "HcrisisAssistant-" + Integer.toString((int)Math.random()*100);;
        // トピックはPublisherとSubscriberで同一である必要があります。
        String topic        = "sip4/karte/#";

        try {
            TestSubscribe sampleClient = new TestSubscribe();
            sampleClient.subscribe(broker, clientId, topic, qos);
        } catch(MqttException me) {
            // Display full details of any exception that occurs
            log("reason "+me.getReasonCode());
            log("msg "+me.getMessage());
            log("loc "+me.getLocalizedMessage());
            log("cause "+me.getCause());
            log("excep "+me);
            me.printStackTrace();
        }
    }

    /**
     * ブローカーに接続して指定トピックのメッセージを受信します。
     * ENTERキーを押すまで待機し続けます。
     *
     * @param broker ブローカー
     * @param clientId クライアントID
     * @param サブスクライブするトピック（ワイルドカードも利用可）
     * @param このサブスクリプションで利用するQOSの最大値（これを超えるものは下げて受信される）
     * @throws MqttException
     */
    public void subscribe(String broker, String clientId, String topic, int qos) throws MqttException {
        log("Initializing");
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        client.setCallback(this);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // QoSに沿った耐障害性の高い配信を行うためには、falseにセット
        connOpts.setCleanSession(false);

        log("Connecting to broker: " + broker);
        client.connect(connOpts);
        log("Connected");

        log("Subscribing to topic");
        client.subscribe(topic, qos);

        log("Press <Enter> to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            //do nothing
        }

        client.disconnect();
        log("Disconnected");
    }

    /**
     * メッセージを受信したときに呼ばれるCallback。SkyOnDemandではスクリプトにメッセージを渡す処理を行う。
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        log(time + "\tMessage: " + (new String(message.getPayload())).split(",").length + " " + new String(message.getPayload()));
    }

    /**
     * ブローカーとの接続が失われた時に呼ばれるCallback。本来は再接続のロジックを入れる。
     */
    @Override
    public void connectionLost(Throwable cause) {
        log("Connection lost");
        System.exit(1);
    }

    /**
     * メッセージの送信が完了したときに呼ばれるCallback。
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //will not be called in this demo
        log("Delivery complete");
    }

    private static void log(String message) {
        System.out.println(message);
    }
}
