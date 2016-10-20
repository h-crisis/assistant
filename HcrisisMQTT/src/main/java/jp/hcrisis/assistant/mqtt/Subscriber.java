package jp.hcrisis.assistant.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by manabu on 2016/08/26.
 * H-CRISISのサブスクライブを担うクラス
 */
public class Subscriber implements MqttCallback {

    private int qos = 0;
    private String broker;
    private String clientID = "HcrisisAssistantSubscriber1";
    private MqttClient client;

    /**
     * コンストラクタ。接続IDを生成する。
     */
    Subscriber(String broker) {
        this.broker = broker;
    }

    Subscriber(String broker, int qos) {
        this.broker = broker;
        this.qos = qos;
    }

    /**
     * 指定されたBrokerに接続する
     * @return
     */
    public boolean connectBroker() {
        if(broker==null) {
            log("Please input Broker's URL");

            return false;
        } else {
            boolean connection = false;
            try {
                this.client = new MqttClient(broker, clientID, new MemoryPersistence());
                this.client.setCallback(this);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // QoSに沿った耐障害性の高い配信を行うためには、falseにセット
                connOpts.setCleanSession(false);

                log("Connecting to broker: " + broker);
                client.connect(connOpts);
                log("Connected");
                connection = true;
            } catch (MqttException e) {
                log(broker + "に接続することができませんでした。");
                e.printStackTrace();
            }
            return connection;
        }
    }

    /**
     * Brokerへ再接続する
     * @return リストの1つにでも接続できればTrue、1つも接続できない場合はfalse
     */
    public boolean reconnectBroker() {
        if(client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                log(client + " を切断できませんでした。");
                e.printStackTrace();
            }
        }
        return connectBroker();
    }

    public boolean subscribe(String topic) {
        if(client.isConnected()) {
            try {
                client.subscribe(topic, qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * メッセージを受信したときに呼ばれるCallback。
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        AnalyzeMessage.analyze(topic, new String(message.getPayload()));
    }

    /**
     * ブローカーとの接続が失われた時に呼ばれるCallback。本来は再接続のロジックを入れる。
     */
    @Override
    public void connectionLost(Throwable cause) {
        log("Connection lost");
        reconnectBroker();
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
