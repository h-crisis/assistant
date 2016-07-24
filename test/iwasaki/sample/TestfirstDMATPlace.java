package iwasaki.sample;

import com.google.common.base.StandardSystemProperty;
import iwasaki.evaluator.SphereEvaluator;
import iwasaki.evaluator.DMATfirstEvaluator;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TUndxMgg;

import java.util.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.SynchronousQueue;


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
    public static ArrayList Hospitalseismic = new ArrayList();
    public static ArrayList distance = new ArrayList();
    public static ArrayList everyDMATlevel = new ArrayList();
    public static Map<Map<String,String>,Double> test = new HashMap<Map<String, String>, Double>();
    public static Map<String,Map<String,Double>> DMATtoHospitalevel = new HashMap<String, Map<String, Double>>();
    public static Map<String,Map<String,Double>>  DMATtoHospitaldistance = new HashMap<String, Map<String, Double>>();
    public static int NO_OF_PARAMETERS = 0;//次元数、扱うパラメータの数
    public static final int POPULATION_SIZE = 500;//個体数
    public static final int NO_OF_CROSSOVERS = 10;//交叉回数



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
        File HeadQuater = new File("test/iwasaki/OutHeadQuater.csv");
        String str;
        //File Medeicalcapacity
        //File patienteNumber


        //////災害本部の確定//////
        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(HeadQuater), "UTF-8"));
        int counter=0;
        while ((str = br1.readLine()) != null)
        {
            String[] pair = str.split(",");
            if(counter>0)
            {
                Hospitalnumber.add(pair[1]);
                Hospitallocationlat.add(pair[6]);
                Hospitallocationlon.add(pair[7]);

            }

            counter++;
        }



        /////災害拠点病院のピックアップ///////
        /////ファイル２参照,コード、緯度、経度、災害拠点病院、震度//////
        /*BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(Medicallocation), "Shift_JIS"));

        String str;

        while ((str = br1.readLine()) != null) {
            String[] pair = str.split(",");

            if(pair[3].equals("1"))
            {
                Hospitalnumber.add(pair[0]);
                Hospitallocationlat.add(pair[1]);
                Hospitallocationlon.add(pair[2]);
                Hospitalseismic.add(pair[4]);
            }

        }



        /////////震度５以上の災害拠点病院のピックアップ////////
       for(int  i = 0; i<Hospitalnumber.size(); ++i)
       {
           int seismic = Integer.parseInt((String) Hospitalseismic.get(i));

           if(seismic < 5)
           {
               Hospitalnumber.remove(i);
               Hospitalseismic.remove(i);
               Hospitallocationlat.remove(i);
               Hospitallocationlon.remove(i);
           }
       }*/


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
                    double num = Double.parseDouble(pair[4]);
                    DMATLevel.add(num);

                }
            }
        }

        ///////GA計算用DMATlevelの配列を作成////////
        for(int i =0; i < DMATnumber.size(); ++i)
        {
            for (int j = 0; j <Hospitalnumber.size(); ++j)
            {
                everyDMATlevel.add(DMATLevel.get(i));
            }
        }




        /////////派遣元と派遣先の距離の確定////////
        ////////参照ファイル１、ファイル１参照、コード、緯度、経度、災害拠点病院名、救急、被爆、DMAT////////
        for (int i = 0; i < DMATnumber.size(); ++i)
        {

            double lat1 = Double.parseDouble((String) DMATlocationlat.get(i));
            double lon1 = Double.parseDouble((String) DMATlocationlon.get(i));

            DMATtoHospitalevel.put((String) DMATnumber.get(i),new HashMap<String,Double>());
            DMATtoHospitaldistance.put((String) DMATnumber.get(i),new HashMap<String, Double>());

            for (int j = 0; j < Hospitalnumber.size(); ++j)
            {
                double lat2 = Double.parseDouble((String)Hospitallocationlat.get(j));
                double lon2 = Double.parseDouble((String)Hospitallocationlon.get(j));

                double dis = getStraightDistance(lat1,lon1,lat2,lon2);

                DMATtoHospitalevel.get(DMATnumber.get(i)).put((String) Hospitalnumber.get(j), (Double)DMATLevel.get(i));
                DMATtoHospitaldistance.get(DMATnumber.get(i)).put((String) Hospitalnumber.get(j),dis);
                distance.add(dis);
            }
        }




        ///////次元数（考慮するパラメーターの数）の設定//////////
        NO_OF_PARAMETERS = Hospitalnumber.size() * DMATnumber.size();

        //////GAの評価プログラムDMATfirstEVAluatorに設定した値を渡す////////
        DMATfirstEvaluator.setdata(Hospitalnumber,DMATnumber,DMATLevel,distance,everyDMATlevel,DMATtoHospitaldistance,DMATtoHospitalevel,NO_OF_PARAMETERS);
        //DMATfirstEvaluator.Hospitalprint();//値が渡せたかの確認
        //DMATfirstEvaluator.DMATprint();//値が渡せたかの確認
        //DMATfirstEvaluator.disprint();
        //DMATfirstEvaluator.printeveryDMATlevl();


        //////GAによる計算/////
        TUndxMgg ga = new TUndxMgg(true,NO_OF_PARAMETERS,POPULATION_SIZE,NO_OF_CROSSOVERS);
        List<TRealNumberIndividual> initialPopulation = ga.getInitialPopulation();
        DMATfirstEvaluator.evaluatePopulation(initialPopulation);

        for(int i =0; i<500; ++i)
        {
            List<TRealNumberIndividual> family = ga.selectParentsAndMakeKids();
            DMATfirstEvaluator.evaluatePopulation(
                    family);
            List<TRealNumberIndividual> nextPop = ga.doSelectionForSurvival();
            System.out.println( ga.getIteration() + " " + ga.getBestEvaluationValue() + " " + ga.getAverageOfEvaluationValues());
        }

        System.out.println();
        System.out.println("Best individual");
        DMATfirstEvaluator.printIndividualevaluateIndividual(ga.getBestIndividual());
        DMATfirstEvaluator.writeIndividualevaluateIndividual(ga.getBestIndividual());
        DMATfirstEvaluator.writeIndividualevaluateIndividualtotallevle(ga.getBestIndividual());
        //DMATfirstEvaluator.printTotalDMATHospital(ga.getBestIndividual());


        ///////////情報の表示/////////////
        //////////病院情報///////////////


        for(int i=0; i<Hospitalnumber.size(); ++i)
        {
            //System.out.println("HospaitlNumber" + Hospitalnumber.get(i));//コード
            //System.out.println("Hospitallocationlat" + Hospitallocationlat.get(i));//経度
            //System.out.println("Hospitallocationlon" + Hospitallocationlon.get(i));//緯度
            //System.out.println("HospitalNumber:" + Hospitalnumber.get(i) + "   Hospitalseismic:" + Hospitalseismic.get(i));//震度

        }

        //////////DMAT情報/////////////
        for(int i = 0; i<DMATnumber.size(); ++i)
        {

            //System.out.println("DMATNumber:" + DMATnumber.get(i));//コード
            //System.out.println("DMATlocationlat:" + DMATlocationlat.get(i));//緯度
            //System.out.println("DMATlocationlon:" + DMATlocationlon.get(i));//経度
            //System.out.println("DMATNumber:"+ DMATnumber.get(i)+ "  DMATLevel:" + DMATLevel.get(i));//レベル

        }

        /////////各DMATから病院までの距離とレベルの情報（Hospital,DMAT)の並び(ただし、GAでのxの並びに対応しているのは（DMAT,Hospital)/////////////
            for (int i = 0; i < Hospitalnumber.size(); ++i) {
                for (int j = 0; j < DMATnumber.size(); ++j) {

                    //System.out.println("Hospital:Number" + Hospitalnumber.get(i) + "  DMATNumber:" + DMATnumber.get(j) + "  level:" + DMATtoHospitalevel.get(DMATnumber.get(j)).get(Hospitalnumber.get(i)) + "  Distance:" + DMATtoHospitaldistance.get(DMATnumber.get(j)).get(Hospitalnumber.get(i)));
                }
            }


        /////////書くDMATから病院までの距離とレベルの情報(DMAT,Hospitalの並び、GAでのxのならびはこっち）//////
        for (int i = 0; i < DMATnumber.size(); ++i) {
            for (int j = 0; j < Hospitalnumber.size(); ++j) {

                //System.out.println("DMATNumber:" + DMATnumber.get(i) + "  Hospitalnumber:" + Hospitalnumber.get(j) + "  level:" + DMATtoHospitalevel.get(DMATnumber.get(i)).get(Hospitalnumber.get(j)) +"  Distance:" + DMATtoHospitaldistance.get(DMATnumber.get(i)).get(Hospitalnumber.get(j)));
            }
        }


        ////////次元数の表示///////
        //System.out.println(NO_OF_PARAMETERS);

    }
}



















