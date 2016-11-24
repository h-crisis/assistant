package DisasterHeadquatersSelect.evaluator;

import DisasterHeadquatersSelect.agent.Hospital;
import DisasterHeadquatersSelect.ga.core.IIndividual;
import DisasterHeadquatersSelect.ga.realcode.TRealNumberIndividual;
import DisasterHeadquatersSelect.ga.realcode.TVector;

import javax.measure.unit.SystemOfUnits;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;


/**
 * Created by daiki on 2016/07/11.
 */
public class DMATfirstEvaluator {

    public static final double MIN = 0;//閾値
    public static final double MAX = 1;//閾値
    public static ArrayList DMATNumber = new ArrayList();
    public static ArrayList HospitalNumber = new ArrayList();
    public static ArrayList DMATLevel = new ArrayList();
    public static ArrayList Distance = new ArrayList();
    public static ArrayList DMATHospital = new ArrayList();
    public static HashMap<String,Double> testDMATHospital = new HashMap<String, Double>();
    public static ArrayList EveryDMATlevel;
    public static ArrayList xDMATlevel  = new ArrayList();
    public static ArrayList HospitalDMAT = new ArrayList();
    public static ArrayList DMATHospitalcode = new ArrayList();
    public static ArrayList changeorderxlevle  = new ArrayList();
    public static ArrayList getTotalHospitallevel;
    public static int Dimension;
    public static HashMap<ArrayList<String>,Double>  DMATtoHospitallevel =new HashMap<ArrayList<String>, Double>();
    public static HashMap<String,HashMap<String,Double>>  DMATtoHospitaldistance =new HashMap<String, HashMap<String, Double>>();
    public static double TotalDMATLevelavg;
    private static double map(double x)//変数の定義域へ写像する。
    {
        return (MAX - MIN) * x + MIN;
    }



    ///////////それぞれの値をセットする///////////
    ///////////(hospitalnumber,DMATnumber,DMATlevelHashMap(キーhospitalnumber、値:HashMap(キーDMATnumber,値：dis)
    public static void setdata (ArrayList hospitalNumber,ArrayList dmatnumber,ArrayList dmatlevel,ArrayList distance,ArrayList everyDMATlevel,Map<String,Map<String,Double>> dmattohospitaldistance,Map<String,Map<String,Double>> damttohopitallevel, int dimension)
    {
        Distance = distance;
        HospitalNumber = hospitalNumber;
        DMATNumber = dmatnumber;
        DMATLevel = dmatlevel;
        EveryDMATlevel = everyDMATlevel;
        DMATtoHospitallevel = DMATtoHospitallevel;



        ////////GAで評価するための配列////////
        for(int i =0; i<DMATNumber.size(); ++i)
        {
            for(int j = 0; j<HospitalNumber.size(); ++j)
            {
                HospitalDMAT.add(HospitalNumber.get(j));
                DMATHospital.add(DMATNumber.get(i));
            }
        }

        for(int i = 0; i<DMATHospital.size(); ++i)
        {
            ArrayList test = new ArrayList();

            test.add(DMATHospital.get(i));
            test.add(HospitalDMAT.get(i));

            DMATHospitalcode.add(test);
        }
    }


    /////////DMATがそれぞれの派遣先病院に対していく確率の合計値を１とする//////////
    public static TRealNumberIndividual reformat(TRealNumberIndividual ind){

        TVector v = ind.getVector();
        TRealNumberIndividual ind_new = new TRealNumberIndividual(v.getDimension());

        TVector v_new = ind_new.getVector();

        double sum = 0;
        double x = 0;
        int counter = 0;
        String test = null;
        String temp1 = (String)DMATHospital.get(0);
        for (int i = 0; i < v.getDimension(); i++) {

            x = v.getData(i);

            if(x>1)
                x = x-1;
            else if(x<0)
                x = 0-x;

            if(i != 0){
                temp1 = (String)DMATHospital.get(i-1);
            }
            test = (String)DMATHospital.get(i);

            if(!(temp1.contentEquals(test))){

                for(int j = counter; j > 0;j--){
                    v_new.setData(i-j, v.getData(i-j)/sum);
                }
                sum = 0;
                counter = 0;
            }

            if( i == v.getDimension()-1) {
                sum += x;
                for (int j = counter; j >= 0; j--) {
                    v_new.setData(i-j, v.getData(i-j) / sum);
                }
                sum = 0;
                counter = 0;
            }

            sum += x;
            counter++;;
        }

        return ind_new;
    }


