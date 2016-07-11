package ichikawa.emis;

import com.vividsolutions.jts.geom.*;
import ichikawa.assist.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by manabu on 2016/07/11.
 */
public class csv2Shp {
    public static void main(String args[]) {
        medicalInstituteCsv2Shp(new File("files/CsvFiles/medical_institute_20160622.csv"), new File("files/ShapeFiles/medical_institute/medical_institute.shp"), "SHIFT_JIS", "UTF-8");
    }


    /**
     * EMISから受け取る医療機関マスターCSVをShapeファイルに変換する
     * 1項目目から6項目は文字列、7列目は緯度、8列目は経度、9列目以降は医療機関種別の構成かをチェック。
     * @param inFile EMISから受け取る医療機関マスターCSV
     * @param outFile 生成する医療機関Shapeファイル
     * @param inFileEncording EMISから受け取る医療機関マスターCSVの文字コード
     * @param outFileEncording 生成する医療機関Shapeファイルの文字コード
     */
    public static void medicalInstituteCsv2Shp(File inFile, File outFile, String inFileEncording, String outFileEncording) {
        // Medical InstituteのShapeファイルを読み込む

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), inFileEncording));
            String line = br.readLine(); // 1行目を読み込む

            String name = "Medical Institute";
            String geom = "the_geom:Point:srid=4612";
            String text = "";
            String texts[] = line.split(",");
            if(texts.length>0) {
                for(int i=0; i<texts.length; i++)  {
                    if(i<6)//1項目から6項目はString
                        text = text + "," + texts[i] + ":String";
                    else if(i<8)
                        text = text + "," + texts[i] + ":double";
                    else
                        text = text + "," + texts[i] + ":int";
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
                double lat = Double.parseDouble(pair[6]); // 緯度
                double lon = Double.parseDouble(pair[7]); // 経度
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
                featureBuilder.add(point);
                for(int i=0; i<pair.length; i++)  {
                    pair[i].trim();
                    if(i<6)//1項目から6項目はString
                        featureBuilder.add(pair[i]);
                    else if(i<8) //7,8項目はDouble
                        featureBuilder.add(Double.parseDouble(pair[i]));
                    else //9項目以降はInt
                        featureBuilder.add(Integer.parseInt(pair[i]));
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
