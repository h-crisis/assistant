/**
 * Created by komori on 2017/04/18.
 */
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class csv2csv {

    public static void main(String args[]) {
        File inf = new File("/Users/komori/Downloads/master.csv");
        File ouf = new File("/Users/komori/Downloads/new_hospital_master.csv");
        converter(inf,ouf);
    }

    public static HashMap<String, String> prefectureDB = new HashMap<>();

    public static HashMap<String, String> getPefectureDB(){

        String tdfk[] = {"北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県", "茨城県", "栃木県", "群馬県", "埼玉県",
                "千葉県", "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県", "岐阜県", "静岡県", "愛知県", "三重県",
                "滋賀県", "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県", "鳥取県", "島根県", "岡山県", "広島県", "山口県",
                "徳島県", "香川県", "愛媛県", "高知県", "福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県"};

        int j = 1;
        for(String prefecture : tdfk) {
            if(j<10) {
                prefectureDB.put(prefecture, "0" + Integer.toString(j));
            }
            else {
                prefectureDB.put(prefecture, Integer.toString(j));
            }
            j++;
        }
        return prefectureDB;
    }


    public static void converter(File ipf, File opf){
        int lNum = 1;

        String text = "";
        try {
            InputStream is = new FileInputStream(ipf);
            OutputStream os = new FileOutputStream(opf);
            Reader r  = new InputStreamReader(is, "SJIS");
            Writer w = new OutputStreamWriter(os, "SJIS");
            BufferedReader br = new BufferedReader(r);
            BufferedWriter bw = new BufferedWriter(w);
            getPefectureDB();

            while((text=br.readLine())!=null) {
                String array[] = text.split("\",\"");
                array[1] = array[1].replace(",","");
                if(lNum==1) {
                    bw.write(text);
                }
                else {
                    text = "\"" + prefectureDB.get(array[6]) + "1" + array[1] + "\"," + text;
                    bw.write("\n" + text);
                    System.out.println(text);
                }
                lNum++;
            }
        } catch (Exception e) {
            System.out.println(text);
            throw new RuntimeException(e);
        } finally {

        }
    }
}
