package iwasaki.sample;

import java.util.*;
import java.io.*;
import java.util.Map;
/**
 * Created by daiki on 2016/07/08.
 */
public class TestfirstDMATPlace {

    public static ArrayList Hospitalnumber = new ArrayList();
    public static ArrayList DMATnumber = new ArrayList();
    public static ArrayList DMATLevel = new ArrayList();
    public static ArrayList Distance = new ArrayList();
    public static ArrayList Hospitallocationlat = new ArrayList();
    public static ArrayList Hospitallocationlon = new ArrayList();
    public static ArrayList DMATlocationlat = new ArrayList();
    public static ArrayList DMATlocationlon = new ArrayList();


    public static void main(String args[]) throws Exception {

        File Medicalfacility = new File("test/iwasaki/Medicalfacility.csv");
        File Medicallocation = new File("test/iwasaki/Medicallocation.csv");
        File DMATlevel = new File("test/iwasaki/DMATlevel.csv");
        //File Medeicalcapacity
        //File patientetNumber


        /////DMAT派遣先の確定///////
        /////ファイル２参照,コード、緯度、経度、災害拠点病院、震度//////

        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicallocation), "Shift_JIS"));

        String str;

        while ((str = br1.readLine()) != null) {
            String[] pair = str.split(",");

            int num = Integer.parseInt(pair[4]);//震度

            if (num > 5 && pair[3].equals("1")) {
                Hospitalnumber.add(pair[0]);
            }

        }

        for (int i = 0; i < Hospitalnumber.size(); ++i) {
            System.out.println("hospitalnumber:" + Hospitalnumber.get(i));
        }

        ////////DAMT派遣可能病院の確定///////
        ////////ファイル１参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicalfacility), "Shift_JIS"));

        while ((str = br2.readLine()) != null) {
            String[] pair = str.split(",");

            //System.out.println(pair[6]);//DMATの有無

            for (int i = 0; i < Hospitalnumber.size(); ++i) {
                if (!(Hospitalnumber.get(i).equals(pair[0])) && pair[6].equals("1")) {
                    DMATnumber.add(pair[0]);

                }
            }

        }

        for (int i = 0; i < DMATnumber.size(); ++i) {
            System.out.println("DMAT Number:" + DMATnumber.get(i));
        }


        ////////派遣可能DAMTのレベルの確定///////
        ////////ファイル3参照、コード、緯度、経度、DMA////////
        BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream(DMATlevel), "SHIFT_JIS"));

        while ((str = br3.readLine()) != null) {
            String[] pair = str.split(",");

            for (int i = 0; i < DMATnumber.size(); ++i) {
                if (DMATnumber.get(i).equals(pair[0])) {
                    DMATLevel.add(pair[4]);
                }
            }

        }

        for (int i = 0; i < DMATLevel.size(); ++i) {
            System.out.println(DMATLevel.get(i));
        }


        /////////派遣元と派遣先の位置を確定////////
        ////////参照ファイル１、ファイル１参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////

        while ((str = br1.readLine()) != null) {
            String[] pair = str.split(",");

            for (int i = 0; i < Hospitalnumber.size(); ++i) {
                if (Hospitalnumber.get(i).equals(pair[0])) {
                    Hospitallocationlat.add(pair[1]);
                    Hospitallocationlon.add(pair[2]);
                    System.out.println(pair[0]);
                    System.out.println(pair[1]);
                    System.out.println(pair[2]);

                }
            }
        }
    }
}




















