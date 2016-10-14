package jp.hcrisis.assistant.mqtt;

/**
 * Created by manabu on 2016/08/26.
 */
public class Main {
    public static void main(String args[]) {
        Subscriber client = new Subscriber("tcp://10.0.1.12:1883");
        if(client.connectBroker()) {
            client.subscribe("/hcrisis/assistant/#");
        }
    }
}
