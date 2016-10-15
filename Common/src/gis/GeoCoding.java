package gis;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by manab on 2016/07/23.
 */
public class GeoCoding {

    /**
     * 指定した住所、ランドマークのGeoCoding結果を返す。
     * @param string   GeoCodingしたい文字列
     * @param language GeoCodingする文字列の言語（en, jp, など）
     * @return GeoCoding結果のリストを返す
     */
    public static List<GeocoderResult> getGeocoderResult(String string, String language) {
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(string).setLanguage(language).getGeocoderRequest();
        try {
            sleep(1000); //1秒間休む
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            List<GeocoderResult> results = geocoderResponse.getResults();
            return results;
        } catch (IOException e) {
            System.out.println("Google GeoCoding中にエラーが生じました。");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Google GeoCoding中にエラーが生じました。");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 緯度、経度からReverse GeoCodingの結果を返す。
     * @param lat 緯度
     * @param lon 経度
     * @param language GeoCodingする文字列の言語（en, jp, など）
     * @return GeoCoding結果のリストを返す
     */
    public static List<GeocoderResult> getReverseGeocoderResult(BigDecimal lat, BigDecimal lon, String language) {
        LatLng location = new LatLng(lat, lon);
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(location).setLanguage(language).getGeocoderRequest();
        try {
            sleep(1000); //1秒間休む
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            List<GeocoderResult> results = geocoderResponse.getResults();
            return results;
        } catch (IOException e) {
            System.out.println("Google GeoCoding中にエラーが生じました。");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Google GeoCoding中にエラーが生じました。");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * CSVファイルの最後に指定した列でGeoCodingを行い緯度と経度の列を追加する
     * @param inFile 入力CSVファイル
     * @param outFile 出力CSVファイル
     * @param encording ファイルの文字コード
     * @param col GeoCodeする入力CSVファイルの列数（最初が0）
     * @param header 1行目が見出し行かの判定
     */
    public static void addCoordinate(File inFile, File outFile, String encording, int col, boolean header) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), encording));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile, false), encording))) {
            // 入力ファイルを全行メモリにストック
            List<String> inLines = new ArrayList<String>();
            String line = null;
            while ((line = br.readLine()) != null) {
                inLines.add(line);
            }

            int counter = 0;
            for (String str : inLines) {
                String lat = "";
                String lon = "";
                if((counter==0) && header) {
                    pw.write(str + ",LAT,LON");
                    counter++;
                }
                else if(counter==0) {
                    String[] pair = str.split(",");
                    List<GeocoderResult> results = getGeocoderResult(pair[col], "ja");
                    if(results.size()>0) {
                        GeocoderResult result = results.get(0);
                        lat = result.getGeometry().getLocation().getLat().toString();
                        lon = result.getGeometry().getLocation().getLng().toString();
                    }
                    pw.write(str + "," + lat + "," + lon);
                    counter++;
                }
                else {
                    String[] pair = str.split(",");
                    List<GeocoderResult> results = getGeocoderResult(pair[col], "ja");
                    if(results.size()>0) {
                        GeocoderResult result = results.get(0);
                        lat = result.getGeometry().getLocation().getLat().toString();
                        lon = result.getGeometry().getLocation().getLng().toString();
                    }
                    pw.write("\n" + str + "," + lat + "," + lon);
                    counter++;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
