package jp.hcrisis.assistant.disaster;

import java.io.File;

/**
 * Created by manabu on 2016/12/01.
 */
public class MainCustomSet {
    public static void main(String args[]) throws Exception {
        String eventCode = args[0];
        String shelterCode = args[1];

        new EarthquakeDamageDbCustomSet(eventCode, shelterCode);
    }
}
