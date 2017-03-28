package org.geotools.Shelter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiao.xue on 2017/02/10.
 * 避難所のデータベースを作るprogram
 * 入力：避難所データ
 * 入力：小学校区shpファイル
 * 出力避難所が所属している小学校区と避難所の位置情報が入ってるDB
 */
public class DB_creat {
    public static HashMap<String,String> area_DB=new HashMap<>();//小学校区が持ってる避難所ルスト

    public static void main(String arg[]) throws IOException {
        DB_operation op=new DB_operation();
        //出力

        //避難所setを作る
        File shelter = new File("/Users/jiao.xue/Desktop/files_full/master/shelters.csv");//避難所データ
        BufferedReader shelter_info = new BufferedReader(new InputStreamReader(new FileInputStream(shelter), "UTF-8"));
        Map<Point,String> shelters = new HashMap<>();

        String line;
        line = shelter_info.readLine();//第一行を飛ばす
        while ((line = shelter_info.readLine()) != null) {
            String pair[] = line.split(",");
            double lon=Double.parseDouble(pair[0]);
            double lat=Double.parseDouble(pair[1]);
            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon,lat));
            shelters.put(point,pair[2]);
        }


        File file = new File("/Users/jiao.xue/Downloads/小学校区 2/小学校区コード有.shp");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);

        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                .getFeatureSource(typeName);

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
        FeatureIterator<SimpleFeature> features = collection.features();



        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            Iterator<Property> its = feature.getProperties().iterator();

            MultiPolygon poly= (MultiPolygon) feature.getDefaultGeometryProperty().getValue();


            Iterator iter = shelters.entrySet().iterator();

            int i = 0;
            Property pro = null;
            while (its.hasNext() && i < 4) {
                pro = its.next();
            }
            String id = String.valueOf(pro.getValue());

            while (iter.hasNext()) {
                Map.Entry  entry = (Map.Entry) iter.next();
                Point point = (Point) entry.getKey();
                String name = (String) entry.getValue();

                if(point.within(poly)) {
                    double lon=point.getX();
                    double lat=point.getY();
                    op.insert(new shelters(id,name,lon,lat));



                }
            }

        }
        // features.close();

    }
}
