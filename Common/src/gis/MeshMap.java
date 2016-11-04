package gis;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manab on 2016/07/11.
 */
public class MeshMap {

    private static HashMap<String, List<SimpleFeature>> map0;
    private static HashMap<String, List<SimpleFeature>> map1;
    private static HashMap<String, List<SimpleFeature>> map2;
    private static HashMap<String, List<SimpleFeature>> map3;
    private static HashMap<String, List<SimpleFeature>> map4;

    /**
     * MeshMapのコンストラクタ
     * @param file メッシュShapeがあるディレクトリパス
     * @param encording メッシュShapeの文字コード
     */
    public MeshMap(File file, String encording) {
        map0 = createMeshRelationMap(new File(file.getPath() + "/Mesh1st.shp"), encording);
        map1 = createMeshRelationMap(new File(file.getPath() + "/Mesh2nd.shp"), encording, "MESH1ST");
        map2 = createMeshRelationMap(new File(file.getPath() + "/Mesh3rd.shp"), encording, "MESH2ND");
        map3 = createMeshRelationMap(new File(file.getPath() + "/Mesh4th.shp"), encording, "MESH3RD");
        map4 = createMeshRelationMap(new File(file.getPath() + "/Mesh5th.shp"), encording, "MESH4TH");
    }


    public static HashMap<String, List<SimpleFeature>> createMeshRelationMap(File file, String encording) {
        HashMap<String, List<SimpleFeature>> map = new HashMap<String, List<SimpleFeature>>();

        //メッシュファイルを開いて上位メッシュごとにリストを作成する
        ShapefileDataStore fileDataStore = null;
        try {
            fileDataStore = new ShapefileDataStore(file.toURI().toURL());
            fileDataStore.setCharset(Charset.forName(encording)); // 文字コードの設定
            SimpleFeatureSource featureSource = fileDataStore.getFeatureSource();
            SimpleFeatureCollection featureCollection = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> featureI = featureCollection.features();
            List<SimpleFeature> list = new ArrayList<SimpleFeature>();
            while(featureI.hasNext()) {
                SimpleFeature feature = featureI.next();
                String meshCode = (String) feature.getAttribute("MESH1ST");
                list.add(feature);
            }
            map.put("japan", list);
            featureI.close();
            fileDataStore.dispose();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileDataStore.dispose();
        }
        return map;
    }

    /**
     * 上位メッシュと下位メッシュの関連性DBを作成する
     * @param file メッシュShapeファイル
     * @param encording メッシュShapeファイルの文字コード
     * @param relationAttribute 関連性のキーとなる項目名
     * @return 上位メッシュコードをキー、下位メッシュフィーチャーのリストを値に持つマップを返す
     */
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
            featureI.close();
            fileDataStore.dispose();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileDataStore.dispose();
        }
        return map;
    }

    /**
     * 1つ下位のメッシュリストを返す
     * @param i 指定する階層（例 1を指定すると二次メッシュリストが返る）
     * @return 上位メッシュコードをキー、下位メッシュフィーチャーのリストを値に持つマップを返す
     */
    public static HashMap<String, List<SimpleFeature>> getMap(int i) {
        if(i==0)
            return map0;
        else if(i==1)
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

    public static LinkedList<String> getMeshIDs(int level, Point point) {
        LinkedList<String> list = new LinkedList<String>();
        if(level<=0 || level>5)
            return list;

        // 1次メッシュの絞り込み
        String mesh1st = "-";
        List<SimpleFeature> list1 = map0.get("japan");
        for(int i=0; i<list1.size(); i++) {
            SimpleFeature mesh = list1.get(i);
            if(point.within((MultiPolygon)mesh.getAttribute("the_geom")) || point.touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                mesh1st = mesh.getAttribute("MESH1ST").toString();
                list.addLast(mesh1st);
                break;
            }
        }
        if(level==1)
            return list;

        // 2次メッシュの絞り込み
        String mesh2nd = "-";
        List<SimpleFeature> list2 = map1.get(mesh1st);
        for(int i=0; i<list2.size(); i++) {
            SimpleFeature mesh = list2.get(i);
            if(point.within((MultiPolygon)mesh.getAttribute("the_geom")) || point.touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                mesh2nd = mesh.getAttribute("MESH2ND").toString();
                list.addLast(mesh2nd);
                break;
            }
        }
        if(level==2)
            return list;

        // 3次メッシュの絞り込み
        String mesh3rd = "-";
        List<SimpleFeature> list3 = map2.get(mesh2nd);
        for(int i=0; i<list3.size(); i++) {
            SimpleFeature mesh = list3.get(i);
            if(point.within((MultiPolygon)mesh.getAttribute("the_geom")) || point.touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                mesh3rd = mesh.getAttribute("MESH3RD").toString();
                list.addLast(mesh3rd);
                break;
            }
        }
        if(level==3)
            return list;

        // 4次メッシュの絞り込み
        String mesh4th = "-";
        List<SimpleFeature> list4 = map3.get(mesh3rd);
        for(int i=0; i<list4.size(); i++) {
            SimpleFeature mesh = list4.get(i);
            if(point.within((MultiPolygon)mesh.getAttribute("the_geom")) || point.touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                mesh4th = mesh.getAttribute("MESH4TH").toString();
                list.addLast(mesh4th);
                break;
            }
        }
        if(level==4)
            return list;

        // 5次メッシュの絞り込み
        String mesh5th = "-";
        List<SimpleFeature> list5 = map4.get(mesh4th);
        for(int i=0; i<list5.size(); i++) {
            SimpleFeature mesh = list5.get(i);
            if(point.within((MultiPolygon)mesh.getAttribute("the_geom")) || point.touches((MultiPolygon)mesh.getAttribute("the_geom"))) {
                mesh5th = mesh.getAttribute("MESH5TH").toString();
                list.addLast(mesh5th);
                break;
            }
        }
        if(level==5)
            return list;

        return list;
    }
}
