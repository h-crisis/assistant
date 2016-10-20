package jp.hcrisis.assistant.mqtt;

import java.io.*;
import java.sql.*;

/**
 * Created by manabu on 2016/10/13.
 */
public class CreateEvent {

    public static void main(String args[]) {
        createEventTables("test");
    }

    private static String url;
    private static String usr;
    private static String pwd;

    public CreateEvent() {
        // JDBCドライバのロード
        this.url = "jdbc:postgresql://10.0.1.14/hcrisis";
        this.usr = "hcrisis";
        this.pwd = "niph614";

        File file = new File("files/out/F008101_municipalities_base_02_damage.csv");
        addShelters2Table(file, "20161017_01");
    }


    public static void createEventTables(String eventCode) {
        try {
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();

            st.execute("CREATE TABLE event_shelter_" + eventCode + " AS SELECT * FROM event_shelter_template"); //避難所テンプレートデータベースの複製

            // データベース切断
            st.close();
            db.close();

            // エラー処理
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addShelters2Table(File file, String eventCode) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {

            // JDBCドライバのロード
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();
            ResultSet rs;

            // 被災自治体の読み込み
            String line = br.readLine(); // 1行目は見出し
            while((line = br.readLine())!=null) {
                String pair[] = line.split(","); // pair[0]が自治体コード、先頭に0を追加する必要あり
                String jcode = pair[0];
                if(jcode.length()==4) {
                    jcode = "0" + jcode;
                }

                String sql = "SELECT * FROM template_shelter where jcode=" + jcode;
                rs = st.executeQuery(sql);
                while(rs.next()) {
                    System.out.println(rs.getString("name"));
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
