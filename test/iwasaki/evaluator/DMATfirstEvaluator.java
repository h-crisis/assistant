package iwasaki.evaluator;

import iwasaki.agent.Hospital;
import iwasaki.ga.core.IIndividual;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TVector;

import java.util.*;

/**
 * Created by daiki on 2016/07/11.
 */
public class DMATfirstEvaluator {

    public static final double MIN = 0;//閾値
    public static final double MAX = 0;//閾値
    public static ArrayList DMATNumber = new ArrayList();
    public static ArrayList HospitalNumber = new ArrayList();
    public static ArrayList DMATLevel;
    public static Map<String,Map<String,Double>>  DMATtoHospital = new HashMap<String, Map<String, Double>>();
    private static double map(double x)//変数の定義域へ写像する。
    {
        return (MAX - MIN) * x + MIN;
    }


    ///////////それぞれの値をセットする///////////
    ///////////(hospitalnumber,DMATnumber,DMATlevelHashMap(キーhospitalnumber、値:HashMap(キーDMATnumber,値：dis)
    public static void setdata (ArrayList hospitalNumber,ArrayList dmatnumber,ArrayList dmatlevel,Map<String,Map<String,Double>> damattohospital)
    {
        DMATtoHospital = damattohospital;
        HospitalNumber = hospitalNumber;
        DMATNumber = dmatnumber;
        DMATLevel = dmatlevel;

    }


    /////////////個体を評価する/////////////
    private static void evaluateIndividual(TRealNumberIndividual ind)
    {
        TVector v = ind.getVector();
        double evaluationValue =0.0;
        for(int i =0; i<v.getDimension(); ++i)
        {
            double x = map(v.getData(i));
            if(x < MIN || x > MAX)
            {
                ind.setStatus(IIndividual.INVALID);
                return;
            }
            evaluationValue += x*x;
        }
    }


    ///////////集団内の全ての個体を評価する///////
    public static  void evaluatePoulation(List<TRealNumberIndividual> pop)
    {
        for(int i= 0; i<pop.size(); ++i)
        {
            evaluateIndividual(pop.get(i));
        }
    }

    ////////////情報を表示する///////////
    ////////////GA情報////////
    public static void printIndividual(TRealNumberIndividual ind) {
        System.out.println("Evaluation value: " + ind.getEvaluationValue());
        TVector v = ind.getVector();
        for (int i = 0; i < v.getDimension(); ++i) {
            System.out.print(map(v.getData(i)) + " ");
        }
        System.out.println();
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
        }
    }

    ///////////距離情報/////////
    public static void disprint()
    {
        for(int i = 0; i<HospitalNumber.size(); ++i)
        {
            for(int j= 0; j<DMATNumber.size(); ++j)
            {
                System.out.println(DMATtoHospital.get(HospitalNumber.get(i)).get(DMATNumber.get(j)));
            }
        }
    }
}
