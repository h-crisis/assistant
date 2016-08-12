package ichikawa.assist;


import ichikawa.common.Shape2GeoJson;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manabu on 2016/06/24.
 */
public class MedicalInstituteEMIS {

    private File medicalInstituteMasterFile;
    private File medicalInstituteStatusFile;
    private File combinedFile;
    private File shapeFile;
    private File jsonFile;
    private SimpleFeatureType simpleFeatureType;
    private List<SimpleFeature> features;

    MedicalInstituteEMIS(String masterFilePath, String statusFilePath, String outFilePath) throws IOException, SchemaException {
        medicalInstituteMasterFile = new File(masterFilePath);
        medicalInstituteStatusFile = new File(statusFilePath);
        combinedFile = new File(outFilePath);
        shapeFile = new File("files/medical_status.shp");
        jsonFile = new File("files/medical_status.geojson");

        simpleFeatureType = createFeatureType();
        features = new ArrayList<SimpleFeature>();

        checkFiles(medicalInstituteMasterFile, medicalInstituteStatusFile, combinedFile);
    }

    // ShapeファイルのFeatureタイプと測地系及び属性値の設定を行う
    private SimpleFeatureType createFeatureType() throws SchemaException {
        SimpleFeatureType type = DataUtilities.createType("Medical Institute",
                "the_geom:Point:srid=4612," +
                "code:String," +
                "name:String," +
                "prefecture:String," +
                "address:String," + // 5
                "assist:String,mds:String,team:String,rd:String,atd:String," + // 5
                "e001:String,e002:String,e003:String,e004:String,e005:String,e006:String,e007:String,e008:String,e009:String,e010:String,e011:String,e012:String," +
                "d_f001:String,d_f002:String,d_f003:String,d_f004:String,d_f005:String," +
                "d_l001:String,d_l002:String,d_l003:String,d_l004:String,d_l005:String,d_l006:String,d_l007:String," +
                "d_c001:String,d_c002:String," +
                "d_p001:String,d_p002:String,d_p003:String,d_p004:String,d_p005:String," +
                "d_k001:String,d_k002:String,d_k003:String,d_k004:String,d_k005:String,d_k006:String,d_k007:String,d_k008:String,d_k009:String,d_k010:String,d_k011:String,d_k012:String,d_k013:String," +
                "d_g001:String,d_g002:String,d_g003:String,d_g004:String,d_g005:String,d_g006:String,d_g007:String," +
                "d_s001:String,d_s002:String,d_s003:String,d_s004:String,d_s005:String,d_s006:String," +
                "d_o:String,d_i:String,d_date:String,aid:String,head:String,end:String"
        );
        System.out.println("CreateFeatureType: " + type);
        return type;
    }

    private static void checkFiles(File masterFile, File statusFile, File outFile) throws IOException {
        if(!masterFile.exists())
            throw new RuntimeException("医療機関マスターファイルがありません。");
        else if(!statusFile.exists())
            throw new RuntimeException("医療機関状況ファイルがありません。");
        else if(!outFile.exists())
            outFile.createNewFile();

        BufferedReader masterFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(masterFile),"Shift_JIS"));
        BufferedReader statusFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(statusFile),"Shift_JIS"));

        String str1 = masterFileBR.readLine();
        String str2 = statusFileBR.readLine();
        int col1 = str1.split(",").length;
        int col2 = str2.split(",").length;
        int line1 = 1;
        int line2 = 1;

        while(str1!=null) {
            if(col1 != str1.split(",").length)
                throw new RuntimeException("医療機関マスターファイルの列数が揃っていません: " + line1);
            str1 = masterFileBR.readLine();
            line1++;
        }

        while(str2!=null) {
            if(col2 != str2.split(",").length) {
                System.out.println(str2.split(",").length + " " + col2);
                throw new RuntimeException("医療機関状況ファイルの列数が揃っていません: " + line2);
            }
            str2 = statusFileBR.readLine();
            line2++;
        }
    }

    public void comineMedicalInstituteFiles() throws IOException {
        System.out.println("医療機関マスターファイルと状況ファイルを結合します");
        HashMap<String, String> masterMap = new HashMap<String, String>();
        HashMap<String, String> statusMap = new HashMap<String, String>();
        LinkedList<String> codeList = new LinkedList<String>();
        BufferedReader masterFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(medicalInstituteMasterFile),"Shift_JIS"));
        BufferedReader statusFileBR = new BufferedReader(new InputStreamReader(new FileInputStream(medicalInstituteStatusFile),"Shift_JIS"));
        PrintWriter pw    = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(combinedFile),"UTF-8")));

        String masterStr;
        while((masterStr = masterFileBR.readLine()) != null) {
            String pair[] = masterStr.split(",");
            for(int i =0; i<pair.length; i++)
                pair[i].trim();

            codeList.addLast(pair[0]);
            masterMap.put(pair[0], masterStr);
        }
        masterFileBR.close();


        String statusStr;
        while((statusStr = statusFileBR.readLine()) != null) {
            String pair[] = statusStr.split(",");
            for(int i =0; i<pair.length; i++)
                pair[i].trim();

            String str = "," + pair[3] + "," + pair[4];
            for(int i = 6; i<pair.length; i++) {
                str = str + "," + pair[i];
            }
            statusMap.put(pair[0], str);
        }
        statusFileBR.close();

        for(int i=0; i<codeList.size(); i++) {
            String code = codeList.get(i);
            String str = masterMap.get(code);
            if(statusMap.containsKey(code)) {
                String pair[] = statusMap.get(code).split(",");
                for(int j=1; j<pair.length; j++)
                    str = str + "," + pair[j];
            }
            else
                str = str + ",未,未入力,0,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,,,,,,,,0,0,0,0,0,0,,,,0,,1";
            pw.println(str);
        }
        pw.close();
        System.out.println("医療機関マスターファイルと状況ファイルを結合が完了しました");
    }

    public void createFeatures() throws Exception {
        System.out.println("Featureの作成を開始します");
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(combinedFile),"UTF-8"));
        try {
            String line = br.readLine();

            while((line=br.readLine())!=null) {
                String pair[] = line.split(",");

                double lat = Double.parseDouble(pair[6]);
                double lon = Double.parseDouble(pair[7]);
                String code = pair[0];
                String name = pair[1];
                String prefecture = pair[4];
                String address = pair[5];

                Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
                featureBuilder.add(point);
                featureBuilder.add(code);
                featureBuilder.add(name);
                featureBuilder.add(prefecture);
                featureBuilder.add(address);
                for(int i=12; i < pair.length; i++) {
                    if(pair[i].length()>50) {
                        System.out.println(pair[i] + ">>" + pair[i].substring(0, 50));
                        featureBuilder.add(pair[i].substring(0, 50) + "...");
                    }
                    else
                        featureBuilder.add(pair[i]);
                }

                SimpleFeature feature = featureBuilder.buildFeature(null);
                features.add(feature);
            }
        }
        finally {
            br.close();
        }
        System.out.println(features.size() + "のFeatureの作成が完了しました");
    }

    public void createShapeFile() throws Exception {
        CreateShape.createShapeFile(shapeFile, "utf-8", simpleFeatureType, features);
    }

    public void createGeoJsonFile() throws Exception {
        Shape2GeoJson.createGeoJson(shapeFile, "utf-8", jsonFile, "utf-8");
    }
}
