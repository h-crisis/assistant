package ichikawa.setting;


import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import ichikawa.assist.CreateShape;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by manabu on 2016/07/18.
 */
public class CreateBaseMesh5thShapeFile {
    public static void createBaseMesh5th(File meshFile, File municipalitiesFile, File meshDataFile, File outFile,
                                  String meshFileEncording, String municipalitiesFileEncording, String meshDataFileEncording, String outFileEncording) {
        try {
            // 標準5次メッシュファイルを開く
            ShapefileDataStore meshFileStore = new ShapefileDataStore(meshFile.toURI().toURL());
            meshFileStore.setCharset(Charset.forName(meshFileEncording));
            SimpleFeatureSource meshFeatureSource = meshFileStore.getFeatureSource();
            SimpleFeatureCollection meshC = meshFeatureSource.getFeatures();
            FeatureIterator<SimpleFeature> meshI = meshC.features();

            // 行政区域ファイルを開く
            ShapefileDataStore municipalitiesFileStore = new ShapefileDataStore(municipalitiesFile.toURI().toURL());
            municipalitiesFileStore.setCharset(Charset.forName(municipalitiesFileEncording));
            SimpleFeatureSource municipalitiesFeatureSource = municipalitiesFileStore.getFeatureSource();
            SimpleFeatureCollection municipalitiesC = municipalitiesFeatureSource.getFeatures();

            // メッシュデータをマップにする
            System.out.println("createBaseMesh5th: メッシュデータをマップにします。");
            HashMap<String, HashMap<String, String>> meshDataMap = new HashMap<String, HashMap<String, String>>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(meshDataFile), meshDataFileEncording));
            String line;
            while((line = br.readLine())!=null) {
                HashMap<String, String> singleMeshDataMap = new HashMap<String, String>();
                String pair[] = line.split(",");
                for(int i=0; i<pair.length; i++)
                    pair[i].trim();

                singleMeshDataMap.put("POP", pair[1]);
                singleMeshDataMap.put("POP_M", pair[2]);
                singleMeshDataMap.put("POP_F", pair[3]);
                singleMeshDataMap.put("HOUSE", pair[4]);

                meshDataMap.put(pair[0], singleMeshDataMap);
            }
            br.close();
            System.out.println("createBaseMesh5th: メッシュデータをマップにしました。");

            // 各メッシュに地区町村コード、世帯数、人口を割り振る
            System.out.println("createBaseMesh5th: 各メッシュに地区町村コード、世帯数、人口を割り振ります(全 " + meshC.size() + " Feature)。" );

            // Shapeファイルを作成する
            String name = "Mech5th Base";
            String geom = "the_geom:MultiPolygon:srid=4612";
            String text = ",MESH5TH:String,MESH4TH:String,MESH3RD:String,MESH2ND:String,MESH1ST:String,CITYCODE:String,POP:int,POP_M:int,POP_F:int,HOUSE:int";

            // Featureを生成する準備
            SimpleFeatureType type = DataUtilities.createType(name, geom + text);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

            List<SimpleFeature> features = new ArrayList<SimpleFeature>();
            int counter = 0;
            double percent = 0;
            Boolean thread = true;

            ExecutorService executor = Executors.newFixedThreadPool(8);

            while(meshI.hasNext()) {
                SimpleFeature mesh = meshI.next();
                String code = "00000";
                counter++;


                if(thread) { // thread処理
                    Future<SimpleFeature> future = executor.submit(new SearchMunicipalityThread(mesh, municipalitiesC, geometryFactory, featureBuilder, meshDataMap));
                    features.add(future.get());
                }
                else {
                    // 各メッシュが属する市区町村を探索する
                    FeatureIterator<SimpleFeature> municipalitiesI = municipalitiesC.features();
                    while (municipalitiesI.hasNext()) {
                        SimpleFeature municipality = municipalitiesI.next();
                        // メッシュの中心が市区町村の境界内か境界上であればそのコードを取得する
                        if (((MultiPolygon) mesh.getAttribute("the_geom")).getCentroid().within((MultiPolygon) municipality.getAttribute("the_geom")) || ((MultiPolygon) mesh.getAttribute("the_geom")).getCentroid().touches((MultiPolygon) municipality.getAttribute("the_geom"))) {
                            code = (String) municipality.getAttribute("CODE");
                            break;
                        }
                    }
                    municipalitiesI.close();

                    // Featureの作成
                    Polygon polygon = geometryFactory.createPolygon(((MultiPolygon) mesh.getAttribute("the_geom")).getCoordinates());
                    featureBuilder.add(polygon);
                    featureBuilder.add(mesh.getAttribute("MESH5TH"));
                    featureBuilder.add(mesh.getAttribute("MESH4TH"));
                    featureBuilder.add(mesh.getAttribute("MESH3RD"));
                    featureBuilder.add(mesh.getAttribute("MESH2ND"));
                    featureBuilder.add(mesh.getAttribute("MESH1ST"));
                    featureBuilder.add(code);
                    if (meshDataMap.containsKey(mesh.getAttribute("MESH4TH")) && (!code.equals("00000"))) {
                        featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP"))/4));
                        featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP_M"))/4));
                        featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("POP_F"))/4));
                        featureBuilder.add((int)Math.ceil(Integer.parseInt(meshDataMap.get(mesh.getAttribute("MESH4TH")).get("HOUSE"))/4));
                    } else {
                        featureBuilder.add(0);
                        featureBuilder.add(0);
                        featureBuilder.add(0);
                        featureBuilder.add(0);
                    }

                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    features.add(feature);
                }

                if(counter>=(meshC.size()/10000)) {
                    counter = 0;
                    percent = percent + 0.01;
                    System.out.println("\t" + percent + "%完了しました（全 " + meshC.size() + " Feature中)。");
                }

            }
            executor.shutdown();
            System.out.println("createBaseMesh5th: 各メッシュに地区町村コード、世帯数、人口を割り振りました。");

            CreateShape.createShapeFile(outFile, outFileEncording, type, features);

        } catch (MalformedURLException e) {
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
