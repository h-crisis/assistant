package iwasaki.sample;

import iwasaki.evaluator.SphereEvaluator;
import iwasaki.evaluator.DMATfirstEvaluator;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TUndxMgg;

import java.util.*;
import java.io.*;
import java.util.List;


/**
 * Created by daiki on 2016/07/08.
 */
public class TestfirstDMATPlace {

    public static ArrayList Hospitalnumber = new ArrayList();
    public static ArrayList DMATnumber = new ArrayList();
    public static ArrayList DMATLevel = new ArrayList();
    public static ArrayList Hospitallocationlat = new ArrayList();
    public static ArrayList Hospitallocationlon = new ArrayList();
    public static ArrayList DMATlocationlat = new ArrayList();
    public static ArrayList DMATlocationlon = new ArrayList();
    public static Map<String,Map<String,Double>>  DMATtoHospital = new HashMap<String, Map<String, Double>>();
    public static int NO_OF_PARAMETERS = 20;//次元数、扱うパラメータの数
    public static final int POPULATION_SIZE = 50;//個体数
    public static final int NO_OF_CROSSOVERS = 100;//交叉回数


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

             if(!((String) (pair[4])).equals("地震")){
                int num = Integer.parseInt(pair[4]);


                if (num > 4 && pair[3].equals("1")) {
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

            for (int i = 0; i < Hospitalnumber.size(); ++i) {
                    if (!(Hospitalnumber.get(i).equals(pair[0])) && pair[6].equals("1"))
                    {
                        DMATnumber.add(pair[0]);
                        DMATlocationlat.add(pair[1]);
                        DMATlocationlon.add(pair[2]);

                        break;
                    }
                }

        }

        //////////////被災病院とDMAT派遣病院の重複を削除////////////
        for(int i = 0; i<Hospitalnumber.size(); ++i) {
            for (int j = 0; j < DMATnumber.size(); ++j) {
                if (Hospitalnumber.get(i).equals(DMATnumber.get(j))) {
                    DMATnumber.remove(j);
                    DMATlocationlat.remove(j);
                    DMATlocationlon.remove(j);
                }
            }
        }

        ////////派遣可能DAMTのレベルの確定///////
        ////////ファイル3参照、コード、緯度、経度、DMAT////////
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
        for (int i = 0; i < Hospitalnumber.size(); ++i)
        {
            double lat1 = Double.parseDouble((String) Hospitallocationlat.get(i));
            double lon1 = Double.parseDouble((String) Hospitallocationlon.get(i));

            DMATtoHospital.put((String) Hospitalnumber.get(i),new HashMap<String, Double>());
            for (int j = 0; j < DMATnumber.size(); ++j)
            {
                double lat2 = Double.parseDouble((String)DMATlocationlat.get(j));
                double lon2 = Double.parseDouble((String)DMATlocationlon.get(j));

                double dis = getStraightDistance(lat1,lon1,lat2,lon2);

                DMATtoHospital.get(Hospitalnumber.get(i)).put((String) DMATnumber.get(j),dis);

            }
        }

        ///////次元数（考慮するパラメーターの数）の設定//////////
        //NO_OF_PARAMETERS = Hospitalnumber.size() * DMATnumber.size();

        //////GAの評価プログラムDMATfirstEVAluatorに設定した値を渡す////////
        DMATfirstEvaluator.setdata(Hospitalnumber,DMATnumber,DMATLevel,DMATtoHospital);
        //DMATfirstEvaluator.Hospitalprint();//値が渡せたかの確認
        //DMATfirstEvaluator.DMATprint();//値が渡せたかの確認
        //DMATfirstEvaluator.disprint();


        //////GAによる計算/////
        TUndxMgg ga = new TUndxMgg(true,NO_OF_PARAMETERS,POPULATION_SIZE,NO_OF_CROSSOVERS);
        List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();
        DMATfirstEvaluator.evaluatePoulation(initialPopulation);

        for(int i =0; i<1000; ++i)
        {
            List<TRealNumberIndividual> family = ga.selectParentsAndMakeKids();
            DMATfirstEvaluator.evaluatePoulation( family);
            List<TRealNumberIndividual> nextPop = ga.doSelectionForSurvival();
            System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
        }

        System.out.println();
        System.out.println("Best individual");
        DMATfirstEvaluator.printIndividual(ga.getBestIndividual());


        ///////////情報の表示/////////////
        //////////病院情報///////////////
        for(int i=0; i<Hospitalnumber.size(); ++i)
        {
            //System.out.println("HospaitlNumber" + Hospitalnumber.get(i));
            //System.out.println("Hospitallocationlat" + Hospitallocationlat.get(i));
            //System.out.println("Hospitallocationlon" + Hospitallocationlon.get(i));

        }

        //////////DMAT情報/////////////
        for(int i = 0; i<DMATnumber.size(); ++i)
        {
            //System.out.println("DMATNumber:" + DMATnumber.get(i));
            //System.out.println("DMATlocationlat:" + DMATlocationlat.get(i));
            //System.out.println("DMATlocationlon:" + DMATlocationlon.get(i));
            //System.out.println("DMATNumber:"+ DMATnumber.get(i)+ "  DMATLevel:" + DMATLevel.get(i));

        }

        /////////各DMATから病院までの距離の情報/////////////
        for(int i= 0; i<Hospitalnumber.size(); ++i)
        {
            for(int j= 0; j<DMATnumber.size(); ++j)
            {
                //System.out.println("HospitalNuber:" + Hospitalnumber.get(i) + "  DMATNumber:" + DMATnumber.get(j) +"  Distance:" + DMATtoHospital.get(Hospitalnumber.get(i)).get(DMATnumber.get(j)));
            }
        }

    }
}



















