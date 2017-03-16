package GA_new;

import Distribution_DMAT.distance_evaluation_only;

import java.util.*;

import static Distribution_DMAT.distance_evaluation_only.rank;
import static Distribution_DMAT.select_dmat.Dmat_level;
import static Distribution_DMAT.select_dmat.dmat;
import static Distribution_DMAT.select_dmat.kyoten;

/**
 * Created by jiao on 2017/03/10.
 */
public class Individual {
    //遺伝子の長さ
    static int defaultGeneLength = Dmat_level.size();
    //遺伝子の中の要素の範囲
    static int range = kyoten.size();//拠点病院の数
    //遺伝子行列
    private int[] genes = new int[defaultGeneLength];
    // 個体の適応値
    private int fitness = 0;

    // 伝子個体を作る(距離最適解)
    public void generate_dsitance_Individual(HashMap map) {
        genes=distance_evaluation_only.distance_evaluation(map);
    }


    // 最初遺伝子個体を作る（その他）
    public void generateIndividual(HashMap map) {

        int[] kyoten_times=new int[range];

        List<HashMap.Entry<HashMap, Double>> infoIds =new ArrayList<>();
        infoIds=rank(map);
        Collections.shuffle(infoIds); //shuffleした順位で初期値を探す
        for(Iterator<Map.Entry<HashMap, Double>> it = infoIds.iterator(); it.hasNext();){
            Map.Entry<HashMap, Double> now = it.next();
            HashMap<String, String> key=now.getKey();
            for (Map.Entry<String, String> entry : key.entrySet()) {

                int kyoten_num =kyoten.indexOf(entry.getKey());
                int dmat_num=dmat.indexOf(entry.getValue());
                if(genes[dmat_num]==0 && kyoten_times[kyoten_num]<3) {//0なら搬送しない
                    genes[dmat_num] = kyoten_num+1;
                    kyoten_times[kyoten_num]++;
                }
            }
        }

    }


    /* Getters and setters */
    // 遺伝子の長さが違ったら、lengthの値にします
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public int getGene(int index) {
        return genes[index];
    }
    public void setGene(int index, int value) {
        genes[index] = value;
        fitness = 0;
    }

    /* Public methods */
    //サイズ測る
    public int size() {
        return genes.length;
    }

    //適応値

    public int getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i);
        }
        return geneString;
    }
}
