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
                String pair[] = message.split(",");
                String sql = "update shelter set a01 = " + pair[4] +
                        ", info_type = 'emgcy'" +
                        ", c01_1 = '" + pair[5] + "'" +
                        ", c01_2 = '" + pair[6] + "'" +
                        ", c01_3 = '" + pair[7] + "'" +
                        ", c01_4 = '" + pair[8] + "'" +
                        ", c01_5 = '" + pair[9] + "'" +
                        ", c01_8 = '" + pair[10] + "'" +
                        ", c01_9 = '" + pair[11] + "'" +
                        ", c02_6 = '" + pair[11] + "'" +
                        ", c04_1 = '" + pair[11] + "'" +
                        " where code = '" + pair[1] + "'";
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

            System.out.println(message);
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
