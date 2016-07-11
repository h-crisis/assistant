package ichikawa.assist;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manab on 2016/07/11.
 */
public class MeshMap {

    private static HashMap<String, List<SimpleFeature>> map1;
    private static HashMap<String, List<SimpleFeature>> map2;
    private static HashMap<String, List<SimpleFeature>> map3;
    private static HashMap<String, List<SimpleFeature>> map4;

    public MeshMap(File file, String encording) {
        map1 = createMeshRelationMap(new File(file.getPath() + "/Mesh2nd.shp"), encording, "MESH1ST");
        map2 = createMeshRelationMap(new File(file.getPath() + "/Mesh3rd.shp"), encording, "MESH2ND");
        map3 = createMeshRelationMap(new File(file.getPath() + "/Mesh4th.shp"), encording, "MESH3RD");
        map4 = createMeshRelationMap(new File(file.getPath() + "/Mesh5th.shp"), encording, "MESH4TH");
    }

    public static HashMap<String, List<SimpleFeature>> createMeshRelationMap(File file, String encording, String relationAttribute) {
        HashMap<String, List<SimpleFeature>> map = new HashMap<String, List<SimpleFeature>>();

        //メッシュファイルを開いて上位メッシュごとにリストを作成する
        ShapefileDataStore fileDataStore = null;
        try {
            fileDataStore = new ShapefileDataStore(file.toURI().toURL());
            fileDataStore.setCharset(Charset.forName(encording)); // 文字コードの設定
            SimpleFeatureSource featureSource = fileDataStore.getFeatureSource();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> featureI = featureCollection.features();
            while(featureI.hasNext()) {
                SimpleFeature feature = featureI.next();
                String meshCode = (String)feature.getAttribute(relationAttribute);
                if(map.containsKey(meshCode)) { // Mapにコードがまだ含まれている場合
                    List<SimpleFeature> list = map.get(meshCode);
                    list.add(feature);
                    map.put(meshCode, list);
                }
                else { // Mapにコードがまだ含まれていない場合
                    List<SimpleFeature> list = new ArrayList<SimpleFeature>();
                    list.add(feature);
                    map.put(meshCode, list);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static HashMap<String, List<SimpleFeature>> getMap(int i) {
        if(i==1)
            return map1;
        else if(i==2)
            return map2;
        else if(i==3)
            return map3;
        else if(i==4)
            return map4;
        else
            return null;
    }


}
