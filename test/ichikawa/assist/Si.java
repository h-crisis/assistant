package ichikawa.assist;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.index.UnsupportedFilterException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manabu on 2016/06/29.
 */
public class Si {
/**
    public static List createSiFeatures(File inFile, String inEncode, List<SimpleFeature> list) {
        try {
            SimpleFeatureType type = DataUtilities.createType("SI Mesh", "the_geom:Point:srid=4612,si:Double");
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);


            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile),inEncode))) {
                String line;
                while((line = br.readLine()) != null) {
                    String pair[] = line.split(",");

                    featureBuilder.add(geometryFactory.createPolygon(new Coordinate(lon, lat)));
                    featureBuilder.add(code);

                    double lat = Double.parseDouble(pair[6]);
                    double lon = Double.parseDouble(pair[7]);
                    featureBuilder.add(point);
                    featureBuilder.add(code);

                    Point point = geometryFactory.createPoint(new Coordinate(lon, lat));


                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    list.add(feature);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println(inFile.getPath() + "/" + inFile.getName() + "のファイルが見つかりません。");
            }
            catch (UnsupportedEncodingException e) {
                System.out.println(inEncode + "はサポートされていない文字コードです。");
            }
            catch (IOException e) {
                System.out.println("ファイルの入出力エラーです。");
            }
        }
        catch (SchemaException e) {
            System.out.println("Featureのスキーマ作成エラーです。");
        }
        return list;
    }
 */
}
