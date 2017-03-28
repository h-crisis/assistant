package distance_calculation;

/**
 * Created by jiao on 2016/12/19.
 * 避難所の近くの病院３つを選びます
 */

public class MAIN {
    public static void main(String args[]) throws Exception {


        //root_findプログラムのテスト
        File outDir= new File("/Users/jiao/Desktop/split_map");//
        double lat1 = 24.36525965, lon1 = 124.2353145, lat2 = 24.361194, lon2 = 124.141777;//test
        new split_map(outDir, outDir, lat1, lon1, lat2,lon2);


        double distance=root_find.rootdistance(lat1, lon1, lat2, lon2, outDir);
        System.out.print(distance);

        LinkedList root=new LinkedList();
        root=root_find.rootfind(lat1, lon1, lat2, lon2, outDir);
        System.out.print(root);


    }
}
