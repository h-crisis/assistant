package gis;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by manab on 2016/07/23.
 */
public class GeoCoding {

    /**
     * 指定した住所、ランドマークのGeoCoding結果を返す。
     * @param string GeoCodingしたい文字列
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
}
