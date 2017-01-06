package org.geotools.newshape;

import org.geotools.graph.structure.Graph;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by jiao on 2017/01/05.
 */
public class root_find {

    private static File srcfilepath;
    private static File destfilepath;
    private static Graph graph;

    //public static LineStringGraphGenerator lineStringGen;
    double lat1, lon1,lat2,lon2; //lat1 lon1: start点の緯度経度　lat2,lon2：end点の緯度経度


    /**
     * @param outDir 分割されたshapeファイルのフォルダ
     */
    public static LinkedList rootfind(Double lat1, Double lon1, Double lat2, Double lon2, File outDir) throws Exception {
        //find root
        //戻り値：rootのlinkedlist
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        LinkedList root=new LinkedList();
        root= find_nearest_top3.search_path(lat1,lon1,lat2,lon2,destfilepath);
        return root;
    }


    public static double rootdistance(Double lat1, Double lon1, Double lat2, Double lon2, File outDir) throws Exception {
        //戻り値：rootの長さ
        File destfilepath= new File(outDir.getPath()+"/split_map.shp");//start点からend点まで挟んでいるshape
        double distance= find_nearest_top3.search_distance(lat1,lon1,lat2,lon2,destfilepath);
        return distance;
    }


}