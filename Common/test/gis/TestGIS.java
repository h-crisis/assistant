package gis;

import com.google.code.geocoder.model.GeocoderResult;
import java.util.List;

/**
 * Created by manabu on 2016/08/23.
 */
public class TestGIS {
    public static void main(String args[]) {
        List<GeocoderResult> list = gis.GeoCoding.getGeocoderResult("所沢市東所沢2-56-5-3", "ja");
        System.out.println(list.size() + " : " + list);
    }
}
