/**
 * Created by komori on 2017/04/18.
 */
import com.sun.tools.javac.util.*;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.File;



public class csv2csv {

    public static void main(String args[]) {
        File inf = new File("/Users/komori/Downloads/master.csv");
        File duf = new File("/Users/komori/Downloads/dummy.csv");
        File ouf = new File("/Users/komori/Downloads/new_hospital_master.csv");
        converter(inf,ouf);
    }

    public static HashMap<String, String> prefectureDB = new HashMap();
    public static Map<Integer, String> sinryoukaDB = new TreeMap();
    public static Map<String, Integer> sinryoukaRDB = new TreeMap();
    public static Map<String, Integer> frequencyDB = new HashMap();
    public static String item = new String();

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

    public static String getItem(File inf, File ouf){
        int lNum = 1;
        int k=0,fNum,dummyNum;

        // ある診療科がlevelの値未満しか存在しない場合、その診療科はその他に丸め込まれる
        int level = 2;

        String text="",copy="",dummy="";
        try {
            InputStream is = new FileInputStream(inf);
            Reader r  = new InputStreamReader(is, "SJIS");
            BufferedReader br = new BufferedReader(r);

            while((text=br.readLine()) != null) {
                String tex[] = text.split("\",\"");
                tex[1] = tex[1].replace(",","");
                tex[15] = tex[15].replace("　"," ");
                String sinryou[] = tex[15].split(" ");
                for (int i = 0;i < sinryou.length; i++) {
                    if (!sinryoukaDB.containsValue(sinryou[i])) {
                        sinryoukaDB.put(k,sinryou[i]);
                        sinryoukaRDB.put(sinryou[i],k);
                        frequencyDB.put(sinryou[i],1);
                        k++;
                    } else {
                        fNum = frequencyDB.get(sinryou[i]) + 1;
                        frequencyDB.put(sinryou[i], fNum);
                    }
                }

                if(lNum==1) {
                    item = "id," + text;
                }
                lNum++;
                if (br.readLine()==null) {

                }

            }
            dummyNum = sinryoukaDB.size();
            for(int i=0;i < dummyNum;i++) {
                dummy = sinryoukaDB.get(i);
                if (frequencyDB.get(dummy) > level) {
                    item += "," + dummy;
                    //System.out.println(item);
                }else{
                    //item += "," + dummy;
                    sinryoukaDB.put(i,"dum");
                }
            }
            br.close();
            return item;
        } catch (Exception e) {
            System.out.println(copy);
            throw new RuntimeException(e);
        } finally {

        }
    }

    public static void converter(File inf, File ouf){
        int lNum = 1;
        String text="",copy="",dummy="";

        getItem(inf,ouf);


        try {
            InputStream is = new FileInputStream(inf);
            OutputStream os = new FileOutputStream(ouf);
            Reader r  = new InputStreamReader(is, "SJIS");
            Writer w = new OutputStreamWriter(os, "SJIS");
            BufferedReader br = new BufferedReader(r);
            BufferedWriter bw = new BufferedWriter(w);
            getPefectureDB();

            while((text=br.readLine()) != null) {
                String tex[] = text.split("\",\"");
                tex[1] = tex[1].replace(",","");
                tex[15] = tex[15].replace("　"," ");
                String sinryou[] = tex[15].split(" ");
                ArrayList<String> sinryouList = new ArrayList<String>(Arrays.asList(sinryou));
                ArrayList<String> sinryouDummy = new ArrayList<String>();
                /*
                for (int l=0;l<(sinryoukaDB.size() - sinryou.length);l++){
                    sinryouList.add("dum");
                }
                */
                for (int l=0;l<  sinryoukaDB.size();l++){
                    sinryouDummy.add("0");
                }


                if(lNum==1) {
                    bw.write(item);
                }
                else {
                    text = "\"" + prefectureDB.get(tex[6]) + "1" + tex[1] + "\"," + text;


                    for (int l=0; l < sinryouList.size();l++){
                        if (sinryoukaDB.containsValue(sinryouList.get(l))){
                            //sinryouDummy.set(sinryoukaRDB.get(sinryouList.get(l)),sinryouList.get(l));
                            sinryouDummy.set(sinryoukaRDB.get(sinryouList.get(l)),"1");
                        }
                    }



                    for (int l=0; l < sinryouDummy.size();l++) {
                        if (sinryoukaDB.get(l)=="dum") {}
                        else {
                            text += "," + sinryouDummy.get(l);
                        }
                    }


                    System.out.println(sinryouList);
                    System.out.println(sinryouDummy);
                    System.out.println(sinryoukaDB);
                    System.out.println(sinryoukaRDB);
                    bw.write("\n" + text);
                }
                lNum++;
            }
            br.close();
            bw.close();

/*
            is = new FileInputStream(opf);
            os = new FileOutputStream(opf);
            r  = new InputStreamReader(is, "SJIS");
            w = new OutputStreamWriter(os, "SJIS");
            br = new BufferedReader(r);
            bw = new BufferedWriter(w);

            bw.write("\n" + copy);
    //System.out.println(br.readLine());
    */

} catch (Exception e) {
        System.out.println(text);
        throw new RuntimeException(e);
        } finally {

        }
    }
}


