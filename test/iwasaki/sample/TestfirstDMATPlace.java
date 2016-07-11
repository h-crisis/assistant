package iwasaki.sample;


import java.awt.peer.SystemTrayPeer;
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
    public static double distance,lat1,lon1,lat2,lon2;


    ///////距離を取得する///////
    ///////lat1:Hospitallocationlat,lon1:Hospitallocationlon,lat2:DMATlocationlat,lon2:DMATlocationlon
    public static double getStraightDistance(double lat1, double lon1, double lat2, double lon2)
    {
        double precision = 0.1;//精度
        int R = 6371; // km
        double lat = Math.toRadians(lat2 - lat1);
        double lng = Math.toRadians(lon2 - lon1);
        double A = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lng / 2) * Math.sin(lng / 2);
        double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A));
        double decimalNo = Math.pow(10, precision);
        double distance = R * C;
        distance = Math.round(decimalNo * distance / 1) / decimalNo;

        return distance;
    }

    public static void main(String args[]) throws Exception {

        File Medicalfacility = new File("test/iwasaki/Medicalfacility.csv");
        File Medicallocation = new File("test/iwasaki/Medicallocation.csv");
        File DMATlevel = new File("test/iwasaki/DMATlevel.csv");
        //File Medeicalcapacity
        //File patienteNumber


        /////DMAT派遣先の確定///////
        /////ファイル２参照,コード、緯度、経度、災害拠点病院、震度//////
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicallocation), "Shift_JIS"));

        String str;

        while ((str = br1.readLine()) != null) {
            String[] pair = str.split(",");

            if (!(pair[4].equals("地震"))) {
                int num = Integer.parseInt(pair[4]);

                if (num > 5 && pair[3].equals("1")) {
                    Hospitalnumber.add(pair[0]);//コード
                    Hospitallocationlat.add(pair[1]);//緯度
                    Hospitallocationlon.add(pair[2]);//経度
                }
            }

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
                    DMATlocationlat.add(pair[1]);
                    DMATlocationlon.add(pair[2]);

                }
            }

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

        /////////派遣元と派遣先の距離の確定////////
        ////////参照ファイル１、ファイル１参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////
        HashMap DMATtoHospaital = new HashMap();
        List keys = new ArrayList();

        for (int i = 0; i < Hospitalnumber.size(); ++i)
        {
            String str1 = (String) Hospitallocationlat.get(i);
            String str2 = (String) Hospitallocationlon.get(i);

            lat1 = Double.parseDouble(str1);
            lon1 = Double.parseDouble(str2);

            for (int j = 0; j < DMATnumber.size(); ++j)
            {
                String str3 = (String)DMATlocationlat.get(j);
                String str4 = (String)DMATlocationlon.get(j);

                lat2 = Double.parseDouble(str3);
                lon2 = Double.parseDouble(str4);

                double dis = getStraightDistance(lat1,lon1,lat2,lon2);


            }
        }
    }
}



















