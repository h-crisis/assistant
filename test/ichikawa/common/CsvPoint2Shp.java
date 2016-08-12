package ichikawa.common;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import ichikawa.assist.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by manabu on 2016/07/11.
 */
public class CsvPoint2Shp {

    /**
     * CSVをShapeファイル(ポイント)に変換する。緯度と経度の項目を指定する。属性値はStringで保存される。
     * @param inFile CSV
     * @param outFile Shapeファイル
     * @param inFileEncording CSVの文字コード
     * @param outFileEncording Shapeファイルの文字コード
     */
    public static void csv2ShpPoint(String name, File inFile, File outFile, String inFileEncording, String outFileEncording, int iLat, int iLon) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), inFileEncording));
            String line = br.readLine(); // 1行目を読み込む

            String geom = "the_geom:Point:srid=4612";
            String text = "";
            String texts[] = line.split(",");
            if(texts.length>0) {
                for(int i=0; i<texts.length; i++)  {
                        text = text + "," + texts[i] + ":String";
                }
            }

            // Featureを生成する準備
            SimpleFeatureType type = DataUtilities.createType(name, geom + text);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

            List<SimpleFeature> features = new ArrayList<SimpleFeature>();

            // Featureを生成する
            while((line=br.readLine())!=null) {
                String pair[] = line.split(",");

                System.out.println(line);
                double lat = Double.parseDouble(pair[iLat]); // 緯度
                double lon = Double.parseDouble(pair[iLon]); // 経度
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
                featureBuilder.add(point);
                for(int i=0; i<pair.length; i++)  {
                    pair[i].trim();
                    featureBuilder.add(pair[i]);
                }
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }

            CreateShape.createShapeFile(outFile, outFileEncording, type, features);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SchemaException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
