package jp.hcrisis.assistant.disaster;

import org.postgis.PGgeometry;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;

/**
 * Created by manabu on 2016/10/14.
 */
public class EarthquakeDamageDbSet {


    private static String url;
    private static String usr;
    private static String pwd;

    public EarthquakeDamageDbSet(File dir, String code) {
        // JDBCドライバのロード
        this.url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        this.usr = "hcrisis";
        this.pwd = "niph614";

        File municipalitiesFile = new File(dir.getPath() + "/" + code + "_municipalities_base_02_damage.csv");
        File shelterFile = new File(dir.getPath() + "/" + code + "_shelter_01_evacuee.csv");
        createEventTables(code);
        addShelters2Table(municipalitiesFile, code);
        updateEstimateInfoShelter2Table(shelterFile, code);
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
            int i = 0;
            while((line = br.readLine())!=null) {
                String pair[] = line.split(","); // pair[0]が自治体コード、先頭に0を追加する必要あり
                String jcode = pair[0];
                if(jcode.length()==4) {
                    jcode = "0" + jcode;
                }

                String sql = "SELECT * FROM template_shelter where jcode='" + jcode + "'";
                rs = st.executeQuery(sql);
                LinkedList<String> list = new LinkedList<>();
                while(rs.next()) {
                    String code = rs.getString("code");
                    String name = rs.getString("name");
                    String pref = rs.getString("prefecture");
                    String gun = rs.getString("gun");
                    if(gun==null)
                        gun = "";
                    String sikuchoson = rs.getString("sikuchoson");
                    String address = rs.getString("address");
                    PGgeometry geom = (PGgeometry) rs.getObject("geom");
                    String s[] = geom.toString().split(";");
                    String sgeom = "ST_GeomFromText('" + s[1] + "'," + s[0].substring(5) + ")";
                    String str = "INSERT INTO event_shelter_" + eventCode + " (code, name, pref, gun, sikuchoson, address, geom) " +
                            "VALUES ('" + code + "','" + name + "','" + pref + "','" + gun + "','" + sikuchoson + "','" + address + "'," + sgeom + ")";
                    list.add(str);
                }

                System.out.println(list.size());

                for(String s : list) {
                    i++;
                    System.out.println(i + ":" + s);
                    st.execute(s);
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

    public static void updateEstimateInfoShelter2Table(File file, String eventCode) {
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
                String sql = "update event_shelter_" + eventCode + " set info_type='estimate',a01=" + evacuee + " where code='" + code + "'";
                i++;
                System.out.println(i + " : " + sql);
                st.execute(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
