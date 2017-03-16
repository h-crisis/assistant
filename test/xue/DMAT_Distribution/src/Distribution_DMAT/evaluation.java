package Distribution_DMAT;

import java.io.*;
import java.util.HashMap;

import static Distribution_DMAT.select_dmat.*;

/**
 * Created by jiao on 2017/03/08.
 */
public class evaluation {
    public static void main(String arg[]) throws IOException {
        File masterDir = new File("/Users/jiao/IdeaProjects/private/files_full/master");
        File outDir = new File("/Users/jiao/IdeaProjects/private/files_full/out");

        new Distribution_DMAT.select_dmat(masterDir,outDir);
//距離の評価する場合の回答(GAの初期個体)
        distance_result_print(kyoten_Dmat_distance,outDir);

    }

    public static void distance_result_print(HashMap<HashMap<String, String>, Double> kyoten_Dmat_distance ,File outDir) throws FileNotFoundException {

       try {
           int[] genes = distance_evaluation_only.distance_evaluation(kyoten_Dmat_distance);
           File outFile = new File(outDir.getPath() + "/shortest_distance_result.csv");
           PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), "Shift_JIS"));//結合した結果を新しいファイル'out'に保存する
           System.out.println("Distance evoluation");
           output.write("From,To,Time");
           for (int i = 0; i < genes.length; i++) {
               if (genes[i] != 0) {
                   System.out.println(dmat.get(i) + " should send to " + kyoten.get(genes[i] - 1));

                   HashMap<String, String> map= new HashMap<String, String>();
                   map.put((String)kyoten.get(genes[i] - 1),(String)dmat.get(i));
                   double distance= kyoten_Dmat_distance.get(map);
                   output.write("\n"+dmat.get(i) +","+ kyoten.get(genes[i] - 1)+","+distance);
               }
           }
       output.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

/*

    public static final double MIN = 0;//閾値
    public static final double MAX = 1;//閾値
    public static ArrayList DMATHospital = new ArrayList();
    private static double map(double x)//変数の定義域へ写像する。
    {
        return (MAX - MIN) * x + MIN;
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
*/
}
