package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/12/01.
 */
public class ResetMain {
    public static void main(String args[]) throws Exception {
        String code = args[1];
        File dir = new File(args[0]);
        File outDir = new File(dir.getPath() + "/out/" + code);
        if(!outDir.exists()) {
            System.exit(1);
        }

        new EarthquakeDamageDbReset(outDir, code);
    }
}
