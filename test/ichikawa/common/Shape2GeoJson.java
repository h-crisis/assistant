package ichikawa.common;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;

import java.io.*;
import java.nio.charset.Charset;


/**
 * Created by manabu on 2016/06/24.
 * ShapeファイルをGeoJsonファイルに変換するクラス
 */
public class Shape2GeoJson {
    /**
     * ShapeファイルからGeoJsonファイルを作成する
     * @param inFile Shapeファイル
     * @param inFileEncoding Shapeファイルの文字コード（Shift_JIS, UTF-8, EUC...）
     * @param outFile GeoJsonファイル
     * @param outFileEncoding GeoJsonファイルの文字コード（Shift_JIS, UTF-8, EUC...）
     * @throws Exception
     */
    public static void createGeoJson(File inFile, String inFileEncoding, File outFile, String outFileEncoding) throws Exception {
        getGeoJSON(getJSON(inFile, inFileEncoding),outFile, outFileEncoding);
    }

    /**
     * ShapeファイルからJSON形式の文字列を返すメソッド
     * @param f Shapeファイル
     * @param encoding Shapeファイルの文字コード
     * @return JSON形式の文字列
     * @throws Exception
     */
    public static String getJSON(File f, String encoding)throws Exception{
        f.setReadOnly();
        ShapefileDataStore store = new ShapefileDataStore(f.toURI().toURL());
        Charset cs=Charset.forName(encoding);
        store.setCharset(cs);
        SimpleFeatureSource source = store.getFeatureSource();
        SimpleFeatureCollection featureCollection = source.getFeatures();
        String geoJson;
        FeatureJSON fj = new FeatureJSON();
        StringWriter writer = new StringWriter();
        fj.writeFeatureCollection(featureCollection, writer);
        geoJson = writer.toString();
        return geoJson;
    }

    /**
     * JSON形式の文字列からGeoJSONファイルを作成する
     * @param json JSON形式の文字列
     * @param outFile GeoJSONファイル
     * @param encoding GeoJSONファイルの文字コード
     * @throws Exception
     */
    public static void getGeoJSON(String json,File outFile,String encoding) throws Exception{
        PrintWriter pw=null;
        try{
            FileOutputStream fos = new FileOutputStream(outFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos,encoding);
            pw = new PrintWriter(osw);
            pw.write(json);
            pw.close();
            pw=null;
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(pw!=null){
                pw.close();
            }
        }
    }
}
