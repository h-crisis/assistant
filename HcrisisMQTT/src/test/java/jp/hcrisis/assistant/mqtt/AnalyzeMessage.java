package jp.hcrisis.assistant.mqtt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by manabu on 2016/08/26.
 * MQTT Brokerよりサブスクライブして得られたメッセージを分析するクラス
 */
public class AnalyzeMessage {
    public static void analyze(String topic, String message) {
        if(topic.startsWith("/hcrisis/assistant/event/")) {
            analyzeEvent(topic, message);
        }
    }

    /**
     * Topicが/hcrisis/assistant/eventで始まる場合の処理
     * @param topic
     * @param message
     */
    public static void analyzeEvent(String topic, String message) {
        String topics[] = topic.split("/");
        if(topics.length>4) {
            String eventCode = topics[4].trim();

            if(topic.endsWith("/emergency")) {
                String sql = "update shelter set a1 = '" + topics[4] + "' where id = '" + topics[1] + "'";
                updateShelterEmergency(eventCode, sql);
            }
        }
    }


    public static void updateShelterEmergency(String eventCode, String message) {
        try {
            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://www.ichilab.org/" + eventCode;
            String usr = "gis";
            String pwd = "gis910";


            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();


            st.execute(message);

            // データベース切断
            st.close();
            db.close();

            // エラー処理
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
