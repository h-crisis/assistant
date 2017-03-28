package Distribution_DMAT;

import java.util.*;

/**
 * Created by jiao.xue on 2017/03/16.
 * レベルの均一化のみを考えて、最適化問題の解
 *
 */
public class level_evaluation_only {
    public static HashMap<String, Double> kyoten_level = new HashMap<String, Double>();//病院のeコードはキー、dmatレベルは値
    public static HashMap<String, Double> Dmat_level = new HashMap<String, Double>();//病院のeコードはキー、dmatレベルは値
    public static ArrayList kyoten=new ArrayList();//災害拠点病院のリスト
    public static ArrayList dmat=new ArrayList();//搬送可能な病院のリスト

    private static List<BinaryNode> nodelist=null;//ノードの集合

    public static void main(String arg[]) {
        //テスト値を作る
        kyoten_level.put("A",1.0);
        kyoten_level.put("B",2.0);
        kyoten_level.put("C",3.0);
        kyoten_level.put("D",4.0);
        kyoten_level.put("E",5.0);
        kyoten_level.put("F",5.0);
        Dmat_level.put("a",1.0);
        Dmat_level.put("b",2.0);
        Dmat_level.put("c",3.0);
        Dmat_level.put("d",3.0);
        Dmat_level.put("e",4.0);
        Dmat_level.put("f",3.0);
        Dmat_level.put("g",4.0);
        Dmat_level.put("h",1.0);
        Dmat_level.put("i",4.0);
        Dmat_level.put("j",2.0);
        Dmat_level.put("k",4.0);
        BinaryNode root=BinaryNode.createTree_order(Dmat_level);
        BinaryNode.inOrderTravers(root);
        System.out.println("......");
        BinaryNode.deleteByMerging("k",root,Dmat_level);

        BinaryNode.inOrderTravers(root);


    }



}
