package ichikawa.assist;

/**
 * Created by manabu on 2016/06/24.
 */
public class HcrisisAssistant {
    public static void main(String args[]) throws Exception {
        doMedicalInstituteEMIS();
        // doSiCombine();
    }

    /**
    public static void doSiCombine() {
        File file = new File("files/WorkingFiles/EMIS医療機関情報_kochi.csv");
        HashMap<String, Double> map = Si.getSiMap(file, "Shift_JIS");
        System.out.println(map);
    }
     */

    public static void doMedicalInstituteEMIS() throws Exception {
        MedicalInstituteEMIS mie = new MedicalInstituteEMIS("files/WorkingFiles/EMIS医療機関情報_kochi.csv"
                ,"files/WorkingFiles/medical_status_20160630143035.csv"
                ,"files/WorkingFiles/outFile_20160630143035.csv");
        mie.comineMedicalInstituteFiles();
        mie.createFeatures();
        mie.createShapeFile();
        mie.createGeoJsonFile();
    }
}
