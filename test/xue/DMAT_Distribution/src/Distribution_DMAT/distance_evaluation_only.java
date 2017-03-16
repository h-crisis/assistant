package Distribution_DMAT;

import java.util.*;

import static Distribution_DMAT.select_dmat.*;

/**
 * Created by jiao on 2017/03/10.
 * 距離のみを評価する
 * 距離のみの場合、GAを使わず operation_researchの方法でできます
 * 距離のみで評価できた結果は後で、GAの初期種として使えます
 */
public class distance_evaluation_only {
    //遺伝子の長さ
    static int defaultGeneLength = Dmat_level.size();
    //遺伝子の中の要素の範囲
    static int range = kyoten.size();//拠点病院の数

//初期値を作る
    //このmethodから作った初期値は距離のみ評価する場合の回答

    public static int[] distance_evaluation(HashMap map) {
        //遺伝子行列
        int[] genes = new int[defaultGeneLength];
        int[] kyoten_times=new int[range];

        List<HashMap.Entry<HashMap, Double>> infoIds =new ArrayList<>();
        infoIds=rank(map);
        for(Iterator<Map.Entry<HashMap, Double>> it = infoIds.iterator(); it.hasNext();){
            Map.Entry<HashMap, Double> now = it.next();
            HashMap<String, String> key=now.getKey();

          //  System.out.println(now.getValue());

            for (Map.Entry<String, String> entry : key.entrySet()) {

                int kyoten_num =kyoten.indexOf(entry.getKey());
                int dmat_num=dmat.indexOf(entry.getValue());
                if(genes[dmat_num]==0 && kyoten_times[kyoten_num]<3) {//0なら搬送しない
                    genes[dmat_num] = kyoten_num+1;
                    kyoten_times[kyoten_num]++;
                    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    //System.out.println("genes  " + dmat_num + ", Value = " + genes[dmat_num]);
                }

            }

        }

        return genes;
    }

    //hashmapの距離近い順でsort
    public static List rank(HashMap map) {
        List<HashMap.Entry<HashMap, Double>> infoIds =new ArrayList<>();

        infoIds.addAll((Collection<? extends HashMap.Entry<HashMap, Double>>) map.entrySet());
        ValueComparator vc=new ValueComparator();
        Collections.sort(infoIds,vc);
        /*for(Iterator<Map.Entry<HashMap, Double>> it=infoIds.iterator();it.hasNext();)
        {
            System.out.println(it.next());
        }*/
        return infoIds;
    }

    private static class ValueComparator implements Comparator<Map.Entry<HashMap, Double>>
    {
        public int compare(Map.Entry<HashMap, Double> mp1, Map.Entry<HashMap, Double>mp2)
        {
            return (int)(mp1.getValue()*100000) - (int)(mp2.getValue()*100000);//距離の単位をmに変わる
        }
    }





}
