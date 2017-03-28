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
//距離の評価する場合の回答
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


}
