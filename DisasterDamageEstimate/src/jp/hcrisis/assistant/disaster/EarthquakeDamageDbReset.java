package jp.hcrisis.assistant.disaster;

import org.postgis.PGgeometry;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;

/**
 * Created by manabu on 2016/12/01.
 */
public class EarthquakeDamageDbReset {
    private static String url;
    private static String usr;
    private static String pwd;

    public EarthquakeDamageDbReset(File dir, String code) {
        // JDBCドライバのロード
        this.url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        this.usr = "hcrisis";
        this.pwd = "niph614";

        File shelterFile = new File(dir.getPath() + "/" + code + "_shelter_01_evacuee.csv");
        resetEstimateInfoShelter2Table(shelterFile, code);
    }

    public static void resetEstimateInfoShelter2Table(File file, String eventCode) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {

            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();

            String s = "update event_shelter_" + eventCode + " set info_type=NULL, reporter=NULL, status=NULL, timestamp=NULL";
            s = s + ", a01=NULL, a01_1=NULL, a01_2=NULL, a03=NULL, a04=NULL, a05=NULL, a06=NULL, a07=NULL, a08=NULL, a09=NULL";
            s = s + ", b01=NULL, b02=NULL, b03=NULL, b03_1=NULL, b04=NULL, b04_1=NULL, b04_2=NULL, b04_3=NULL, b05=NULL, b05_1=NULL, b05_2=NULL, b05_3=NULL, b06=NULL, b06_1=NULL, b06_2=NULL, b06_3=NULL";
            s = s + ", c01_1=NULL, c01_2=NULL, c01_3=NULL, c01_4=NULL, c01_5=NULL, c01_8=NULL, c01_9=NULL"
                    + ", c02_1=NULL, c02_2=NULL, c02_3=NULL,c02_4=NULL,c02_5=NULL,c02_6=NULL,c02_6_1=NULL, c02_6_2=NULL, c02_6_3=NULL, c02_6_4=NULL, c02_6_5=NULL, c02_6_6=NULL, c02_7=NULL, c02_7_1=NULL, c02_8=NULL, c02_8_1=NULL"
                    + ", c03_1=NULL, c03_2=NULL, c03_3=NULL, c03_4=NULL, c03_5=NULL, c03_6=NULL, c03_7=NULL, c03_8=NULL, c03_9=NULL, c03_10=NULL, c03_11=NULL, c03_12=NULL"
                    + ", c04_1=NULL, c04_1_1=NULL, c04_2=NULL, c04_3=NULL, c04_4=NULL";
            s = s + ", d01=NULL, d01_1=NULL, d01_2=NULL, d02=NULL, d02_1=NULL, d03=NULL, d04=NULL, d05=NULL, d05_1=NULL, d05_2=NULL, d05_3=NULL, d06=NULL, d06_1=NULL, d06_2=NULL, d06_3=NULL, d06_4=NULL"
                    + ", d07=NULL, d08=NULL, d09=NULL, d10=NULL, d12=NULL, d11=NULL, d11_1=NULL, d11_2=NULL,d11_3=NULL, d13=NULL";
            s = s + ",e01=NULL, e01_1=NULL, e01_2=NULL, e01_3=NULL, e02=NULL";
            s = s + ",f01=NULL, f01_1=NULL, f01_2=NULL, f01_3=NULL, f02=NULL, f02_1=NULL, f02_2=NULL,f02_3=NULL"
                    + ",f03=NULL, f03_1=NULL, f03_2=NULL, f03_3=NULL, f04=NULL, f04_1=NULL, f04_2=NULL, f04_3=NULL"
                    + ",f05=NULL, f05_1=NULL, f05_2=NULL, f05_3=NULL, f06=NULL, f06_1=NULL, f06_2=NULL, f06_3=NULL"
                    + ",f07=NULL, f07_1=NULL, f07_2=NULL, f07_3=NULL, f08=NULL, f08_1=NULL, f08_2=NULL, f08_3=NULL"
                    + ",f09=NULL, f09_1=NULL, f09_2=NULL, f09_3=NULL, f10=NULL, f10_1=NULL, f10_2=NULL, f10_3=NULL"
                    + ",f11=NULL, f12=NULL,f13=NULL,f14=NULL,f15=NULL, f16=NULL";
            s = s + ", g01=NULL, g02=NULL, g03=NULL, g04=NULL";
            s = s + ", h01=NULL, h02=NULL, h03=NULL, h04=NULL";
            st.execute(s);
            System.out.println(0 + " : " + s);
            
            int i=0;
            // 避難所情報の読み込み
            String line = br.readLine(); // 1行目は見出し
            while((line = br.readLine()) != null) {
                String pair[] = line.split(",");
                String code = pair[0];
                double si = Double.parseDouble(pair[5]);
                double evacuee = Double.parseDouble(pair[6]);
                int ievacuee = (int) evacuee;
                String sql = "update event_shelter_" + eventCode + " set info_type='estimate', reporter=NULL, status=NULL, timestamp=NULL";
                sql = sql + ", a01=" + ievacuee + ", a01_1=NULL, a01_2=NULL, a03=NULL, a04=NULL, a05=NULL, a06=NULL, a07=NULL, a08=NULL, a09=NULL";
                sql = sql + ", b01=NULL, b02=NULL, b03=NULL, b03_1=NULL, b04=NULL, b04_1=NULL, b04_2=NULL, b04_3=NULL, b05=NULL, b05_1=NULL, b05_2=NULL, b05_3=NULL, b06=NULL, b06_1=NULL, b06_2=NULL, b06_3=NULL";
                sql = sql + ", c01_1=NULL, c01_2=NULL, c01_3=NULL, c01_4=NULL, c01_5=NULL, c01_8=NULL, c01_9=NULL"
                        + ", c02_1=NULL, c02_2=NULL, c02_3=NULL,c02_4=NULL,c02_5=NULL,c02_6=NULL,c02_6_1=NULL, c02_6_2=NULL, c02_6_3=NULL, c02_6_4=NULL, c02_6_5=NULL, c02_6_6=NULL, c02_7=NULL, c02_7_1=NULL, c02_8=NULL, c02_8_1=NULL"
                        + ", c03_1=NULL, c03_2=NULL, c03_3=NULL, c03_4=NULL, c03_5=NULL, c03_6=NULL, c03_7=NULL, c03_8=NULL, c03_9=NULL, c03_10=NULL, c03_11=NULL, c03_12=NULL"
                        + ", c04_1=NULL, c04_1_1=NULL, c04_2=NULL, c04_3=NULL, c04_4=NULL";
                sql = sql + ", d01=NULL, d01_1=NULL, d01_2=NULL, d02=NULL, d02_1=NULL, d03=NULL, d04=NULL, d05=NULL, d05_1=NULL, d05_2=NULL, d05_3=NULL, d06=NULL, d06_1=NULL, d06_2=NULL, d06_3=NULL, d06_4=NULL"
                        + ", d07=NULL, d08=NULL, d09=NULL, d10=NULL, d12=NULL, d11=NULL, d11_1=NULL, d11_2=NULL,d11_3=NULL, d13=NULL";
                sql = sql + ",e01=NULL, e01_1=NULL, e01_2=NULL, e01_3=NULL, e02=NULL";
                sql = sql + ",f01=NULL, f01_1=NULL, f01_2=NULL, f01_3=NULL, f02=NULL, f02_1=NULL, f02_2=NULL,f02_3=NULL"
                        + ",f03=NULL, f03_1=NULL, f03_2=NULL, f03_3=NULL, f04=NULL, f04_1=NULL, f04_2=NULL, f04_3=NULL"
                        + ",f05=NULL, f05_1=NULL, f05_2=NULL, f05_3=NULL, f06=NULL, f06_1=NULL, f06_2=NULL, f06_3=NULL"
                        + ",f07=NULL, f07_1=NULL, f07_2=NULL, f07_3=NULL, f08=NULL, f08_1=NULL, f08_2=NULL, f08_3=NULL"
                        + ",f09=NULL, f09_1=NULL, f09_2=NULL, f09_3=NULL, f10=NULL, f10_1=NULL, f10_2=NULL, f10_3=NULL"
                        + ",f11=NULL, f12=NULL,f13=NULL,f14=NULL,f15=NULL, f16=NULL";
                sql = sql + ", g01=NULL, g02=NULL, g03=NULL, g04=NULL";
                sql = sql + ", h01=NULL, h02=NULL, h03=NULL, h04=NULL";
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
