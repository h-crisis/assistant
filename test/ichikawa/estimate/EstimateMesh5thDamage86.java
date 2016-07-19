package ichikawa.estimate;

import ichikawa.assist.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by manabu on 2016/07/18.
 */
public class EstimateMesh5thDamage86 {

    public static void main(String args[]) {
        estimatingMeth5thDamage(new File("files/ShapeFiles/mesh/Mesh5th.shp"), new File("files/CsvFiles/si_201608051200.csv"), "UTF-8", "UTF-8", 4, 4);
    }

    public static void estimatingMeth5thDamage(File meshFile, File siFile, String meshFileEncording, String siFileEncording, int siMeshLevel, int thread) {
        try {
            // 5次メッシュファイルを開く
            ShapefileDataStore meshFileStore = new ShapefileDataStore(meshFile.toURI().toURL());
            meshFileStore.setCharset(Charset.forName(meshFileEncording));
            SimpleFeatureSource meshFeatureSource = meshFileStore.getFeatureSource();
            SimpleFeatureCollection meshC = meshFeatureSource.getFeatures();
            FeatureIterator<SimpleFeature> meshI = meshC.features();

            // 震度分布Mapを作成する
            HashMap<String, Double> siMap = new HashMap<String, Double>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(siFile), siFileEncording));
            String line;
            line = br.readLine();
            while((line = br.readLine())!=null) {
                String pair[] = line.split(",");
                for(int i=0; i<pair.length; i++)
                    pair[i].trim();
                siMap.put(pair[0], Double.parseDouble(pair[1]));
            }

            // 揺れた5次メッシュのみを抽出する
            System.out.println("estimateingMesh5thDamage: 揺れた5次メッシュのみを抽出します。");
            ExecutorService executor = Executors.newFixedThreadPool(thread);
            List<SimpleFeature> features = new ArrayList<SimpleFeature>();
            int counter = 0;
            int percent = 0;
            while(meshI.hasNext()) {
                counter++;
                SimpleFeature mesh = meshI.next();
                Future<Boolean> future = executor.submit(new EstimateMesh5thDamageThread(mesh, siMap, siMeshLevel));
                if(future.get())
                    features.add(mesh);

                if(counter>=(meshC.size()/100)) {
                    counter = 0;
                    percent++;
                    System.out.println("\t" + percent + "%完了しました（全 " + meshC.size() + " Feature中 " + features.size() + " )。");
                }
            }
            executor.shutdown();
            System.out.println("estimateingMesh5thDamage: 揺れた5次メッシュのみを抽出しました。");

            String name = "Mesh5th 201608051200";
            String geom = "the_geom:MultiPolygon:srid=4612";
            String text = ",MESH5TH:String,MESH4TH:String,MESH3RD:String,MESH2ND:String,MESH1ST:String";
            SimpleFeatureType type = DataUtilities.createType(name, geom + text);
            CreateShape.createShapeFile(new File("files/out/mesh5_tmp_201608051200.shp"), "UTF-8", type, features);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (SchemaException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
