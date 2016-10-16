package gis;

import com.google.code.geocoder.model.GeocoderResult;

import java.io.File;
import java.util.List;

/**
 * Created by manabu on 2016/08/23.
 */
public class TestGIS {
    public static void main(String args[]) {

        //List<GeocoderResult> results = gis.GeoCoding.getGeocoderResult("深川市2条18番6号", "ja");
        List<GeocoderResult> results = gis.GeoCoding.getGeocoderResult("和光市駅", "ja");
        List<GeocoderResult> results2 = gis.GeoCoding.getReverseGeocoderResult(results.get(0).getGeometry().getLocation().getLat(), results.get(0).getGeometry().getLocation().getLng(), "ja");
        System.out.println(results);
        System.out.println(results2.get(0));
        //System.out.println(results.get(0).getGeometry().getLocation().getLat().toString() + ", " + results.get(0).getGeometry().getLocation().getLng().toString() );

        //gis.GeoCoding.addCoordinate(new File("/Users/manabu/Desktop/保健所一覧.csv"), new File("/Users/manabu/Desktop/保健所一覧2.csv"), "Shift_JIS", 12, true);
    }
}
