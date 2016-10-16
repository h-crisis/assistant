package jp.hcrisis.assistant.masterfiles;

import com.google.code.geocoder.model.GeocoderResult;
import org.postgis.Geometry;
import org.postgis.PGgeometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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

    public static void shelterUpload(File file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"))) {
            Class.forName("org.postgresql.Driver");

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();

            String line = br.readLine(); // 1行目は見出し

            int id = 0;
            while((line=br.readLine())!=null) {
                String pair[] = line.split(",");

                id++; // idの更新
                String code = pair[0]; // 避難所コード
                String jcode = pair[1]; // 自治体コード
                String prefecture = pair[2]; // 市区町村名
                String gun = pair[3]; // 郡名
                String sikuchoson = pair[4]; // 市区町村名
                String name = pair[5]; // 避難所名
                String address = pair[6]; // 住所
                double lat = Double.parseDouble(pair[7]); // 緯度
                double lon = Double.parseDouble(pair[8]); // 経度
                String hinanjyo = pair[9]; // 避難所判定 1,0,-
                String ittoki = pair[10]; // 避難所判定 1,0,-
                String koiki = pair[11]; // 避難所判定 1,0,-
                String jishin = pair[12]; // 地震利用判定 1,0,-
                String fusuigai = pair[13]; // 風水害利用判定 1,0,-
                String tsunami = pair[14]; // 津波利用判定 1,0,-
                String capacity = ""; // 収容人数
                if(pair.length>15)
                    capacity = pair[15];
                String sgeom = "ST_GeomFromText('POINT(" + pair[8] + " " + pair[7] + ")',4612)";

                String sql = "INSERT INTO template_shelter (gid, code, jcode, prefecture, gun, sikuchoson, name, address, lat, lon, hinanjyo, ittoki, koiki, jishin, fusuigai, tsunami, capacity, geom) " +
                        "VALUES (" + Integer.toString(id) + ",'" + code + "','"  + jcode + "','" + prefecture + "','" + gun + "','"
                        + sikuchoson + "','" + name + "','" + address + "'," + lat + "," + lon + ",'" + hinanjyo + "','" + ittoki + "','" + koiki + "','" +
                        jishin + "','" + fusuigai + "','" + tsunami + "','" + capacity + "'," + sgeom + ")";
                System.out.println(sql);
                st.execute(sql);
            }

            // データベース切断
            st.close();
            db.close();

            // エラー処理
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
