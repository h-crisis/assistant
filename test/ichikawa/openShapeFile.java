package ichikawa;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;

/**
 * Created by manabu on 2016/05/24.
 */
public class openShapeFile {
    public static void main(String args[]) throws Exception {
        // Shapeファイルを指定する
        File file = new File("files/ShapeFiles/municipalities/municipalities.shp");

        // ShapeファイルをDataStoreに紐付け、DataStoreをFeatureSourceに関連づける
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource featureSource = store.getFeatureSource();

        // FeatureSourceから全Featureを要素にもつコレクションが取れる
        FeatureIterator i = featureSource.getFeatures().features();
        while(i.hasNext())
            System.out.println(((Feature) i.next()).getProperties());
    }

    public void openShapeFile2() throws Exception{
        // Shapeファイルを指定する
        File file = new File("files/ShapeFiles/municipalities/municipalities.shp");

        // ShapeファイルをDataStoreに紐付け、DataStoreをFeatureSourceに関連づける
        FileDataStore store = FileDataStoreFinder.getDataStore(file);


        // storeからSimpleFeatureSourceオブジェクトを作成する。
        SimpleFeatureSource featureSource = store.getFeatureSource();

        // FeatureSourceから全Featureを要素にもつコレクションが取り、イテレーターを定義する。
        SimpleFeatureCollection c = featureSource.getFeatures();
        FeatureIterator i = featureSource.getFeatures().features();
        while(i.hasNext()) {
            SimpleFeature feature = (SimpleFeature)i.next();
            System.out.println(((SimpleFeature) i.next());
        }
    }
}
