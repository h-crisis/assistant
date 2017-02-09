package org.geotools.Shelter;

/**
 * Created by jiao.xue on 2017/02/08.
 * 一つ一つの避難所が地図上の居場所を探す　
 * 実行回数：避難所の数
 * 弱点：Shpファイルを複数回開くと、ロックされます
 * だから、使わない
 * 少量なデータなら　使えます
 */

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class location_judge {

    public static HashMap<String, String> area_DB=new HashMap<>();//小学校区が持ってる避難所ルスト


     public static void main(String[] args) throws IOException, ParseException {
         File file = new File("/Users/jiao.xue/Downloads/小学校区 2/小学校区コード有.shp");

         File shelter = new File("/Users/jiao.xue/Desktop/files_full/master/shelters_test.csv");//避難所データ
         //File shelter = new File("/Users/jiao.xue/Dropbox/shelter/5mesh_lat_lon_mesh.csv");//避難所データ

         BufferedReader shelter_info = new BufferedReader(new InputStreamReader(new FileInputStream(shelter), "UTF-8"));
         String line;
         line = shelter_info.readLine();//第一行を飛ばす
         while ((line = shelter_info.readLine()) != null) {
             String pair[] = line.split(",");
             double lon=Double.parseDouble(pair[0]);
             double lat=Double.parseDouble(pair[1]);
             String shelter_name=pair[2];
             String location=check_point(file,lon,lat);
             if(location!="") {//検索できないものを除く
                 area_DB.put(location, shelter_name);
                 System.out.println(location+": "+shelter_name);
             }
         }
         shelter_info.close();

     }






    //場所(lon, lat)はどこの小学校区にある
    public static String check_point(File file, double lon, double lat) throws IOException, ParseException {

        // File file = new File("/Users/jiao.xue/Downloads/小学校区 2/小学校区コード有.shp");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);

        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                .getFeatureSource(typeName);

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
        FeatureIterator<SimpleFeature> features = collection.features();


        GeometryFactory gf = new GeometryFactory();
        //Point point = gf.createPoint(new Coordinate(140.315407,38.226099));//test
        Point point = gf.createPoint(new Coordinate(lon,lat));//test
        String id="";


        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            Iterator<Property> its = feature.getProperties().iterator();

            MultiPolygon poly= (MultiPolygon) feature.getDefaultGeometryProperty().getValue();
            if(point.within(poly)){
                //System.out.print(feature.getID());//
                //System.out.print(": ");
                //System.out.println(feature.getDefaultGeometryProperty().getValue());//wkt
                int i=0;
                Property pro=null;
                while (its.hasNext()&&i<4) {
                    pro = its.next();
                }
                id = String.valueOf(pro.getValue());
                //System.out.println(id);//wkt
                //id=feature.getID();
                break;
            }

        }
        features.close();
        dataStore.dispose();

        return id;
    }

}
