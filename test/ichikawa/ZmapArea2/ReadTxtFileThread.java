package ichikawa.ZmapArea2;

import com.vividsolutions.jts.geom.*;
import ichikawa.assist.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manab on 2016/07/12.
 */
public class ReadTxtFileThread extends Thread {

    private File file;
    private File outDir;

    public ReadTxtFileThread(File file1, File file2) {
        this.file = file1;
        this.outDir = file2;
    }

    public void run() {
        try {
            System.out.println("[読込]" + file.getName() + "を読み込みます。: " + Thread.currentThread().getId());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Shift_JIS"));
            String line;
            LinkedList<Object> featuresList = new LinkedList<Object>();
            LinkedList<String> textName = new LinkedList<String>();
            int figTypeI = 0;
            int layerI = 0;

            while ((line = br.readLine()) != null) {
                // 空行の処理
                if (line.equals(""))
                    ; //System.out.println("空行です");
                else if (line.startsWith("[")) { // 上記以外の処理　[から始まるパラグラフ
                    LinkedList<Object> featureList = new LinkedList<Object>();
                    String id = "";
                    String figType = "";
                    String layer = "";
                    String dispLevel = "";
                    String no = "";
                    LinkedList<LinkedList<String>> pointList = new LinkedList<LinkedList<String>>();
                    LinkedList<String> textList = new LinkedList<String>();

                    while (line != null) {
                        if (line.startsWith("[")) {
                            id = line.substring(1, line.length() - 1);
                        } else if (line.startsWith("FIGTYPE")) {
                            String pair[] = line.split("=");
                            figType = pair[1];
                            figTypeI = Integer.parseInt(figType);
                        } else if (line.startsWith("DISPLEVEL")) {
                            String pair[] = line.split("=");
                            dispLevel = pair[1];
                        } else if (line.startsWith("LAYER")) {
                            String pair[] = line.split("=");
                            layer = pair[1];
                            layerI = Integer.parseInt(layer);
                        } else if (line.startsWith("NO")) {
                            String pair[] = line.split("=");
                            no = pair[1];
                        } else if (line.startsWith("PNTTYPE"))
                            ;
                        else if (line.startsWith("PNTNUM"))
                            ;
                        else if (line.startsWith("PNT")) {
                            String pair[] = line.split("=");
                            String str[] = pair[1].split(",");
                            LinkedList<String> point = new LinkedList<String>();
                            point.addLast(str[0]);
                            point.addLast(str[1]);
                            pointList.addLast(point);
                        } else if (line.startsWith("TEXT")) {
                            String pair[] = line.split("=");
                            if (!textName.contains(pair[0]))
                                textName.addLast(pair[0]);
                            textList.addLast(pair[1]);
                        } else if (line.equals(""))
                            break;
                        line = br.readLine();
                    }
                    featureList.addLast(id);
                    featureList.addLast(figType);
                    featureList.addLast(dispLevel);
                    featureList.addLast(layer);
                    featureList.addLast(no);
                    featureList.addLast(pointList);
                    featureList.addLast(textList);
                    featuresList.addLast(featureList);
                }
            }
            br.close();
            createShape(file, outDir, figTypeI, layerI, featuresList, textName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void createShape(File file, File outDir, int figType, int layer, LinkedList<Object> featuresList, LinkedList<String> texts) throws Exception {
        System.out.println("[変換]" + file.getName() + "をShapeファイルに変換します。: " + Thread.currentThread().getId());
        String inFileName[] = file.getName().split("\\.");
        String outFilePath = outDir.getPath() + "/" + inFileName[0] + ".shp";
        File outFile = new File(outFilePath);
        String name = "Layer " + Integer.toString(layer);
        String geom = "the_geom:";
        if(figType==1)
            geom = geom + "LineString:srid=4612,";
        else if(figType==2)
            geom = geom + "Polygon:srid=4612,";
        else
            geom = geom + "Point:srid=4612,";

        String text = "";
        if(texts.size()>0) {
            for(int i=0; i<texts.size(); i++)
                text = text + "," + texts.get(i) + ":String";
        }

        // FeatureTypeを生成する。
        SimpleFeatureType type = DataUtilities.createType(name, geom +
                "id:String,figtype:int,displevel:int,layer:int,no:String" + text
        );

        // Featureを生成する準備
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        List<SimpleFeature> features = new ArrayList<SimpleFeature>();

        if(figType==1) {
            for(int i=0; i<featuresList.size(); i++) {
                LinkedList<Object> featureList = (LinkedList<Object>) featuresList.get(i);
                LinkedList<LinkedList<String>> pointList = (LinkedList<LinkedList<String>>) featureList.get(5);
                Coordinate cord[] = new Coordinate[pointList.size()];
                for(int j=0; j<pointList.size();j++) {
                    double lat = Double.parseDouble((pointList.get(j)).get(1))/3600000; // 緯度
                    double lon = Double.parseDouble((pointList.get(j)).get(0))/3600000; // 経度
                    cord[j] = new Coordinate(lon, lat);
                }
                LineString line = geometryFactory.createLineString(cord);
                featureBuilder.add(line);
                featureBuilder.add(featureList.get(0));
                featureBuilder.add(Integer.parseInt((String)featureList.get(1)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(2)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(3)));
                featureBuilder.add(featureList.get(4));
                if(texts.size()>0) {
                    LinkedList<String> textList = (LinkedList<String>) featureList.get(6);
                    for(int j=0; j<textList.size(); j++)
                        featureBuilder.add(textList.get(j));
                }
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
            CreateShape.createShapeFile(outFile, "UTF-8", type, features);
        }
        else if(figType==2) {
            for(int i=0; i<featuresList.size(); i++) {
                LinkedList<Object> featureList = (LinkedList<Object>) featuresList.get(i);
                LinkedList<LinkedList<String>> pointList = (LinkedList<LinkedList<String>>) featureList.get(5);
                Coordinate cord[] = new Coordinate[pointList.size()];
                for(int j=0; j<pointList.size();j++) {
                    double lat = Double.parseDouble((pointList.get(j)).get(1))/3600000; // 緯度
                    double lon = Double.parseDouble((pointList.get(j)).get(0))/3600000; // 経度
                    cord[j] = new Coordinate(lon, lat);
                }
                Polygon polygon = geometryFactory.createPolygon(cord);
                featureBuilder.add(polygon);
                featureBuilder.add(featureList.get(0));
                featureBuilder.add(Integer.parseInt((String)featureList.get(1)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(2)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(3)));
                featureBuilder.add(featureList.get(4));
                if(texts.size()>0) {
                    LinkedList<String> textList = (LinkedList<String>) featureList.get(6);
                    for(int j=0; j<textList.size(); j++)
                        featureBuilder.add(textList.get(j));
                }
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
            CreateShape.createShapeFile(outFile, "UTF-8", type, features);
        }
        else {
            for(int i=0; i<featuresList.size(); i++) {
                LinkedList<Object> featureList = (LinkedList<Object>)featuresList.get(i);
                LinkedList<LinkedList<String>> pointList = (LinkedList<LinkedList<String>>)featureList.get(5);
                double lat = Double.parseDouble((pointList.get(0)).get(1))/3600000; // 緯度
                double lon = Double.parseDouble((pointList.get(0)).get(0))/3600000; // 経度
                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
                featureBuilder.add(point);
                featureBuilder.add(featureList.get(0));
                featureBuilder.add(Integer.parseInt((String)featureList.get(1)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(2)));
                featureBuilder.add(Integer.parseInt((String)featureList.get(3)));
                featureBuilder.add(featureList.get(4));
                if(texts.size()>0) {
                    LinkedList<String> textList = (LinkedList<String>) featureList.get(6);
                    for(int j=0; j<textList.size(); j++)
                        featureBuilder.add(textList.get(j));
                }
                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
            CreateShape.createShapeFile(outFile, "UTF-8", type, features);
        }
        System.out.println("[完了]" + file.getName() + "をShapeファイルに変換が完了しました。: " + Thread.currentThread().getId());
    }
}
