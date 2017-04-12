/**
 * Created by komori on 2017/04/12.
 */
import sun.nio.cs.ext.SJIS;
import sun.nio.cs.ext.SJIS_0213;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;

public class test {
    public static void main(String args[])throws Exception {

        String tdfk[] = {"北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県", "茨城県", "栃木県", "群馬県", "埼玉県",
                "千葉県", "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県", "岐阜県", "静岡県", "愛知県", "三重県",
                "滋賀県", "京都府", "大阪府", "兵庫県", "奈良県", "和歌山県", "鳥取県", "島根県", "岡山県", "広島県", "山口県",
                "徳島県", "香川県", "愛媛県", "高知県", "福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県"};

        int lNum = 0;
        int i = 0;
        Path rPath = Paths.get("/Users/komori/Downloads/hospital_master.csv");
        Path wPath = Paths.get("/Users/komori/Downloads/new_hospital_master.csv");
        BufferedReader br = Files.newBufferedReader(rPath, StandardCharsets.UTF_8);
        BufferedWriter bw = Files.newBufferedWriter(wPath, StandardCharsets.UTF_8);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String array[] = line.split("\",\"");
                array[1] = array[1].replace(",","");
                if(lNum != 0) {
                    if (array[6].equals(tdfk[i])) {
                        line = "\"" + (i+1) + "1" + array[1] + "\"," + line;
                        bw.write(line);
                        bw.newLine();
                        System.out.println(line);
                    }
                    else {
                        i++;
                    }
                }
                lNum++;
            }
        }
    }

