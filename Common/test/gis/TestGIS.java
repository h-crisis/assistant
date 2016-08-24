package gis;

import com.google.code.geocoder.model.GeocoderResult;

import java.io.File;
import java.util.List;

/**
 * Created by manabu on 2016/08/23.
 */
public class TestGIS {
    public static void main(String args[]) {
        /*
        List<GeocoderResult> results = gis.GeoCoding.getGeocoderResult("深川市2条18番6号", "ja");
        List<GeocoderResult> results = gis.GeoCoding.getGeocoderResult("北海道滝川保健所", "ja");
        System.out.println(results);
        */
        gis.GeoCoding.addCoordinate(new File("/Users/manabu/Desktop/保健所一覧.csv"), new File("/Users/manabu/Desktop/保健所一覧2.csv"), "Shift_JIS", 3, true);
    }
}
