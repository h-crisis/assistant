package gis;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by manabu on 2016/06/24.
 * Shapeファイルを作成するクラス
 */
public class CreateShape {
    /**
     * Shapeファイルを作成する
     * @param outFile Shapeファイル
     * @param encoding Shapeファイルの文字コード
     * @param type Featureタイプ
     * @param features 全Featureのリスト
     * @throws Exception
     */
    public static void createShapeFile(File outFile, String encoding, SimpleFeatureType type, List<SimpleFeature> features) throws Exception {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", outFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.createSchema(type);
        newDataStore.setCharset(Charset.forName(encoding));

        //featuresをShapeファイルに書き込む
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        //SimpleFeatureType shapeType = featureSource.getSchema();

        //System.out.println("\tSHAPE: " + shapeType);

        if(featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            SimpleFeatureCollection collection = new ListFeatureCollection(type, features);
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception problem) {
                problem.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
        }
        else  {
            System.out.println(typeName + " does not support read/write access");
        }
    }
}
