import java.io.File;

/**
 * Created by manab on 2016/08/12.
 */
public class Main {

    public static void main(String args[]) throws Exception {
        File masterFilesDir = new File("files/master");
        File shapeDir = new File("files/shape");
        File siFile = new File("files/JSHIS/F014822/S-V1-F014822-MAP-CASE1.csv");
        File outDir = new File("files/out");
        String code = "F014822";

        new EarthquakeDamageEstimate(masterFilesDir, shapeDir, siFile, outDir, code);
    }
}
