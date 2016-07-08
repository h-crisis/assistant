package iwasaki.sample;

import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.WhileNode;
import java.util.*;
import java.io.*;

/**
 * Created by daiki on 2016/07/08.
 */
public class TestfirstDMATPlace {

    public static ArrayList hospitalnumber = new ArrayList();
    public static ArrayList DMATnumber = new ArrayList();

    public static void main(String args[]) throws Exception {

        File Medicalfacility = new File("test/iwasaki/Medicalfacility.csv");
        File Medicallocation = new File("test/iwasaki/Medicallocation.csv");
        File DMATlevel = new File("test/iwasaki/DMATlevel.csv");
        //File Medeicalcapacity
        //File patientetNumber


        /////DMAT派遣先の確定///////
        /////ファイル２参照,コード、緯度、経度、災害拠点病院、震度//////

        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicallocation), "Shift_JIS"));

        String str = br1.readLine();

        while ((str = br1.readLine()) != null) {
            String[] pair = str.split(",");

            int num = Integer.parseInt(pair[4]);//震度

            if (num > 5 && pair[3].equals("1")) {
                hospitalnumber.add(pair[0]);
            }

        }

        for (int i = 0; i < hospitalnumber.size(); ++i) {
            System.out.println("hospitalnumber:" +hospitalnumber.get(i));
        }

        ////////派遣可能DAMTの確定///////
        ////////ファイル１参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicalfacility), "Shift_JIS"));

        while ((str = br2.readLine()) != null) {
            String[] pair = str.split(",");

            //System.out.println(pair[6]);//DMATの有無

            for (int i = 0; i<hospitalnumber.size(); ++i)
            {
                if(!(hospitalnumber.get(i).equals(pair[0])) && pair[6].equals("1"))
                {
                    DMATnumber.add(pair[0]);

                }
            }

        }

        for (int i = 0; i < DMATnumber.size(); ++i) {
            System.out.println("DMAT Number:" +DMATnumber.get(i));
        }


        ////////派遣可能DAMTの確定///////
        ////////ファイル3参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////
    }
}



















