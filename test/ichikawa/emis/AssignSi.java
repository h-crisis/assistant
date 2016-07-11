package ichikawa.emis;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by manab on 2016/07/08.
 */
public class AssignSi {
    public static void assignSi2MedicalInstitute(File inFile1, File inFile2, File inFile3, File outFile,
                                                 String inFileEncording1, String inFileEncording2, String inFileEncording3, String outFileEncording) throws IOException {
        // 医療機関Shapeファイルを開く
        // ShapeファイルをShapefileDataStoreに紐付ける。
        ShapefileDataStore inFileStore1 = new ShapefileDataStore(inFile1.toURI().toURL());

        // 医療機関Shapeファイルの文字コードを指定する
        inFileStore1.setCharset(Charset.forName(inFileEncording1));

        // storeからSimpleFeatureSourceオブジェクトを作成する。
        SimpleFeatureSource featureSource1 = inFileStore1.getFeatureSource();

        // FeatureSourceから全Featureを要素にもつコレクションが取り、イテレーターを定義する。
        SimpleFeatureCollection c = featureSource1.getFeatures();
        FeatureIterator i = c.features();

        // 震度分布ファイルを開きMapにストックする
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), inFileEncording2));
        HashMap<String, Double> siMap = new HashMap<String, Double>(); // 震度分布をMapにストック
        String line;
        while((line = br.readLine()) != null) {
            String pair[] = line.split(",");
            for(int j=0; j<pair.length; j++)
                pair[j].trim();
            siMap.put(pair[0], Double.parseDouble(pair[1]));
        }

        //

        // 出力ファイルの準備をする
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false),outFileEncording));
        pw.write("CODE,LAT,LON,SI");

        // 医療機関の震度を計測して出力する
        while(i.hasNext()) {
            SimpleFeature medicalInstitute = (SimpleFeature) i.next();
            String code = (String)medicalInstitute.getAttribute("CODE");
            if(siMap.containsKey(code)) {
                pw.write("\n" + code + "," + medicalInstitute.getAttribute("LAT")
                        + "," + medicalInstitute.getAttribute("LON") + "," + siMap.get(code));
            }
        }

        br.close();
    }
}
