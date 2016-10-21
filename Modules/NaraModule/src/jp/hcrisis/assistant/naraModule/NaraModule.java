package jp.hcrisis.assistant.naraModule;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by manabu on 2016/10/20.
 */
public class NaraModule {
    public static void main(String args[]) {
        // PostGIS接続用の設定
        String url = "jdbc:postgresql://gis.h-crisis.jp/hcrisis";
        String usr = "hcrisis";
        String pwd = "niph614";

        // 文字列の設定
        String eventCode = "20161022_01";

        File dataDir = new File("/home/svadmin/App/NaraModule/data/" + eventCode);
        //File dataDir = new File("/Users/manabu/Desktop/data/" + eventCode);
        if(!dataDir.exists()) {
            dataDir.mkdir();
        }

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmm");
        File latestFile = new File(dataDir.getPath() + "/shelter_info_latest.csv");
        File dateFile = new File(dataDir.getPath() + "/shelter_info_" + sdf1.format(date) + ".csv");

        try(PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(latestFile, false), "Shift_JIS"));
            PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dateFile, false), "Shift_JIS"))) {

            // データベース接続
            System.out.println("Connecting to " + url);
            Connection db = DriverManager.getConnection(url, usr, pwd);
            Statement st = db.createStatement();
            ResultSet rs;

            String sql = "SELECT * FROM event_shelter_" + eventCode + " where status='evaluated'";
            rs = st.executeQuery(sql);

            String col = "避難所コード,記入日時,記入者,避難所名,都道府県,郡,市区町村,住所,入力種類";
            col = col + ",避難者数,避難者数(昼間),避難者数(夜間),電話,FAX,メールアドレス,設の広さ,スペース密度,一人当たりの専有面積,交通機関(避難所と外との交通手段),避難者への情報伝達手段(黒板/掲示板/マイク/チラシ配布等)"; // a
            col = col + ",管理統括・代表者の情報:氏名,連絡体制・指揮命令系統,自主組織,自主組織内容,外部組織,外部組織:チーム数,外部組織:人数,外部組織:職種,ボランティア,ボランティア:チーム数,ボランティア:人数,ボランティア:職種,医療提供状況,救護所,巡回診療,地域の医師との連携"; // b
            col = col + ",電気,ガス,水道,飲料水,固定電話,携帯電話,通信,対応,洗濯機,冷蔵庫,冷暖房,照明,調理設備,トイレ,箇所,下水,清掃,汲み取り,手洗い場,手指消毒,風呂,風呂清掃状況,喫煙所,分煙,対応,清掃状況,床の清掃,ゴミ収集場所,履き替え,空調管理,粉塵,生活騒音,寝具,寝具乾燥対策,ペット対策,ペットの収容対策,対応,食事,食事回数/日,炊き出し,残飯処理,対応";// c
            col = col + ",高齢者,うち75歳以上,うち要介護認定者数,妊婦,うち妊婦健診受診困難者数,産婦,乳児,幼児・児童,うち身体障害児,うち知的障害児,うち発達障害児,障害者,うち身体障害者,うち知的障害者,うち精神障害者,うち発達障害者,難病患者,在宅酸素療養者,人工透析者,アレルギー疾患児・者,配慮を要する人の全体像:外国人,要援助者数,うち全介助,うち一部介助,うち認知障害,対応・特記事項"; // d
            col = col + ",服薬者数:服薬者,うち高血圧治療薬,うち糖尿病治療薬,うち向精神薬,応・特記事項" ; // e
            col = col + ",有症状者数:外傷者数:総数,うち乳児・幼児,うち妊婦,うち高齢者,感染症(症状数:下痢):総数,うち乳児・幼児,うち妊婦,うち高齢者,感染症(症状数：嘔吐):総数,うち乳児・幼児,うち妊婦,うち高齢者,感染症(症状数：発熱):総数,うち乳児・幼児,うち妊婦,うち高齢者,感染症(症状数：咳):総数,うち乳児・幼児,うち妊婦,うち高齢者,その他(便秘):総数,うち乳児・幼児,うち妊婦,うち高齢者,その他(食欲不振):総数,うち乳児・幼児,うち妊婦,うち高齢者,その他(頭痛):総数,うち乳児・幼児,うち妊婦,うち高齢者,その他(不眠):総数,うち乳児・幼児,うち妊婦,うち高齢者,その他(不安):総数,うち乳児・幼児,うち妊婦,うち高齢者,専門的医療ニーズ,小児疾患,精神疾患,周産期,歯科,対応・特記事項";
            col = col + ",食中毒様症状(下痢、嘔吐などの動向),風邪様症状(咳・発熱などの動向),感染症症状・その他,防疫に関するその他情報"; // g
            col = col + ",全体の健康状態,活動内容,アセスメント,課題／申し送り"; // h

            pw1.write(col);
            pw2.write(col);
            while(rs.next()) {
                String s = "";
                s = rs.getString("code") + "," + rs.getString("timestamp") + "," + rs.getString("reporter") + "," + rs.getString("name") + "," + "," + rs.getString("pref") + "," + rs.getString("gun") + "," + rs.getString("sikuchoson") + "," + rs.getString("address") + "," + rs.getString("info_type");
                s = s + "," + rs.getString("a01") + "," + rs.getString("a01_1") + "," + rs.getString("a01_2") + "," + rs.getString("a03") + "," + rs.getString("a04") + "," + rs.getString("a05") + "," + rs.getString("a06") + "," + rs.getString("a07") + "," + rs.getString("a08") + "," + rs.getString("a09");
                s = s + "," + rs.getString("b01") + "," + rs.getString("b02") + "," + rs.getString("b03") + "," + rs.getString("b03_1") + "," + rs.getString("b04") + "," + rs.getString("b04_1") + "," + rs.getString("b04_2") + "," + rs.getString("b04_3") + "," + rs.getString("b05") + "," + rs.getString("b05_1") + "," + rs.getString("b05_2") + "," + rs.getString("b05_3") + "," + rs.getString("b06") + "," + rs.getString("b06_1") + "," + rs.getString("b06_2") + "," + rs.getString("b06_3");
                s = s + "," + rs.getString("c01_1") + "," + rs.getString("c01_2") + "," + rs.getString("c01_3") + "," + rs.getString("c01_4") + "," + rs.getString("c01_5") + "," + rs.getString("c01_8") + "," + rs.getString("c01_9") + "," + rs.getString("c01_7")
                         + "," + rs.getString("c02_1") + "," + rs.getString("c02_2") + "," + rs.getString("c02_3") + "," + rs.getString("c02_4") + "," + rs.getString("c02_5") + "," + rs.getString("c02_6") + "," + rs.getString("c02_6_1") + "," + rs.getString("c02_6_2") + "," + rs.getString("c02_6_3") + "," + rs.getString("c02_6_4") + "," + rs.getString("c02_6_5") + "," + rs.getString("c02_6_6") + "," + rs.getString("c02_7") + "," + rs.getString("c02_7_1") + "," + rs.getString("c02_8") + "," + rs.getString("c02_8_1") + "," + rs.getString("c02_9")
                        + "," + rs.getString("c03_1")+ "," + rs.getString("c03_2")+ "," + rs.getString("c03_3")+ "," + rs.getString("c03_4")+ "," + rs.getString("c03_5") + "," + rs.getString("c03_6") + "," + rs.getString("c03_7")+ "," + rs.getString("c03_8")+ "," + rs.getString("c03_9")+ "," + rs.getString("c03_10")+ "," + rs.getString("c03_11")+ "," + rs.getString("c03_12")
                        + "," + rs.getString("c04_1")+ "," + rs.getString("c04_1_1")+ "," + rs.getString("c04_2")+ "," + rs.getString("c04_3")+ "," + rs.getString("c04_4");
                s = s + "," + rs.getString("d01") + "," + rs.getString("d01_1") + "," + rs.getString("d01_2") + "," + rs.getString("d02") + "," + rs.getString("d02_1") + "," + rs.getString("d03")+ "," + rs.getString("d04")+ "," + rs.getString("d05")+ "," + rs.getString("d05_1")+ "," + rs.getString("d05_2")+ "," + rs.getString("d05_3")+ "," + rs.getString("d06")+ "," + rs.getString("d06_1")+ "," + rs.getString("d06_2")+ "," + rs.getString("d06_3")+ "," + rs.getString("d06_4")
                        + "," + rs.getString("d07")+ "," + rs.getString("d08")+ "," + rs.getString("d09")+ "," + rs.getString("d10")+ "," + rs.getString("d12")+ "," + rs.getString("d11")+ "," + rs.getString("d11_1")+ "," + rs.getString("d11_2") + "," + rs.getString("d11_3") + "," + rs.getString("d13"); // d
                s = s + "," + rs.getString("e01") + "," + rs.getString("e01_1") + "," + rs.getString("e01_2") + "," + rs.getString("e01_3") + "," + rs.getString("e02"); // e
                s = s + "," + rs.getString("f01")+ "," + rs.getString("f01_1")+ "," + rs.getString("f01_2")+ "," + rs.getString("f01_3")+ "," + rs.getString("f02")+ "," + rs.getString("f02_1")+ "," + rs.getString("f02_2") + "," + rs.getString("f02_3")
                        + "," + rs.getString("f03") + "," + rs.getString("f03_1") + "," + rs.getString("f03_2") + "," + rs.getString("f03_3") + "," + rs.getString("f04") + "," + rs.getString("f04_1") + "," + rs.getString("f04_2") + "," + rs.getString("f04_3")
                        + "," + rs.getString("f05") + "," + rs.getString("f05_1") + "," + rs.getString("f05_2") + "," + rs.getString("f05_3") + "," + rs.getString("f06") + "," + rs.getString("f06_1") + "," + rs.getString("f06_2") + "," + rs.getString("f06_3")
                        + "," + rs.getString("f07") + "," + rs.getString("f07_1") + "," + rs.getString("f07_2") + "," + rs.getString("f07_3") + "," + rs.getString("f08") + "," + rs.getString("f08_1") + "," + rs.getString("f08_2") + "," + rs.getString("f08_3")
                        + "," + rs.getString("f09") + "," + rs.getString("f09_1") + "," + rs.getString("f09_2") + "," + rs.getString("f09_3") + "," + rs.getString("f10") + "," + rs.getString("f10_1") + "," + rs.getString("f10_2") + "," + rs.getString("f10_3")
                        + "," + rs.getString("f11") + "," + rs.getString("f12") + "," + rs.getString("f13") + "," + rs.getString("f14") + "," + rs.getString("f15") + "," + rs.getString("f16"); // f
                s = s + "," + rs.getString("g01") + "," + rs.getString("g02") + "," + rs.getString("g03") + "," + rs.getString("g04"); // g
                s = s + "," + rs.getString("h01") + "," + rs.getString("h02") + "," + rs.getString("h03") + "," + rs.getString("h04"); // h
                String pair[] = s.split(",");
                s = "";
                for(int i=0; i<pair.length; i++) {
                    if(i==0) {
                        s = pair[i];
                    }
                    else if(pair[i].equals("null")) {
                        s = s + ",";
                    }
                    else if(pair[i].contains("\n")) {
                        String pair2[] = pair[i].split("\n");
                        String s2 = "";
                        for(int j = 0; j<pair2.length; j++) {
                            if(j==0) {
                                s2 = pair2[j];
                            } else {
                                s2 = s2 + " " + pair2[j];
                            }
                        }
                        s = s + "," + s2;
                    }
                    else {
                        s = s + "," + pair[i];
                    }
                }

                pw1.write("\n" + s);
                pw2.write("\n" + s);
            }

            // データベース切断
            rs.close();
            st.close();
            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
