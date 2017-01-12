package search_path;


import java.io.File;
import java.util.LinkedList;

/**
 * Created by jiao on 2016/12/19.
 * 二つの点の緯度経度から地図上のルートと距離を探すプログラム
 */

public class test_main {
    public static void main(String args[]) throws Exception {


        //rout_findプログラムのテスト

        File outDir= new File("/Users/jiao/Desktop/split_map");//
        double lat1 = 24.36525965, lon1 = 124.2353145, lat2 = 24.361194, lon2 = 124.141777;//test
        double path_unit=0.0018;//どういう距離でrootを表現するか　test:180m
         new split_map(outDir, outDir, lat1, lon1, lat2,lon2);



        ///距離計算
        double distance=rout_find.rootdistance(lat1, lon1, lat2, lon2, outDir);
        System.out.print(distance);
        System.out.print("\n");

        ///地図上から取ったstartからendまでのrootのlinkedlist
        LinkedList path=new LinkedList();
        path=rout_find.origin_rootfind(lat1, lon1, lat2, lon2, outDir);
        System.out.print(path);

        System.out.print("\n");

        ///等しい距離path_unitで記録したrootのlinkedlist
        LinkedList root=new LinkedList();
        root=rout_find.rootfind(lat1, lon1, lat2, lon2, outDir,path_unit);
        System.out.print(root);

    }
}
