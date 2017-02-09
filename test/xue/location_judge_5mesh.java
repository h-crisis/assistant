package org.geotools.Shelter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.iso.io.wkt.ParseException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jiao.xue on 2017/02/08.
 */
public class location_judge_5mesh {


    public static HashMap<String,String> area_DB=new HashMap<>();//小学校区が持ってる避難所ルスト

    public static void main(String[] args) throws IOException, ParseException {

        //出力
        File shelter_area = new File("/Users/jiao.xue/Dropbox/shelter/5mesh_area.csv");//避難所データ
        PrintWriter out= new PrintWriter(new OutputStreamWriter(new FileOutputStream(shelter_area , false), "SHIFT_JIS"));
        out.write("Id,shelter,lon,lat"+"\n");




        //避難所setを作る
        File shelter = new File("/Users/jiao.xue/Dropbox/shelter/5mesh_lat_lon_mesh.csv");//避難所データ
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
            //out.write(id);

            while (iter.hasNext()) {
                Map.Entry  entry = (Map.Entry) iter.next();
                Point point = (Point) entry.getKey();
                String name = (String) entry.getValue();

                if(point.within(poly)) {
                    double lon=point.getX();
                    double lat=point.getY();
                    out.write(id);
                    out.write(","+name);
                    out.write(","+lon);
                    out.write(","+lat);
                    out.write("\n");//

                    //System.out.println(id+": "+name);
                    //shelters.remove(point);

                    //System.out.println(id);//wkt
                    //id=feature.getID();
                }
            }
            //out.write("\n");

        }
        // features.close();
        out.close();

    }
}
