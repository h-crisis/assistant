package ichikawa;

/**
 * Created by manabu on 2016/05/26.
 */
public class HcrisisAssistant {
    public static void main(String args[]) throws Exception {
        CombineSi.siCombine("files/ShapeFiles/municipalities/municipalities.shp", "files/ShapeFiles/halls/halls.shp", "files/ShapeFiles/si/si.shp", "files/ShapeFiles/out/out.shp");
    }
}
