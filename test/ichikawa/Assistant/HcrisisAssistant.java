package ichikawa.assistant;

import java.io.IOException;

/**
 * Created by manabu on 2016/06/24.
 */
public class HcrisisAssistant {
    public static void main(String args[]) throws Exception {
        MedicalInstituteEMIS mie = new MedicalInstituteEMIS("files/WorkingFiles/EMIS医療機関情報20160622.csv"
                ,"files/WorkingFiles/medical_status_20160609201410.csv"
        ,"files/WorkingFiles/outFile.csv");
        mie.comineMedicalInstituteFiles();
        mie.createFeatures();
        mie.createShapeFile();
        mie.createGeoJsonFile();
    }
}
