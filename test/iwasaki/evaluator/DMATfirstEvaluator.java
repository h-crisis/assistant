package iwasaki.evaluator;

import iwasaki.ga.core.IIndividual;
import iwasaki.ga.realcode.TRealNumberIndividual;
import iwasaki.ga.realcode.TVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daiki on 2016/07/11.
 */
public class DMATfirstEvaluator {

    public static final double MIN = 0;//閾値
    public static final double MAX = 0;//閾値
    public static ArrayList DMATNumber = new ArrayList();
    public static ArrayList HospitalNumber = new ArrayList();
    private static double map(double x)//変数の定義域へ写像する。
    {
        return (MAX - MIN) * x + MIN;
    }


    //////////////受け取った値を表示する//////
    public static void printreceiveHospitalNumber (ArrayList hospitalNumber)
    {
        for(int i = 0; i<hospitalNumber.size(); ++i)
        {
            HospitalNumber.set(i,hospitalNumber.get(i));
        }

        for (int i =0; i<HospitalNumber.size(); ++i)
        {
            System.out.println(HospitalNumber.get(i));
        }

    }


    /////////////個体を評価する/////////////
    private static void evaluateIndividual(TRealNumberIndividual ind)
    {


        /*TVector v = ind.getVector();
        double evaluationValue =0.0;
        for(int i =0; i<v.getDimension(); ++i)
        {
            double x = map(v.getData(i));
            if(x < MIN || x > MAX)
            {
                ind.setStatus(IIndividual.INVALID);
                return;
            }

            evaluationValue += x * x ;
        }*/
    }


    ///////////集団内の全ての個体を評価する///////
    public static  void evaluatePoulation(List<TRealNumberIndividual> pop)
    {
        for(int i= 0; i<pop.size(); ++i)
        {
            evaluateIndividual(pop.get(i));
        }
    }
}
