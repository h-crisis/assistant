package googleapi;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by manabu on 2017/01/14.
 */
public class GoogleApiParser {

    /**
     * Google Map ApiでResponseからresultsの配列を返す。
     * @param jsonObject Apiで得たJson情報
     * @return resultsの配列
     */
    public static JSONArray getResults(JSONObject jsonObject) {
        return jsonObject.getJSONArray("results");
    }

    /**
     * Google Map ApiでResponseから名前を返す。
     * @param jsonObject Apiで得たJson情報
     * @return retultsのJson
     */
    public static JSONObject getReulstsJsonObject(JSONObject jsonObject) {
        return getResults(jsonObject).getJSONObject(0);
    }

    /**
     * Google Map ApiでResponseから住所を返す。
     * @param jsonObject Apiで得たJson情報
     * @return 住所
     */
    public static String getFormattedAddress(JSONObject jsonObject) {
        return getReulstsJsonObject(jsonObject).getString("formatted_address");
    }

    /**
     * Google Map ApiでResponseから経度を返す。
     * @param jsonObject Apiで得たJson情報
     * @return 経度
     */
    public static double getLon(JSONObject jsonObject) {
        return getReulstsJsonObject(jsonObject).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
    }

    /**
     * Google Map ApiでResponseから緯度を返す。
     * @param jsonObject Apiで得たJson情報
     * @return 緯度
     */
    public static double getLat(JSONObject jsonObject) {
        return getReulstsJsonObject(jsonObject).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
    }

    /**
     * Google Map ApiでResponseから名前を返す。
     * @param jsonObject Apiで得たJson情報
     * @return 名称
     */
    public static String getName(JSONObject jsonObject) {
        return getReulstsJsonObject(jsonObject).getString("name");
    }
}