    ///////各病院に投入されたDMATレベルの合計値を求める/////
    public static ArrayList getTotaleveryHopital(HashMap<ArrayList<String>, Double> map) {
        double sum =0.0;
        double total = 0.0;
        double avg = 0.0;
        double dispersion =0.0;
        ArrayList TotaleveryHospital = new ArrayList();

        for(int i = 0; i<DMATHospital.size(); ++i)
        {
            //System.out.println("[DMATcode, Hospitalcode] " + DMATHospitalcode.get(i)+"  xlevel " + map.get(DMATHospitalcode.get(i)));
        }

        for(int i =0; i<DMATLevel.size(); ++i)
        {
            total += (Double) DMATLevel.get(i);
        }

        //System.out.println(total);
        avg = total/HospitalNumber.size();
        //System.out.println("avg " + avg);

        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            //System.out.println("test i " + i);
            ArrayList temp = new ArrayList();
            sum = 0;


            for(int j = 0; j<DMATHospital.size(); ++j)
            {
                //System.out.println("test j " + j);
                temp = (ArrayList) DMATHospitalcode.get(j);
                //System.out.println(temp);

                //System.out.println(HospitalNumber.get(i));
                if(temp.contains(HospitalNumber.get(i)))
                {
                    //System.out.println(map.get(DMATHospitalcode.get(j)));
                    sum += map.get(DMATHospitalcode.get(j));
                    //System.out.println("sum" + sum);
                }
            }

            //System.out.println("sum " + sum);
            TotaleveryHospital.add(sum);


            dispersion += Math.pow((sum - avg),2);
        }

        dispersion = dispersion/HospitalNumber.size();
        //System.out.println("dipersion " + dispersion);

