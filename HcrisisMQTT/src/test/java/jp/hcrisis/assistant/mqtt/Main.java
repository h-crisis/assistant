package jp.hcrisis.assistant.mqtt;

/**
 * Created by manabu on 2016/08/26.
 */
public class Main {
    public static void main(String args[]) {
        Subscriber client = new Subscriber();
        client.addBroker("tcp://broker.titech.ichilab.org:1883");
        client.connectBroker();
        System.out.println("Subscribe");
        client.subscribe("#", 2);
    }
}
