package jp.hcrisis.assistant.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manabu on 2016/08/26.
 * H-CRISISのサブスクライブを担うクラス
 */
public class Subscriber implements MqttCallback {

    private static int id = 1; // MQTT Client用の接続ID
    private int qos = 0;
    private List<String> brokers;
    private String clientID = "HcrisisAssistant-";
    private List<MqttClient> clients;

    /**
     * コンストラクタ。接続IDを生成する。
     */
    Subscriber() {
        brokers = new ArrayList<>();
        clients = new ArrayList<>();
        clientID = clientID + Integer.toString(id);
        id++;
    }

    /**
     * BrokerのリストにBrokerのURLを加える
     * @param url
     */
    public void addBroker(String url) {
        brokers.add(url);
    }

    /**
     * BrokerのリストからBrokerのURLを加える
     * @param url
     */
    public void removeBroker(String url) {
        brokers.remove(url);
    }

    /**
     * Brokerのリストに接続する
     * @return リストの1つにでも接続できればTrue、1つも接続できない場合はfalse
     */
    public boolean connectBroker() {
        if(brokers.isEmpty()) {
            log("There must be at least one brokerURL. Please add BrokerURL using addBroker method.");
            return false;
        } else {
            boolean connection = false;
            for(String url : brokers) {
                try {
                    MqttClient client = null;
                    client = new MqttClient(url, clientID, new MemoryPersistence());
                    MqttConnectOptions connOpts = new MqttConnectOptions();
                    // QoSに沿った耐障害性の高い配信を行うためには、falseにセット
                    connOpts.setCleanSession(false);

                    log("Connecting to broker: " + url);
                    client.connect(connOpts);
                    log("Connected");
                    clients.add(client);
                    connection = true;
                } catch (MqttException e) {
                    log(url + "に接続することができませんでした。");
                    e.printStackTrace();
                }
            }
            return connection;
        }
    }

    /**
     * Brokerへ再接続する
     * @return リストの1つにでも接続できればTrue、1つも接続できない場合はfalse
     */
    public boolean reconnectBroker() {
        if(!clients.isEmpty()) {
            for(MqttClient client : clients) {
                if(client.isConnected()) {
                    try {
                        client.disconnect();
                    } catch (MqttException e) {
                        log(client + " を切断できませんでした。");
                        e.printStackTrace();
                    }
                }
            }
        }
        return connectBroker();
    }

    public boolean subscribe(String topic, int qos) {
        for(MqttClient client : clients) {
            if(client.isConnected()) {
                try {
                    client.subscribe(topic, qos);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * メッセージを受信したときに呼ばれるCallback。
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        //String time = new Timestamp(System.currentTimeMillis()).toString();
        //log(time + "\tMessage: " + new String(message.getPayload()));
        AnalyzeMessage.test(new String(message.getPayload()));
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