        return TotaleveryHospital;
    }


    ////////各病院に投入された分散値を求める//////
    public static double getdispersion(HashMap<ArrayList<String>, Double> map)
    {
        double sum =0.0;
        double total = 0.0;
        double avg = 0.0;
        double dispersion =0.0;

        for(int i = 0; i<DMATHospital.size(); ++i)
        {
            //System.out.println("[DMATcode, Hospitalcode] " + DMATHospitalcode.get(i)+"  xlevel " + map.get(DMATHospitalcode.get(i)));
        }

        for(int i =0; i<DMATLevel.size(); ++i)
        {
            total += (Double) DMATLevel.get(i);
        }

        //System.out.println(total);
        avg = total/HospitalNumber.size();
        //System.out.println("avg " + avg);

        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            //System.out.println("test i " + i);
            ArrayList temp = new ArrayList();
            sum = 0;


            for(int j = 0; j<DMATHospital.size(); ++j)
            {
                //System.out.println("test j " + j);
                temp = (ArrayList) DMATHospitalcode.get(j);
                //System.out.println(temp);

                //System.out.println(HospitalNumber.get(i));
                if(temp.contains(HospitalNumber.get(i)))
                {
                    //System.out.println(map.get(DMATHospitalcode.get(j)));
                    sum += map.get(DMATHospitalcode.get(j));
                    //System.out.println("sum" + sum);
                }
            }

            //System.out.println("sum " + sum);

            dispersion += Math.pow((sum - avg),2);
        }

        dispersion = dispersion/HospitalNumber.size();
        //System.out.println("dipersion " + dispersion);

        return dispersion;
    }





    /////////個体を評価する////////
    ////////時間に対してのみ最適化を行う////////
    private static void evaluateIndividualtime(TRealNumberIndividual ind) {
        TVector v = reformat(ind).getVector();
        double evaluatonValue = 0.0;
        for (int i = 0; i < v.getDimension(); ++i) {
            double x = map(v.getData(i));

            if (x < MIN || x > MAX) {
                ind.setStatus(IIndividual.INVALID);
                return;
            }
            evaluatonValue += x * (Double)Distance.get(i);
        }
        ind.setEvaluationValue(evaluatonValue);
        ind.setStatus(IIndividual.VALID);
    }

    ////////レベルに対して評価を行う/////////
    private static void evaluateIndividuallevel(TRealNumberIndividual ind) {
        TVector v = reformat(ind).getVector();
        double evaluatonValue = 0.0;
        double sum = 0.0;
        double avg  = 0.0;
        HashMap<String,Double> temp = new HashMap<String, Double>();


        for (int i = 0; i < v.getDimension(); ++i) {
            double x = map(v.getData(i));
            if (x < MIN || x > MAX) {
                ind.setStatus(IIndividual.INVALID);
                return;
            }

            double a = (x * (Double)EveryDMATlevel.get(i));
            xDMATlevel.add(i,a);


            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);

            //System.out.println("DMATHospital  "+ DMATHospital.get(i)+  "  HospitalDMAT  " + HospitalDMAT.get(i) + "   xlevel " + a);
        }

        evaluatonValue = getdispersion(DMATtoHospitallevel);

        //System.out.println("break");


        ind.setEvaluationValue(evaluatonValue);
        ind.setStatus(IIndividual.VALID);
    }


    /////////時間とレベルに対して最適化を行う/////////
    private static void evaluateIndividual(TRealNumberIndividual ind) {
        TVector v = reformat(ind).getVector();
        double evaluatonValue = 0.0;
        double sum = 0.0;
        double avg  = 0.0;
        double w1 = 100;
        HashMap<String,Double> temp = new HashMap<String, Double>();


        for (int i = 0; i < v.getDimension(); ++i) {
            double x = map(v.getData(i));
            if (x < MIN || x > MAX) {
                ind.setStatus(IIndividual.INVALID);
                return;
            }

            evaluatonValue += x * (Double)Distance.get(i);

            double a = (x * (Double)EveryDMATlevel.get(i));
            xDMATlevel.add(i,a);


            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);

            //System.out.println("DMATHospital  "+ DMATHospital.get(i)+  "  HospitalDMAT  " + HospitalDMAT.get(i) + "   xlevel " + a);
        }

        evaluatonValue += w1 * getdispersion(DMATtoHospitallevel);

        //System.out.println("break");


        ind.setEvaluationValue(evaluatonValue);
        ind.setStatus(IIndividual.VALID);
    }


    ///////////集団中の全ての個体を評価する///////////
    ///////////集団内の全ての個体を時間のみで評価する/////
    public static void evaluatePopulationtime(List<TRealNumberIndividual> pop) {
        for ( int i = 0; i < pop.size(); ++i) {
            evaluateIndividualtime(pop.get(i));
        }
    }

    ///////////集団内の全ての個体をレベルの合計値のみで評価する//////
    public static void evaluatePopulationlevel(List<TRealNumberIndividual> pop) {
        for ( int i = 0; i < pop.size(); ++i) {
            evaluateIndividuallevel(pop.get(i));
        }
    }


    //////////集団内のすべての個体を時間、レベルの合計値によって評価する////////
    public static void evaluatePopulation(List<TRealNumberIndividual> pop) {
        for ( int i = 0; i < pop.size(); ++i) {
            evaluateIndividual(pop.get(i));
        }
    }




    ////////////情報を表示する///////////
    ////////////時間に対してのみ最適化をした場合の結果を表示する//////
    public static void printIndividualevaluateIndividualtime(TRealNumberIndividual ind) {

        System.out.println("result of time ");
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v = reformat(ind).getVector();
        for (int i = 0; i < v.getDimension(); ++i) {
            //System.out.print(map(v.getData(i)) + " ");
            System.out.println("DMATNumber:" + DMATHospital.get(i) + "  HospitalNumber:" + HospitalDMAT.get(i) + "  Distance:" + Distance.get(i) + "  x:" + map(v.getData(i)));
        }
        System.out.println();
    }

    //////DMATレベルのみ最適化した場合の結果を表示する////////
    public static void printIndividualevaluateIndividuallevel(TRealNumberIndividual ind)
    {
        System.out.println("result of level");
        ArrayList temp = new ArrayList();
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v= reformat(ind).getVector();
        double x = 0;

        for(int i = 0; i<v.getDimension(); ++i)
        {
            x = map(v.getData(i));
            double a = (x * (Double)EveryDMATlevel.get(i));
            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);
        }

        temp = getTotaleveryHopital(DMATtoHospitallevel);

        for(int i = 0; i<v.getDimension(); ++i)
        {
            System.out.println("DMATNumber:" + DMATHospital.get(i) + "  HospitalNumber:" + HospitalDMAT.get(i) + "  level:" + EveryDMATlevel.get(i) + "  x:" + map(v.getData(i)));
        }
        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            System.out.println("HospitalNumber  "+ HospitalNumber.get(i) + " Totallevel " + temp.get(i));
        }
    }

    //////時間とDMATレベルを最適化した場合の結果を表示する////////
    public static void printIndividualevaluateIndividual(TRealNumberIndividual ind)
    {
        System.out.println("result of time and level");
        ArrayList temp = new ArrayList();
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v= reformat(ind).getVector();
        double x = 0;

        for(int i = 0; i<v.getDimension(); ++i)
        {
            x = map(v.getData(i));
            double a = (x * (Double)EveryDMATlevel.get(i));
            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);
        }

        temp = getTotaleveryHopital(DMATtoHospitallevel);

        for(int i = 0; i<v.getDimension(); ++i)
        {
            System.out.println("DMATNumber:" + DMATHospital.get(i) + "  HospitalNumber:" + HospitalDMAT.get(i) + "  Distance:" + Distance.get(i) +  "  level:" + EveryDMATlevel.get(i) + "  x:" + map(v.getData(i)));
        }
        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            System.out.println("HospitalNumber  "+ HospitalNumber.get(i) + " Totallevel " + temp.get(i));
        }
    }

    public static void writeIndividualevaluateIndividual(TRealNumberIndividual ind) throws Exception
    {
        File file = new File("test/xue/DisasterHeadquatersSelect/resultofDMATfirstplace.csv");

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
        pw.write("DMATNumber,HospitalNumber,Distance,evel,p");
        System.out.println("result of time and level");
        ArrayList temp = new ArrayList();
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v= reformat(ind).getVector();
        double x = 0;

        for(int i = 0; i<v.getDimension(); ++i)
        {
            x = map(v.getData(i));
            double a = (x * (Double)EveryDMATlevel.get(i));
            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);
        }

        temp = getTotaleveryHopital(DMATtoHospitallevel);

        for(int i = 0; i<v.getDimension(); ++i)
        {
            pw.write("\n" + DMATHospital.get(i) + "," + HospitalDMAT.get(i) + "," + Distance.get(i) +  "," + EveryDMATlevel.get(i) + "," + map(v.getData(i)));
        }

        pw.close();
    }


    public static void writeIndividualevaluateIndividualtotallevle(TRealNumberIndividual ind) throws Exception
    {
        File file = new File("test/xue/DisasterHeadquatersSelect/resultofDMATfirstplacetotallevel.csv");

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
        pw.write("HospitalNumber,Totallevel");
        System.out.println("result of time and level");
        ArrayList temp = new ArrayList();
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v= reformat(ind).getVector();
        double x = 0;

        for(int i = 0; i<v.getDimension(); ++i)
        {
            x = map(v.getData(i));
            double a = (x * (Double)EveryDMATlevel.get(i));
            DMATtoHospitallevel.put((ArrayList)DMATHospitalcode.get(i),a);
        }

        temp = getTotaleveryHopital(DMATtoHospitallevel);

        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            pw.write("\n"+ HospitalNumber.get(i) + "," + temp.get(i));
        }


        pw.close();
    }
    ////////////病院情報/////////
    public static void Hospitalprint()
    {
        for(int i=0; i<HospitalNumber.size(); ++i)
        {
            System.out.println(HospitalNumber.get(i));
        }
    }
    ////////////DMAT情報/////////
    public static void DMATprint()
    {
        for(int i = 0; i<DMATNumber.size(); ++i)
        {
            System.out.println(DMATNumber.get(i));
            System.out.println(DMATLevel.get(i));
            System.out.println(DMATHospital.get(i));
        }
    }

    ///////////距離情報/////////
    public static void disprint()
    {
        for(int i = 0; i<Distance.size(); ++i)
        {
            System.out.println(Distance.get(i));
        }
    }

    ///////GAにおけるパラメーターの表示////////
    public static void codeprint()
    {
        for(int i =0; i<DMATHospital.size(); ++i)
        {
            System.out.println(DMATHospital.get(i));

        }
    }

    //////GAにおけるDMATlevelのパラメーター表示/////
    public static void printeveryDMATlevl()
    {
        for(int i = 0; i<EveryDMATlevel.size(); ++i) {
            System.out.println(EveryDMATlevel.get(i));
        }
    }


}
