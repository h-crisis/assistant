package jp.hcrisis.assistant.disaster;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by manabu on 2016/12/01.
 */
public class EarthquakeDamageDbExcelSet {
    private static String url;
    private static String usr;
    private static String pwd;

    public EarthquakeDamageDbExcelSet(File file, String code) {
        // JDBCドライバのロード
        this.url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        this.usr = "hcrisis";
        this.pwd = "niph614";

        setExcelEstimateInfoShelter2Table(file, code);
    }

    public static void setExcelEstimateInfoShelter2Table(File file, String eventCode) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {

            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();
            
            int i=0;
            // 避難所情報の読み込み
            String line = br.readLine(); // 1行目は見出し
            while((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                String code = pair[0];
                double si = Double.parseDouble(pair[5]);
                double evacuee = Double.parseDouble(pair[6]);
                int ievacuee = (int) evacuee;
                String sql = "update event_shelter_" + eventCode + " set info_type='excel', reporter='EXCEL', status='evaluated', timestamp='20XX/MM/DD HH:mm:SS'";
                sql = sql + ", a01=" + ievacuee + ", d01=" + pair[9] + ", d05_1=" + pair[10] + ", d04=" + pair[11] + ", d02=" + pair[12] + ", d11=" + pair[13] + ", d09=" + pair[15];
                if(pair[17].equals("◯")) { // 電気
                    sql = sql + ", c01_1='適度'";
                }
                else {
                    sql = sql + ", c01_1='皆無'";
                }
                if(pair[18].equals("◯")) { // 水
                    sql = sql + ", c01_3='適度'";
                }
                else {
                    sql = sql + ", c01_3='皆無'";
                }
                if(pair[19].equals("◯")) { // ガス
                    sql = sql + ", c01_2='適度'";
                }
                else {
                    sql = sql + ", c01_2='皆無'";
                }
                if(pair[20].equals("◯")) { // 通信
                    sql = sql + ", c01_9='適度'";
                }
                else {
                    sql = sql + ", c01_9='皆無'";
                }
                if(pair[21].equals("◯")) { // 換気
                    sql = sql + ", c03_5='適度'";
                }
                else {
                    sql = sql + ", c03_5='皆無'";
                }
                if(pair[22].equals("◯")) { // 暖房
                    sql = sql + ", c02_3='適度'";
                }
                else {
                    sql = sql + ", c02_3='皆無'";
                }
                if(pair[23].equals("◯")) { // トイレ
                    sql = sql + ", c02_6='適度'";
                }
                else {
                    sql = sql + ", c02_6='皆無'";
                }
                if(pair[24].equals("◯")) { // 汚物
                    sql = sql + ", c02_6_2='有'";
                }
                else {
                    sql = sql + ", c02_6_2='無'";
                }
                if(pair[25].equals("◯")) { // ごみ
                    sql = sql + ", c03_3='有'";
                }
                else {
                    sql = sql + ", c03_3='無'";
                }
                if(pair[26].equals("◯")) { // 食品
                    sql = sql + ", c04_1='有'";
                }
                else {
                    sql = sql + ", c04_1='無'";
                }
                if(pair[26].equals("◯")) { // 衛生
                    sql = sql + ", c03_1='普'";
                }
                else {
                    sql = sql + ", c03_1='不良'";
                }
                if(pair[27].equals("◯")) { // 飲料水
                    sql = sql + ", c01_4='適度'";
                }
                else {
                    sql = sql + ", c01_4='皆無'";
                }
                sql = sql + " where code='" + code + "'";
                
                i++;
                System.out.println(i + " : " + sql);
                st.execute(sql);
            }

            // データベース切断
            st.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
