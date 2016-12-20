package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/12/01.
 */
public class MainExcelSet {
    public static void main(String args[]) throws Exception {
        String code = args[1];
        File file = new File(args[0]);
        if(!file.exists()) {
            System.exit(1);
        }

        new EarthquakeDamageDbExcelSet(file, code);
    }
}
