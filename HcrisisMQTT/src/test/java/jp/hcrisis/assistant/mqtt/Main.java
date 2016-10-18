package jp.hcrisis.assistant.mqtt;

/**
 * Created by manabu on 2016/08/26.
 */
public class Main {
    public static void main(String args[]) {
        if(args.length!=0) {
            System.out.println("BrokerのIPアドレス（第1引数）とサブスクライブするトピック（第2引数）を引数に指定してください");
            System.exit(1);
        }
        else {
            Subscriber client = new Subscriber("tcp://" + args[0] + ":1883");
            if(client.connectBroker()) {
                client.subscribe(args[1]);
        }
    }
}
