package search_path;

import org.geotools.graph.structure.Graph;

import java.io.File;
import java.util.LinkedList;


/**
 * Created by jiao on 2017/01/05.
 *  二つの点の緯度経度から地図上のルートと距離を探すプログラム
 *  ３つmethod入ってます：
 *  origin_rootfind：地図上のルートを探す、地図上ルートに含まれるnodeを戻す
 *  rootfind：地図上のルートを探す、ルートをある距離単位で切ります。切れた点の緯度経度を返す
 *  rootdistance:二つの点の距離を計算する　単位はkm
 */
public class rout_find {

    private static File srcfilepath;
    private static File destfilepath;
    private static Graph graph;

    //public static LineStringGraphGenerator lineStringGen;
    double lat1, lon1,lat2,lon2; //lat1 lon1: start点の緯度経度　lat2,lon2：end点の緯度経度


    /**
     * @param outDir 分割されたshapeファイルのフォルダ
     */

    public static LinkedList origin_rootfind(Double lat1, Double lon1, Double lat2, Double lon2, File outDir) throws Exception {
        //find 同じ距離で記録する前の元root
        //戻り値：元rootのlinkedlist
        // linkedlistの中の点の距離
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        LinkedList root=new LinkedList();
        root= find_path_library.search_origin_path(lat1,lon1,lat2,lon2,destfilepath);
        return root;
    }

    public static LinkedList rootfind(Double lat1, Double lon1, Double lat2, Double lon2, File outDir,double path_unit) throws Exception {
        //find root
        //戻り値：rootのlinkedlist
        // linkedlistの中の点の距離
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        LinkedList root=new LinkedList();
        root= find_path_library.search_path(lat1,lon1,lat2,lon2,destfilepath,path_unit);
        return root;
    }


    public static double rootdistance(Double lat1, Double lon1, Double lat2, Double lon2, File outDir) throws Exception {
        //戻り値：rootの長さ
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        double distance= find_path_library.search_distance(lat1,lon1,lat2,lon2,destfilepath);
        distance=distance*100;//単位をkmに変更
        return distance;
    }


}

