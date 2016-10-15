package jp.hcrisis.assistant.masterfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * Created by manabu on 2016/10/15.
 */
public class ShelterDbEdit {

    private static String url;
    private static String usr;
    private static String pwd;

    public ShelterDbEdit() {
        // JDBCドライバのロード
        this.url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        this.usr = "hcrisis";
        this.pwd = "niph614";
    }

    public ShelterDbEdit(String url, String usr, String pwd) {
        this.url = url;
        this.usr = usr;
        this.pwd = pwd;
    }


    public static void shelterAddressEdit() {
        try {
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();
            ResultSet rs;

            rs = st.executeQuery("SELECT * FROM template_shelter");
            LinkedList<String> list = new LinkedList<>();
            while(rs.next()) {
                String code = rs.getString("code");
                String pref = rs.getString("prefecture");
                String gun = rs.getString("gun");
                if(gun==null)
                    gun = "";
                String sikuchoson = rs.getString("sikuchoson");
                String str = pref + gun + sikuchoson;
                String address = rs.getString("address");
                String pair[] = address.split(sikuchoson);
                if(!address.contains(sikuchoson)) {
                    System.out.println(code + ","  + str  + "," + address);
                }
                //String sql = "update template_shelter set address='" + pair[1] + "' where code='" + code + "'";
                //list.addLast(sql);
            }

            int i=0;
            for(String s : list) {
                i++;
                //System.out.println(i + " : " + s);
                //st.execute(s);
            }

            // データベース切断
            rs.close();
            st.close();
            db.close();

            // エラー処理
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
