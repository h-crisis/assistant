package jp.hcrisis.assistant.mqtt;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        // PostGIS接続用の設定
        String url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        String usr = "hcrisis";
        String pwd = "niph614";

        if(topics.length>4) {
            String eventCode = topics[4].trim();

            if(topics.length>7 && topics[5].endsWith("shelter")) { // topicが /hcrisis/assistant/event/eventCode/shelter の時の処理
                String shelterCode = topics[6];

                if (topic.endsWith("/emergency")) {
                    updateShelter(url, usr, pwd, eventCode, shelterCode, message, "emergency");
                } else if (topic.endsWith("/nacphn01")) {
                    updateShelter(url, usr, pwd, eventCode, shelterCode, message, "nacphn01");
                } else if (topic.endsWith("/nacphn02")) {
                    updateShelter(url, usr, pwd, eventCode, shelterCode, message, "nacphn02");
                }
            }
            else if(topic.endsWith("/control")) { // topicが　/hcrisis/assistant/event/eventCode/control の時の処理
                if (message.equals("exportShelterInfo")){ // messageがexportShelterInfoの時の処理
                    exportShelterInfo(url, usr, pwd, eventCode, message);
                }
            }
        }
    }

    public static void updateShelter(String url, String usr, String pwd, String eventCode, String shelterCode, String message, String type) {
        try {
            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();

            String sql = "update event_shelter_" + eventCode + " set ";
            if(message.equals("")) {
                sql = sql + "info_type='" + type + "', status='evaluated' where code='" + shelterCode + "'";
            }
            else {
                sql = sql + message + ", info_type='" + type + "', status='evaluated' where code='" + shelterCode + "'";
            }

            System.out.println(sql);
            st.execute(sql);

            // データベース切断
            st.close();
            db.close();

            // エラー処理
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void exportShelterInfo(String url, String usr, String pwd, String eventCode, String message) {
        try {
            File dataDir = new File("/home/svadmin/App/HcrisisMQTT/data");

            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();
            ResultSet rs;
            ResultSetMetaData rsmd;

            String sql = "SELECT * FROM event_shelter_" + eventCode + " where status='evaluated'";
            rs = st.executeQuery(sql);
            rsmd = rs.getMetaData();

            while(rs.next()) {
                int numCol = rsmd.getColumnCount();
                String s = "";
                for(int i = 0; i<numCol; i++) {
                    if(i == 0) {
                        s = rs.getString(i);
                    } else {
                        s = s + ", " + rs.getString(i);
                    }
                }

                System.out.println(s);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
