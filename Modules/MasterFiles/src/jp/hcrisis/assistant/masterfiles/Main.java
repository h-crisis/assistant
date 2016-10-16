package jp.hcrisis.assistant.masterfiles;

import java.io.File;

/**
 * Created by manabu on 2016/10/15.
 */
public class Main {
    public static void main(String args[]) {
        ShelterDbEdit sde = new ShelterDbEdit();
        sde.shelterUpload(new File("files/YahooShelter/0_全国改.csv"));
    }
}
